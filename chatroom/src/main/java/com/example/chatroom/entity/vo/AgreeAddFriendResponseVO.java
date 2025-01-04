package com.example.chatroom.entity.vo;

import lombok.Data;

@Data
public class AgreeAddFriendResponseVO {
    private String type;
    private String friendName;
    private int friendId;
    private String img;
}
