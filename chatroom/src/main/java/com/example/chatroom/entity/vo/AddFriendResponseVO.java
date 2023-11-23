package com.example.chatroom.entity.vo;

import lombok.Data;

/**
 * 用于好友请求转发对象
 */

@Data
public class AddFriendResponseVO {
    // 用于区分websocket消息转发类型
    private String type = "addFriend";
    private int userId;
    private String username;
    private String input;
    private String img;
}
