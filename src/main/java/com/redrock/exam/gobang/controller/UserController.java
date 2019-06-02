package com.redrock.exam.gobang.controller;


import com.alibaba.fastjson.JSONObject;
import com.redrock.exam.gobang.entity.SerUser;
import com.redrock.exam.gobang.service.RoomService;
import com.redrock.exam.gobang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    RoomService roomService;
    @Autowired
    RedisTemplate redisTemplate;

    @RequestMapping("/login")
    public JSONObject login(String username, String password, HttpServletResponse response){
        return userService.login(username,password,response);
    }
    @RequestMapping("/register")
    public Object register(String username,String password){
        return userService.register(username,password);
    }

    @RequestMapping("/joinerready")
    public Object joinerready(int roomid,HttpServletResponse response){
        String token=response.getHeader("token");
        SerUser userInRedis = (SerUser) redisTemplate.opsForValue().get(token);
        return roomService.joinerReady(roomid,userInRedis.getId());
    }

    @RequestMapping("/ownerready")
    public Object ownerready(int roomid,HttpServletResponse response){
        String token=response.getHeader("token");
        SerUser userInRedis = (SerUser) redisTemplate.opsForValue().get(token);
        return roomService.ownerReady(roomid,userInRedis.getId());
    }
}
