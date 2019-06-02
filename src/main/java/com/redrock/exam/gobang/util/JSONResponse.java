package com.redrock.exam.gobang.util;

import com.alibaba.fastjson.JSONObject;

public class JSONResponse {

    /**
     * @param code 状态码
     * @param msg 状态信息
     * @param data 数据
     *
     */
    public static JSONObject Response(Integer code, String msg, Object data ){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",code);
        jsonObject.put("msg",msg);
        jsonObject.put("data",data);
        return jsonObject;
    }

    /**
     * @param msg 错误信息状态信息
     */
    public static JSONObject Fail(String msg){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg",msg);
        jsonObject.put("data",null);
        return jsonObject;
    }
    /**
     * @param data 成功
     */
    public static JSONObject Success(Object data){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",1);
        jsonObject.put("msg","操作成功");
        jsonObject.put("data",data);
        return jsonObject;
    }
    /**
     * 没有登录
     */
    public static JSONObject notLogin(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",4000);
        jsonObject.put("msg","没有登录");
        jsonObject.put("data",null);
        return jsonObject;
    }

}