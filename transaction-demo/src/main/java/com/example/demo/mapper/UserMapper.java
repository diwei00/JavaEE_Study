package com.example.demo.mapper;

import com.example.demo.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    // 删除操作
    int del(@Param("id") Integer id);

    // 增加用户
    int add(UserInfo userInfo);

}
