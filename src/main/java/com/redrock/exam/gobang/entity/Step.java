package com.redrock.exam.gobang.entity;

import lombok.Data;

@Data
public class Step {

    private int x;

    private int y;
//i=0为黑子，i=1为白子
    private String color;

    private int times;

    public Step(int x,int y ,String color,int times){
        this.x=x;
        this.y=y;
        this.color=color;
        this.times=times;
    }

}
