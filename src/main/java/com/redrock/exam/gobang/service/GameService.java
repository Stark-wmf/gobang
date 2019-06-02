package com.redrock.exam.gobang.service;

import com.redrock.exam.gobang.dao.GameMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class GameService {
    @Autowired
    private GameMapper gameMapper;


    public void gameend(int winnerid,int loserid,String gameid) {
        gameMapper.end(winnerid,loserid,gameid);
        return ;
    }
}
