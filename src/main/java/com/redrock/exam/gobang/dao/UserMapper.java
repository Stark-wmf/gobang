package com.redrock.exam.gobang.dao;

import com.redrock.exam.gobang.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("Select * from user where username = #{username}")
    User getPassword(String username);

    @Insert(" INSERT INTO user SET username = #{username},password=#{password},uuid =#{uuid}")
    void register(User user);

    @Select("SELECT COUNT(userid) FROM user WHERE username =#{username} ")
    int IfUserExist(String username);

    @Select("Select userid from user where username = #{username}")
    int getUserid(String username);

}
