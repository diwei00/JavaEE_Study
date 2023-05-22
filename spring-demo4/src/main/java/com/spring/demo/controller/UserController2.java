package com.spring.demo.controller;

import com.spring.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

//@Controller
public class UserController2 {

    @Autowired
    private User user;

    public void printUser2() {
        // Bean作用域 默认是单例模式 此Bean在整个spring容器中只有一份（一处修改所有的就都改了）
        System.out.println("user -> " + user.toString());

    }

}
