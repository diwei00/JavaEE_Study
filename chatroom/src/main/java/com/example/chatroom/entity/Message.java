package com.example.chatroom.entity;


import lombok.Data;

/**
 * 用于前端消息模块
 */
@Data
public class Message {
    private int messageId;
    private int fromId;
    private String fromName;
    private int sessionId;
    private String content;
}