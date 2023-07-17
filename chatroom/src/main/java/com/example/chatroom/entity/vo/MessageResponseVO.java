package com.example.chatroom.entity.vo;


import lombok.Data;

/**
 * 用于服务器向客户端消息转发
 */
@Data
public class MessageResponseVO {
    private String type = "message";
    private int fromId;
    private String fromName;
    private int sessionId;
    private String content;
}
