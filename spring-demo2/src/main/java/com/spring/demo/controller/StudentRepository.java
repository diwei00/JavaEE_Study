package com.spring.demo.controller;


import org.springframework.stereotype.Repository;

@Repository
public class StudentRepository {
    public void hello() {
        System.out.println("Hello StudentRepository");
    }
}
