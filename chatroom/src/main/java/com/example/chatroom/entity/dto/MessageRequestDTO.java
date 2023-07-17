package com.example.chatroom.entity.dto;


import lombok.Data;

/**
 * 用于客户端发起消息请求
 */
@Data
public class MessageRequestDTO {

    private String type;
    private int sessionId;
    private String content;
}
