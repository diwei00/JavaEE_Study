package com.example.chatroom.entity;

import lombok.Data;

/**
 * 存储用户信息
 */
@Data
public class User {
    private int userId;
    private String username = "";
    private String password = "";
    private String email = "";
}
