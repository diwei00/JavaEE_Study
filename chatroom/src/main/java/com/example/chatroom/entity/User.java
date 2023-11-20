package com.example.chatroom.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 存储用户信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable{
    private int userId;
    private String username = "";
    private String password = "";
    private String email = "";
    private String img;
}
