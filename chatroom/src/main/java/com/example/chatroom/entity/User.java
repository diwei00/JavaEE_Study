package com.example.chatroom.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 存储用户信息
 */
@Data
public class User implements Serializable {
    private int userId;
    private String username = "";
    private String password = "";
    private String email = "";
}
