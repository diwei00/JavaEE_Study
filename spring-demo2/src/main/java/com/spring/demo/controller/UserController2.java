package com.spring.demo.controller;

import com.spring.demo.service.UserService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller
public class UserController2 {
    // @Resource
    private UserService userService;

    // 出身不同：@Autowired 来⾃于 Spring，⽽ @Resource 来⾃于 JDK 的注解
    // 使⽤时设置的参数不同：相⽐于 @Autowired 来说，@Resource ⽀持更多的参数设置，例如name 设置，根据名称获取 Bean。
    // @Autowired 可⽤于 Setter 注⼊、构造函数注⼊和属性注⼊，⽽ @Resource 只能⽤于 Setter 注⼊和属性注⼊，不能⽤于构造函数注⼊。
    @Resource()
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    public void hello() {
        System.out.println("hello UserController2");
        userService.hello();
    }

}
