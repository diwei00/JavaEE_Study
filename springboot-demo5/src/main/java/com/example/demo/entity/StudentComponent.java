package com.example.demo.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// yml注入对象
// 需要一个类，属性包含yml中对象属性，提供set方法，让spring操作（当然需要托管到spring中）
@ConfigurationProperties("student")
@Component
@Setter
@Getter
@ToString
public class StudentComponent {
    private int id;
    private String name;
    private int age;
}
