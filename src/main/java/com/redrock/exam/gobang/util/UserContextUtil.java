package com.redrock.exam.gobang.util;

import com.redrock.exam.gobang.entity.SerUser;
import com.redrock.exam.gobang.entity.User;


public class UserContextUtil {
    private static ThreadLocal<SerUser> entrySet = new ThreadLocal<>();

    /**
     * 这个用户信息set集合插入User
     */
    public static void addUser(SerUser user) {
        entrySet.set(user);
    }

    /**
     * 获取用户信息
     */
    public static Object getUserInfor() {
        return entrySet.get();
    }

    /**
     * 获取用户ID
     */
    public static Integer getUserId() {
        return entrySet.get().getId();
    }

    /**
     * 清除本次请求连接
     */
    public static void clear() {
        entrySet.remove();
    }

    /**
     * 直接获取 user 实例
     */

    public static   SerUser getUser() {
        return entrySet.get();
    }
}
