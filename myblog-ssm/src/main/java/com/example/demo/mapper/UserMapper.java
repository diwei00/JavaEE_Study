package com.example.demo.mapper;

import com.example.demo.entity.Userinfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    // 注册
    int reg(Userinfo userinfo);

    // 登录
    Userinfo login(@Param("username")String username);

    int modifyPassword(@Param("id")Integer id, @Param("newPassword")String newPassword);

    Userinfo getUserByUsername(@Param("username")String username);

    int updatePassword(Userinfo userinfo);



}
