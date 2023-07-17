package com.example.chatroom.entity;

import lombok.Data;

import java.util.List;

/**
 * 用于前端消息列表
 */
@Data
public class MessageSession {
    private int sessionId;
    private List<Friend> friends;
    private String lastMessage;
}
