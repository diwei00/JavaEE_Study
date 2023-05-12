package com.spring.demo.controller;

import com.spring.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {

    // 属性注入（spring会自动的将spring对象注入到这里）
    // 根据类型和名字（spring中的对象Id），来确定唯一的一个对象
    // 优点：简单
    // 缺点：1. 无法实现final修饰的变量注入
    //      2. 兼容不好，只适用于IoC容器
    //      3. 风险：因为写法简单，所以违背单一设计原则概率更大（可以注入多个对象，使用多个属性）
//    @Autowired
//    private UserService userService;
//


    private final UserService userService;

    // Setter注入（使用set方法，spring会将对象传入到set方法中）（根据类名和对象名来确定）
    // 优点：符合单一设计原则（每个方法只能传递一个对象）
    // 缺点：1. 不能注入不可变对象（final修饰）
    //      2. 使用Setter注入的对象可能会被修改（可以调用set方法进行修改参数）
//    @Autowired
//    public void setUserService(UserService userService) {
//        this.userService = userService;
//    }

    // 构造方法注入（官方推荐的做法）
    // 如果只有一个构造方法，可以省略@Autowired，有多个构造方法就不能省略了
    // 优点：
    //      1. 可以注入不可变对象（final修饰的）
    //      2. 注入的对象不会改变，构造方法只执行一次
    //      3. 构造方法可以保证对象完全被初始化（类加载时就会执行）
    //      4. 通用性更好
    @Autowired
    public UserController(UserService service) {
        this.userService = service;
    }
    public UserController(UserService service, int age) {
        this.userService = service;
    }


    public void hello() {
        System.out.println("Hello UserController");
        userService.hello();
    }
}
