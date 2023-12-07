package com.example.demo.service;

import com.example.demo.entity.Userinfo;
import com.example.demo.mapper.UserMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public int reg(Userinfo userinfo) throws DuplicateKeyException{
        return userMapper.reg(userinfo);
    }
    public Userinfo login(String username) {
        return userMapper.login(username);
    }

    public int modifyPassword(Integer id, String newPassword) {
        return userMapper.modifyPassword(id, newPassword);
    }

    public Userinfo getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    public int updatePassword(Userinfo userinfo) {
        return userMapper.updatePassword(userinfo);
    }

}
