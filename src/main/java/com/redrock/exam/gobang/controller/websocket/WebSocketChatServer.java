package com.redrock.exam.gobang.controller.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.redrock.exam.gobang.entity.SerUser;
import com.redrock.exam.gobang.entity.User;
import com.redrock.exam.gobang.service.RoomService;
import com.redrock.exam.gobang.service.UserService;
import com.redrock.exam.gobang.util.JSONResponse;
import com.redrock.exam.gobang.util.UserContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

/**
 * WebSocket 聊天服务端
 *
 * @see ServerEndpoint WebSocket服务端 需指定端点的访问路径
 * @see Session   WebSocket会话对象 通过它给客户端发送消息
 */
@Component
@ServerEndpoint("/chatRoom/{roomid}/{token}")
public class WebSocketChatServer {
    private static final Logger log = LoggerFactory.getLogger(WebSocketChatServer.class);
    //保存用户的session
    private static Map<Integer, Session> userMap = new HashMap<>();
    //
    private static Map<String, WebSocketChatServer> clients = new ConcurrentHashMap<String, WebSocketChatServer>();
    //房间
    private int roomid=0;

    //redis
    private static RedisTemplate redisTemplate;

    private static RoomService roomService;
    private Session session;
    private String sence ;
    private String token;
    //当前用户信息
    private SerUser user = null;

    @Autowired
    public void setRedsTemp(@Qualifier("redisTemplate") RedisTemplate redsTemp){
        WebSocketChatServer.redisTemplate = redsTemp;
    }
    //房间是个群聊
    @OnOpen
    public void onOpen(Session session, @PathParam("roomid") int roomid, @PathParam("token") String token,@PathParam("roompwd") String roompwd) throws IOException {
        System.out.println("连接");
        //获取当前用户
        SerUser userInRedis = (SerUser) redisTemplate.opsForValue().get(token);
        if(userInRedis == null){
            session.getBasicRemote().sendText(JSONResponse.notLogin().toJSONString());
            return;
        }
        else {
            this.user = userInRedis;
            this.roomid = roomid;

            //更新过期时间
            redisTemplate.expire(token,30, TimeUnit.MINUTES);
            //和好友的场景值
           // sence = friendService.getFriendSence(toWho);
            this.token = token;
            //添加当前用户线程集合
            UserContextUtil.addUser(this.user);
            userMap.put(user.getId(), session);
            //判断该roomid是否已存在房间，若存在则加入房间，若不存在则创建房间
            if(roomService.roomExist(roomid)){
                roomService.joinroom(roomid,roompwd,userInRedis.getId());
            }else {
                roomService.insertroom(userInRedis.getId(),roomid,roompwd);
            }
            log.info("房间聊天系统连接成功，用户："+user+"进入了房间："+roomid);
        }
    }

    @OnClose
    public void onClose(){
        if(this.user != null) {
            System.out.println("连接关闭:"+user.getUsername());
            userMap.remove(user.getId());
        }
    }
    @OnError
    public void onError(Throwable error) throws IOException {
        if(user != null) {
            userMap.get(user.getId()).getBasicRemote().sendText(JSONResponse.Fail("房间连接出错").toJSONString());
        }
        log.info("房间聊天系统出现错误:{}"+error);
    }
    @OnMessage
    public void onMessage(String message) throws IOException {

        if (this.user == null) {
            return;
        }
        sendMessagetoAll(message);

            log.info("房间" + roomid + "内，用户：" + user + "发言"+message);

    }
    public void sendMessagetoAll (String message) throws IOException {
        for (WebSocketChatServer item : clients.values()) {
            item.session.getAsyncRemote().sendText(message);
        }
    }


}