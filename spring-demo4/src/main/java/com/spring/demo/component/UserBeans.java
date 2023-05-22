package com.spring.demo.component;

import com.spring.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

//@Component
public class UserBeans {

    @Bean
    // 在添加Bean的时候就需要设置Bean的作用域
    // @Scope("prototype") 常量
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    // 多例模式，当使用getBean() / @Autowired 都是新的对象实例
    public User user() {
        User user = new User();
        user.setName("张三");
        return user;
    }
}
