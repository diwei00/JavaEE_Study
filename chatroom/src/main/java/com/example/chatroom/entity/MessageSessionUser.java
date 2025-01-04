package com.example.chatroom.entity;

import lombok.Data;

/**
 * @Description
 * @Author wh
 * @Date 2025/1/4 17:17
 */

@Data
public class MessageSessionUser {

    /** 主键id */
    private Integer id;

    /** 会话id */
    private Integer sessionId;

    /** 用户id */
    private Integer userId;
}
