package com.redrock.exam.gobang.entity;

import lombok.Data;

@Data
public class User {

    private int userid;

    private String username;

    private String password;

    //这个状态用来删除和恢复用户账号
    private int status;

    //unique id
    private String uuid;

     public User(){

     }

    public User(String userid,String username){

    }
}
