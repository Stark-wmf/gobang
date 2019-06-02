package com.redrock.exam.gobang.intercpter;
import com.redrock.exam.gobang.entity.SerUser;
import com.redrock.exam.gobang.entity.User;
import com.redrock.exam.gobang.util.JSONResponse;
import com.redrock.exam.gobang.util.UserContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;



@Order(1)
public class IntercepterLogin implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(IntercepterLogin.class);
    /**
     * redis
     */
    @Autowired
    RedisTemplate redisTemplate;

    /**
     * token ,用户登录标识
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse res, Object o) throws Exception {
        String token = request.getHeader("token");
        res.setContentType("application/json;charset=utf-8");
        System.out.println("拦截器");
        if (token == null) {
            res.getWriter().write(JSONResponse.notLogin().toJSONString());
            return false;
        }
        SerUser user = (SerUser) redisTemplate.opsForValue().get(token);
        if (user == null) {
            res.getWriter().write(JSONResponse.notLogin().toJSONString());
            return false;
        } else {
            redisTemplate.expire(token, 7, TimeUnit.DAYS);
            System.out.println("user :"+user);
            UserContextUtil.addUser(user);
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        UserContextUtil.clear();
    }
}