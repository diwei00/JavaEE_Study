package com.example.demo.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // @RestController == (@Controller + @ResponseBody)
@Slf4j  // 给当前类中添加一个叫做 log 的日志对象（= SLF4J 里面提供 Logger）
// lombok执行原理，编译前会把注解替换成对应的代码
public class LogController {


    @RequestMapping("log/hello")
    private String hello() {
        log.trace("trace");
        log.info("info");
        return "hello world";
    }


}
