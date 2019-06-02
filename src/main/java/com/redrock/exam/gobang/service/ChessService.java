package com.redrock.exam.gobang.service;

import com.alibaba.fastjson.JSONObject;
import com.redrock.exam.gobang.dao.ChessMapper;
import com.redrock.exam.gobang.entity.Step;
import com.redrock.exam.gobang.util.JSONResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChessService {
    @Autowired
    private ChessMapper chessMapper;


    public void insertstep(int times,int stepx,int stepy,int userid,String color,String gameid) {
        chessMapper.addStep(times,stepx,stepy,userid,color,gameid);
            return ;
        }


    public List<Step> getList (String gameid){
       List<Step> list= chessMapper.getStep(gameid);
       return list;
    }


    public int ifstepexist(int stepx,int stepy,String gameid) {

        return chessMapper.IfStepExist(stepx,stepy,gameid);
    }

}

