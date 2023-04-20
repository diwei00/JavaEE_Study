package com.spring.demo;

// 只要这个对象多处都需要使用，那么这个对象就可以作为bean存储到spring中
public class Student {

    public Student() {
        System.out.println("构造student对象");
    }
    public void hello() {
        System.out.println("hello spring");
    }
}
