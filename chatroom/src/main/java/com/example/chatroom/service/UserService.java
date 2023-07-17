package com.example.chatroom.service;

import com.example.chatroom.entity.User;
import com.example.chatroom.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.zip.DataFormatException;

@Service
public class UserService {
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
}
