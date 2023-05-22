package com.spring.demo.controller;

import com.spring.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

//@Controller
public class UserController {
    @Autowired
    private User user;

    public void printUser() {
        System.out.println(user.toString());
        // 修改user
        User myUser = user;
        myUser.setName("王五");
        System.out.println("myUser -> " + myUser.toString());
        System.out.println("user -> " + user.toString());

    }


}
