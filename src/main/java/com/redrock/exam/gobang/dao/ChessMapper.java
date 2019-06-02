package com.redrock.exam.gobang.dao;

import com.redrock.exam.gobang.entity.Step;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChessMapper {

    @Insert(" INSERT INTO step SET times=#{times},stepx = #{stepx},stepy=#{stepy},userid =#{userid},color=#{color},gameid=#{gameid}")
    void addStep(int times,int stepx,int stepy,int userid,String color,String gameid);

    @Select("SELECT * FROM step WHERE gameid =#{gameid}  order by times desc")
    List<Step> getStep(String gameid);

    @Select("SELECT COUNT(id) FROM step WHERE stepx = #{stepx},stepy=#{stepy}，gameid=#{gameid} ")
    int IfStepExist(int stepx,int stepy,String gameid);
}

