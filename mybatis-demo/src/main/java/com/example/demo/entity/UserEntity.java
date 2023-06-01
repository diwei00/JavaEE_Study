package com.example.demo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserEntity {
    private Integer id;
    private String username;
    private String pwd;
    private String img;
    private LocalDateTime createtime;
    private LocalDateTime updatetime;
    private Integer state;
}
