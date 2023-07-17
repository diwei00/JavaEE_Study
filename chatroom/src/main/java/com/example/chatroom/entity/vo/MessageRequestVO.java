package com.example.chatroom.entity.vo;


import lombok.Data;

/**
 * 用于客户端发起消息请求
 */
@Data
public class MessageRequestVO {

    private String type = "message";
    private int sessionId;
    private String content;
}
