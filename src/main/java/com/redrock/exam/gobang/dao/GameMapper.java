package com.redrock.exam.gobang.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GameMapper {

    @Insert(" INSERT INTO game SET gameid=#{gameid},winnerid = #{winnerid},loserid=#{loserid}")
    void end(int winnerid,int loserid,String gameid);
}
