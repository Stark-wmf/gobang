package com.redrock.exam.gobang.controller.websocket;

import com.redrock.exam.gobang.entity.Game;
import com.redrock.exam.gobang.entity.SerUser;
import com.redrock.exam.gobang.entity.Step;
import com.redrock.exam.gobang.service.ChessService;
import com.redrock.exam.gobang.service.GameService;
import com.redrock.exam.gobang.service.RoomService;
import com.redrock.exam.gobang.service.UserService;
import com.redrock.exam.gobang.util.JSONResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

@Component
@ServerEndpoint("/game")
public class GoBangWebsocket {
    private static final String BLACK ="balck";
    private static final String WHITE ="white";
    private static final Set<GoBangWebsocket> GO_BANG_GAME_SET = new CopyOnWriteArraySet<>();
    //当前用户信息
    private SerUser user = null;
    private String username = null;
    private String lastUser;
    //redis
    private static RedisTemplate redisTemplate;
    private ChessService chessService;
    private GameService gameService;
    private RoomService roomService;
    private UserService userService;
    private String token;
    private Session session;
    private int[][] chessboard = new int[15][15];
    private static int count = 0;
    private List<Step> ownerstepList;
    private List<Step> joinerstepList;
    @OnOpen
    public void onOpen(@PathParam("token") String token, @PathParam("roomid") int roomid, Session session) throws IOException {
        SerUser userInRedis = (SerUser) redisTemplate.opsForValue().get(token);
        if (userInRedis == null) {
            session.getBasicRemote().sendText(JSONResponse.notLogin().toJSONString());
            return;
        }
        this.username = userInRedis.getUsername();
        this.session = session;
        this.user = userInRedis;

        if (GO_BANG_GAME_SET.size() > 1) {
            System.out.println("房间人满");
            session.getBasicRemote().sendText("房间人已满!");
//            session.close();
        } else {
            GO_BANG_GAME_SET.add(this);
            StringBuffer sb = new StringBuffer();
            for (GoBangWebsocket goBangWebsocket : GO_BANG_GAME_SET) {
                sb.append(goBangWebsocket.username).append(";");
            }
            sendText(username + "进入房间;当前房间有:" + sb.toString());
        }
    }
    @OnMessage
    public void onMessage(String message, @PathParam("username") String username,@PathParam("roomid") int roomid) {
        //认输，/ff在lol里是发起投降的意思
        if(message=="/ff"){
            sendText(username+"认输了");
            return;
        }
        //双方轮流下子
        if (username == lastUser) {
            System.out.println("请等候对方落子");
            return;
        }
        //双方准备才可以开始游戏
       boolean flag= roomService.ifjoinerReady(roomid)&&roomService.ifownerReady(roomid);
        if(flag==false) {
            return;
        }
        Game game = new Game();
        game.setGameid(TimeUnit.values().toString());
        int i = count++ % 2;
        String[] split = message.split("-");
        int x = Integer.parseInt(split[0]);
        int y = Integer.parseInt(split[1]);
        //判断该坐标是否已经落子
        if(chessService.ifstepexist(x,y,game.getGameid())==1){
            sendText("这里已经有棋了");
            return;
        }
        chessboard[y][x] = i;

        if(i==0){
            Step step = new Step(x,y,WHITE,count);
            joinerstepList.add(step);
        }else{
            Step step = new Step(x,y,BLACK,count);
            ownerstepList.add(step);
        }
        int success = isSuccess(x, y, i, chessboard);
        sendText(message + "-" + i);
        if (success == -1) {
            lastUser = username;
        } else {
            if (i == 0) {
                sendText("白棋获胜");

                return;
            } else if (i == 1) {
                sendText("黑棋获胜");
                return;
            }

        }
        for(int k=0;k<ownerstepList.size();k++){
            Step ownersteps = ownerstepList.get(k);//获取每一个ownersteps对象
            int stepx = ownersteps.getX();
            int stepy = ownersteps.getY();
            int times = ownersteps.getTimes();
            chessService.insertstep(times,stepx,stepy,user.getId(),BLACK,game.getGameid());
        }
        for(int v=0;v<joinerstepList.size();v++){
            Step joinersteps = joinerstepList.get(v);//获取每一个Example对象
            int stepx = joinersteps.getX();
            int stepy = joinersteps.getY();
            int times = joinersteps.getTimes();
            chessService.insertstep(times,stepx,stepy,user.getId(),WHITE,game.getGameid());
        }
    }
    //此处的f就是上面的i，代表棋色
    private static int isSuccess(int x, int y, int f, int[][] chessboard) {
        //y的范围在0-14之间，x的范围在0-14之间
        int count = 0;
        for (int i = x - 1; i > -1; i--) {
            if (chessboard[y][i] != f) {
                break;
            }
            count++;
        }
        for (int i = x + 1; i < 15; i++) {
            if (chessboard[y][i] != f) {
                break;
            }
            count++;
        }
        if (count > 3) {
            return f;
        }
        count = 0;
        for (int i = y + 1; i < 15; i++) {
            if (chessboard[i][x] != f) {
                break;
            }
            count++;
        }
        for (int i = y - 1; i > -1; i--) {
            if (chessboard[i][x] != f) {
                break;
            }
            count++;
        }
        if (count > 3) {
            return f;
        }
        count = 0;
        for (int i = x + 1, j = y + 1; i < 15; i++, j++){
            if (j < 15) {
                if (chessboard[j][i] != f) {
                    break;
                }
                count++;
            } else {
                break;
            }
        }
        for (int i = x - 1, j = y - 1; i > -1; i--, j--) {
            if (j > -1) {
                if (chessboard[j][i] != f) {
                    break;
                }
                count++;
            } else {
                break;
            }
        }
        if (count > 3) {
            return f;
        }
        count = 0;
        for (int i = x + 1, j = y - 1; i < 15; i++, j--) {
            if (j > -1) {
                if (chessboard[j][i] != f) {
                    break;
                }
                count++;
            } else {
                break;
            }
        }
        for (int i = x - 1, j = y + 1; i > -1; i--, j++) {
            if (j < 15) {
                if (chessboard[j][i] != f) {
                    break;
                }
                count++;
            } else {
                break;
            }
        }
            if (count > 3) {
                return f;
            }
            return -1;
        }

    private static void sendText(String msg) {
        for (GoBangWebsocket goBangWebsocket : GO_BANG_GAME_SET) {
            try {
                synchronized (goBangWebsocket) {
                    goBangWebsocket.session.getBasicRemote().sendText(msg);
                }
            } catch (IOException e) {
                GO_BANG_GAME_SET.remove(goBangWebsocket);
                try {
                    goBangWebsocket.session.close();
                } catch (IOException e1) {
                }
                sendText(goBangWebsocket.username + "已下线");
            }
        }
    }
}
