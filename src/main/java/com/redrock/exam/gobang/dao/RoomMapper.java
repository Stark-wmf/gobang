package com.redrock.exam.gobang.dao;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface RoomMapper {

    @Insert("INSERT INTO room SET roomid = #{roomid},roompwd=#{roompwd},ownerid =#{ownerid},ownerStatus=0")
    void addRoom(int roomid,int ownerid,String roompwd);


    @Update("UPDATE room SET joinerid=#{joinerid},joinerStatus=0 WHERE roomid=#{roomid} and roompwd=#{roompwd}")
    void joinroom(int roomid,String roompwd,int joinerid);

    @Select("SELECT COUNT(roomid) FROM room WHERE roomid =#{roomid} ")
    int IfRoomExist(int roomid);

    @Select("SELECT roompwd FROM room WHERE roomid =#{roomid} ")
    String getPassword(int roomid);

    @Select("SELECT joinerid FROM room WHERE roomid =#{roomid} ")
    int getJoiner(int roomid);

    @Update("UPDATE room SET joinerStatus=1 WHERE roomid=#{roomid}")
    void joinerReady(int roomid);

    @Update("UPDATE room SET ownerStatus=1 WHERE roomid=#{roomid}")
    void ownerReady(int roomid);

    @Select("SELECT joinerid FROM room WHERE roomid =#{roomid} ")
    int IfJoiner(int roomid);

    @Select("SELECT ownerid FROM room WHERE roomid =#{roomid} ")
    int IfOwner(int roomid);

    @Select("SELECT joinerStatus FROM room WHERE roomid =#{roomid} ")
    int IfJoinerReady(int roomid);

    @Select("SELECT ownerStatus FROM room WHERE roomid =#{roomid} ")
    int IfOwnerReady(int roomid);
}
