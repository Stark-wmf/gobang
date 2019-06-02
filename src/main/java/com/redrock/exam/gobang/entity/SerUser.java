package com.redrock.exam.gobang.entity;

import lombok.Data;

import java.io.Serializable;
//序列化的User对象，好存redis里面
@Data
public class SerUser implements Serializable {
    private int id;
    private String username;
    private String uuid;
}