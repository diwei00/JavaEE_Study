package com.spring.demo.controller;


import com.spring.demo.service.Service2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class Controller2 {

    @Autowired
    private Service2 service2;

    public void hello() {
        service2.hello();
    }

}
