package cn.itcast.eureka;

// 创建springboot项目
// 子项目是maven项目，但父工程是springboot项目，管理了子项目库的一些配置，因此子项目也可认为是springboot项目

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer // 配置eureka组件注入到项目中
public class EurekaApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class, args);
    }
}
