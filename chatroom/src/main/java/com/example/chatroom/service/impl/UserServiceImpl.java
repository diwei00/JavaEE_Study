package com.example.chatroom.service.impl;

import com.example.chatroom.entity.User;
import com.example.chatroom.mapper.UserMapper;
import com.example.chatroom.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service

public class UserServiceImpl implements IUserService {
    @Autowired
    UserMapper userMapper;

    public User selectByName(String username) {
        return userMapper.selectByName(username);

    }

    /**
     * 注册
     * 用户名重复就会注册失败，会抛出异常
     * @param user
     * @return
     * @throws DuplicateKeyException
     */
    public int insert(User user) throws DuplicateKeyException {

        return userMapper.insert(user);
    }

    public String getEmail(String username) {
        return userMapper.getEmailByUserId(username);
    }

    public int changePassword(String newPassword, String username) {
        return userMapper.changePassword(newPassword, username);
    }

    public int saveUserImg(String userImgName, Integer userId) {
        return userMapper.saveUserImg(userImgName, userId);
    }
}
