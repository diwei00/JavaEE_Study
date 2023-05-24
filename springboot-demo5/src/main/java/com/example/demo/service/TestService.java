package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestService {

    @RequestMapping("service/hello")
    private String hello() {
        log.info("info");
        log.warn("warn --");
        log.error("error --");
        return "hello world";
    }

}
