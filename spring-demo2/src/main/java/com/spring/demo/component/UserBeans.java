package com.spring.demo.component;

import com.spring.demo.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
// 控制注入顺序（值越大，注入越迟），如果对象一样，名字也一样（覆盖式注入）
@Order(1)

public class UserBeans {

    // 方法注解必须搭配类注解使用，spring不会扫描所有类的方法，只扫描类注解下的方法（提升效率）
    // @Bean的命名规则，默认是方法名，也可以起多个名字（数组），这个时候默认的名字就无效了
    @Bean(name = {"user1", "user2"})
    public User getUserById() {
        User user = new User();
        user.setUserId(1);
        user.setUsername("张三");
        user.setPassword("111");
        user.setAge(25);
        return user;
    }

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
