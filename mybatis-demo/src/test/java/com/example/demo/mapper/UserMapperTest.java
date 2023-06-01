package com.example.demo.mapper;

import com.example.demo.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // 当前单元测试类是运行在 spring boot 环境中的
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void getAll() {
        List<UserEntity> list = userMapper.getAll();
        System.out.println(list.size());
    }

    @Test
    void getUserById() {
        UserEntity user = userMapper.getUserById(2);
        System.out.println(user);

    }

    @Test
    void getUserByName() {
        UserEntity user = userMapper.getUserByName("wuhao");
        System.out.println(user);
    }


    @Test
    void login() {
        // 测试sql注入
        String username = "admin";
        String password = "' or 1='1";
        UserEntity inputUser = new UserEntity();
        inputUser.setUsername(username);
//        inputUser.setPassword(password);
        UserEntity user = userMapper.login(inputUser);
        System.out.println(user);

    }

    @Test
    void getAllByIdOrder() {
        List<UserEntity> list = userMapper.getAllByIdOrder("desc");
        System.out.println(list.size());
    }

    // 事务，会进行回滚操作。保证数据库数据不会被单元测试污染
    @Transactional
    @Test
    void updatePassword() {
        int result = userMapper.updatePassword("1", "admin", "123");
        System.out.println(result);
    }


    @Transactional
    @Test
    void deleteById() {
        int result = userMapper.deleteById("4");
        System.out.println(result);
    }

    @Test
    void addUser() {
        UserEntity user = new UserEntity();
        user.setUsername("wuhao");
//        user.setPassword("123");
        int result = userMapper.addUser(user);
        System.out.println(result);
    }


    @Test
    void addUserGetId() {
        UserEntity user = new UserEntity();
        user.setUsername("zhangsan");
//        user.setPassword("123");
        int result = userMapper.addUserGetId(user);
        System.out.println("result " + result);
        System.out.println("id: " + user.getId());
    }

    @Test
    void getListByName() {
        List<UserEntity> list = userMapper.getListByName("zhangsan");
        list.stream().forEach(System.out::println);
    }

    @Test
    void addUser2() {
        UserEntity user = new UserEntity();
        user.setUsername("wangwu");
        user.setPwd("123");
        user.setImg("img.png");
        int result = userMapper.addUser2(user);
        System.out.println(result);
    }

    @Transactional
    @Test
    void addUser3() {
        UserEntity user = new UserEntity();
        user.setUsername("wnagwu");
        user.setPwd("123");
        user.setImg("cat.png");
        int result = userMapper.addUser3(user);
        System.out.println(result);
    }
}