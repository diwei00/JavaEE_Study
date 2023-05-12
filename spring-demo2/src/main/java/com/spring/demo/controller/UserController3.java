package com.spring.demo.controller;

import com.spring.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller
public class UserController3 {
    // 重命名（根据 name = "user1" type = "User"）查找具体bean
//    @Resource(name = "user1")
    @Autowired
    // 可以搭配Qualifier来设置查找的name
    @Qualifier(value = "user1")
    private User user;

    public void hello() {
        System.out.println(user.getUsername());
    }

}
