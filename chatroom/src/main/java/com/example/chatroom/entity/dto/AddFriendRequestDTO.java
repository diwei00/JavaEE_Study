package com.example.chatroom.entity.dto;

import lombok.Data;

/**
 * 用于添加好友请求对象
 */
@Data
public class AddFriendRequestDTO {
    private String type;
    private int userId;
    private String input;
}
