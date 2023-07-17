package com.example.chatroom.entity.vo;

import lombok.Data;

/**
 * 用于好友请求转发对象
 */

@Data
public class AddFriendResponseVO {
    private String type = "addFriend";
    private int userId;
    private String username;
    private String input;
}
