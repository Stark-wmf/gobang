package com.redrock.exam.gobang.service;

import com.alibaba.fastjson.JSONObject;
import com.redrock.exam.gobang.dao.UserMapper;
import com.redrock.exam.gobang.entity.SerUser;
import com.redrock.exam.gobang.entity.User;
import com.redrock.exam.gobang.util.JSONResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    RedisTemplate redisTemplate;

    public JSONObject register(String username,String password){
        String uuid = UUID.randomUUID().toString();
        User user = new User();
        user.setUsername(username);
        user.setUuid(uuid);
        user.setPassword(password);
        if(userMapper.IfUserExist(username)==1){
            return JSONResponse.Fail("该用户名已存在");
        }else{
            userMapper.register(user);
            return JSONResponse.Success("注册成功");
        }
    }


    public JSONObject login (String username, String password, HttpServletResponse response){
        String token = UUID.randomUUID().toString();
        response.setHeader("token",token);
        User user = userMapper.getPassword(username);
       // User curuser = new User(user.getUserid(),user.getUsername());
        if(user.getPassword().equals(password)){
            SerUser redisuser = new SerUser();
            redisuser.setUsername(user.getUsername());
            redisuser.setId(user.getUserid());
            redisuser.setUuid(user.getUuid());
            redisTemplate.opsForValue().set(token,redisuser,7, TimeUnit.DAYS);
            return JSONResponse.Response(1,"ok",redisuser);
        }
        return JSONResponse.Fail("密码错误");
    }

    public int getUserid(String username) {return userMapper.getUserid(username);
    }
}
