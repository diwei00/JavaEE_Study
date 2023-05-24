package com.example.demo.controller;

import com.example.demo.entity.StudentComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;

/**
 * 注意 spring boot只会遍历启动类包下所有的子包，带有注解的加载到 spring boot 中
 * 设计思想：约定大于配置，我们需要遵循 spring boot 的约定
 */

@Controller
@ResponseBody // 加在类上，表示当前类中的所有方法返回值都是非静态页面的数据
@RequestMapping("test") // 请求映射，把类或者方法和url关联起来
public class TestController {
    // 得到日志对象，每一个类对应一个日志对象
    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    // 读取配置项，注意写法，必须加 ${key}
    @Value("${str1}")
    private String str1;

    @Value("${str2}")
    private String str2;

    @Value("${str3}")
    private String str3;

    @Value("${number}")
    private int number;

    @Value("${server.port}")
    private int port;

    @Autowired
    private StudentComponent studentComponent;

    // 构造方法，spring加载时就会执行
    @PostConstruct
    public void doPostConstruct() {
//        System.out.println("-------------------------------------");
//        System.out.println(str1);
//        System.out.println(str2);
//        System.out.println(str3);
//        System.out.println(number);
//        System.out.println(port);
//        System.out.println(studentComponent);
//        System.out.println("-------------------------------------");

    }


    @RequestMapping("/hello") // = @WebServlet("/url")  localhost:8080/hello
    public String hello() {
        // 打印日志
        // 默认日志级别是info 只有大于等于info级别才会显示
        // 控制日志级别可以在不同环境打印不同级别的日志（不同的环境所关注的日志信息有所差异）
        log.trace("trace");
        log.debug("debug");
        log.info("info");
        log.warn("warn");
        log.error("error");

        return "hello world";
    }

}
