package com.example.chatroom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


// 关闭 spring Security 认证（项目启动时不加载框架bean到spring中）
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ChatroomApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatroomApplication.class, args);
    }

}
