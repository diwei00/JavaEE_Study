package com.example.chatroom.mapper;


import com.example.chatroom.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    // 注册用户
    int insert(User user);

    // 根据用户名查找用户
    User selectByName(@Param("username") String username);

    String getEmailByUserId(@Param("username") String username);

    int changePassword(@Param("newPassword") String newPassword, @Param("username") String username);

}
