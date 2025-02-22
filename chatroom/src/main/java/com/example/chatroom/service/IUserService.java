package com.example.chatroom.service;

import com.example.chatroom.entity.User;

/**
 * @Description
 * @Author wh
 * @Date 2025/2/22 15:33
 */
public interface IUserService {

    User selectByName(String username);

    int insert(User user);

    String getEmail(String username);

    int changePassword(String newPassword, String username);

    int saveUserImg(String userImgName, Integer userId);
}
