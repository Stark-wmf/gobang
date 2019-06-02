package com.redrock.exam.gobang.entity;

import lombok.Data;

@Data
public class Game {
    private String gameid;

    private String winner;

    private String loser;

    private int times;

}
