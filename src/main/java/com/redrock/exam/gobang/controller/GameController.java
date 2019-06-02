package com.redrock.exam.gobang.controller;

import com.alibaba.fastjson.JSONObject;
import com.redrock.exam.gobang.service.ChessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/game")
public class GameController {
    @Autowired
    private ChessService chessService;

    //回放棋局，回放你选择的棋局所有步数，按是第几步降序排放
    @RequestMapping("/getGame")
    public Object getList(String gameid){

        return  chessService.getList(gameid);
    }
}
