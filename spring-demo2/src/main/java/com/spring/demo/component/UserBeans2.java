package com.spring.demo.component;

import com.spring.demo.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class UserBeans2 {

    @Bean
    public User getUserByName() {
        User user = new User();
        user.setUserId(2);
        user.setUsername("李四");
        user.setPassword("111");
        user.setAge(25);
        return user;
    }
}
