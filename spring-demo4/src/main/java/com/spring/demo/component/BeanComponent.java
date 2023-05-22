package com.spring.demo.component;

import org.springframework.beans.factory.BeanNameAware;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class BeanComponent implements BeanNameAware {

    @Override
    public void setBeanName(String s) {
        System.out.println("执行了通知 -> " + s);
    }

    // xml初始化
    public void myInit() {
        System.out.println("xml方式初始化");
    }

    @PostConstruct
    public void doPostConstruct() {
        System.out.println("注解初始化");
    }

    public void hello() {
        System.out.println("hello");
    }

    @PreDestroy
    public void doPreDestroy() {
        System.out.println("执行了 doPreDestroy");
    }


}
