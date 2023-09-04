package com.example.chatroom.service;

import com.example.chatroom.entity.User;
import com.example.chatroom.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    void insert() {
        User user = new User();
        user.setUserId(500);
        user.setUsername("wu");
        user.setPassword("111");
        user.setEmail("222");
        userMapper.insert(user);
    }
}