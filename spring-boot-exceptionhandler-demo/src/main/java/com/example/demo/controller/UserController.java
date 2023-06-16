package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/login")
    public Object login() {
        String str = null;
        str.hashCode();
        int a = 1 / 0;
        return "login";
    }

    @RequestMapping("/reg")
    public Object reg() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "");
        result.put("data", 10);
        return result;
    }

    @RequestMapping("/reg2")
    public Object reg2() {
        return 1000;
    }

    @RequestMapping("hello")
    public Object hello() {
        return "hello";
    }

}
