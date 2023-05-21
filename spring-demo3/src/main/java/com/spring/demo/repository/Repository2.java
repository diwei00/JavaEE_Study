package com.spring.demo.repository;

import com.spring.demo.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public class Repository2 {

    public void hello() {
        User user = new User();
        user.setName("张三");
        System.out.println(user.getName());

    }
}
