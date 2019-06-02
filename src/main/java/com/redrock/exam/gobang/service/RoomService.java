package com.redrock.exam.gobang.service;

import com.alibaba.fastjson.JSONObject;
import com.redrock.exam.gobang.dao.RoomMapper;
import com.redrock.exam.gobang.entity.User;
import com.redrock.exam.gobang.util.JSONResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class RoomService {
    @Autowired
    private RoomMapper roomMapper;
    @Transactional
    public JSONObject insertroom(int ownerid, int roomid, String roompwd) {

        if (roomMapper.IfRoomExist(roomid) == 1) {
            return JSONResponse.Fail("该房间号已存在");
        } else {
            roomMapper.addRoom(roomid, ownerid, roompwd);
            return JSONResponse.Success("创建成功");
        }
    }
    @Transactional
    public JSONObject joinroom(int roomid, String roompwd, int joinerid) {

        if (roomMapper.IfRoomExist(roomid) == 0 && roomMapper.getPassword(roomid) != roompwd && roomMapper.getJoiner(roomid) == 0) {
            return JSONResponse.Fail("无法加入该房间，请检查是否填写正确且可加入的房间信息");
        } else {
            roomMapper.joinroom(roomid, roompwd, joinerid);
            return JSONResponse.Success("加入房间" + roomid + "成功");
        }
    }
    @Transactional
    public boolean roomExist(int roomid) {
        if (roomMapper.IfRoomExist(roomid) == 1) {
            return true;
        }
        return false;
    }
    @Transactional
    public JSONObject joinerReady(int roomid,int joinerid) {

        if (roomMapper.IfRoomExist(roomid) == 0&&roomMapper.IfJoiner(roomid)!=joinerid) {
            return JSONResponse.Fail("请确定您是否加入了该房间");
        } else {
            roomMapper.joinerReady(roomid);
            return JSONResponse.Success(joinerid+"成功准备");
        }
    }
    @Transactional
    public JSONObject  ownerReady(int roomid,int ownerid) {

        if (roomMapper.IfRoomExist(roomid) == 0&&roomMapper.IfOwner(roomid)!=ownerid) {
            return JSONResponse.Fail("请确定您是否加入了该房间");
        } else {
            roomMapper.ownerReady(roomid);
            return JSONResponse.Success(ownerid+"成功准备");
        }
    }
    @Transactional(propagation= Propagation.NOT_SUPPORTED)
    public boolean  ifjoinerReady(int roomid) {
        if(roomMapper.IfJoinerReady(roomid)==1){

            return true;
        }
        return  false;
    }
    @Transactional(propagation= Propagation.NOT_SUPPORTED)
    public boolean  ifownerReady(int roomid) {
        if(roomMapper.IfOwnerReady(roomid)==1){

            return true;
        }
        return  false;
    }
}
