package com.example.poidemo.entity;

import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author wh
 * @Date 2024/11/3 15:30
 */
@Data
public class UserVO {
   private List<User> userList;
   private String name;
}
