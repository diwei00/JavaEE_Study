package com.example.chatroom.entity.dto;

import lombok.Data;

/**
 * 用户 MessageSession 和 user关联表
 * 后端内部传输使用
 */

@Data
public class MessageSessionDTO {

    private int SessionId;
    private int UserId;

}
