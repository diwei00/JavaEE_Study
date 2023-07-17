package com.example.demo.service;

import com.example.demo.entity.Log;
import com.example.demo.entity.UserInfo;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LogService logService;

    public int del(Integer id) {
        return userMapper.del(id);

    }

    @Transactional(propagation = Propagation.NESTED)
    public int add(UserInfo userInfo) {
        // 给用户表添加信息
        int addResult = userMapper.add(userInfo);
        System.out.println("添加用户结果: " + addResult);

        // 添加日志信息
        Log log = new Log();
        log.setMessage("添加用户信息");
        logService.add(log);

        return addResult;



    }
}
