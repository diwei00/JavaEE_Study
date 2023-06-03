package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/login")
    public String login() {
        String str = null;
        System.out.println(str.hashCode());
        return "login";
    }

    @RequestMapping("reg")
    public String reg() {
        return "reg";
    }

    @RequestMapping("index")
    public String index() {
        return "index";
    }

}
