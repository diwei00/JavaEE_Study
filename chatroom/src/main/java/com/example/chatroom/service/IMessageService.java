package com.example.chatroom.service;

import com.example.chatroom.entity.Message;

import java.util.List;

/**
 * @Description
 * @Author wh
 * @Date 2025/2/22 15:33
 */
public interface IMessageService {

    String getLastMessageBySessionId(Integer sessionId);

    List<Message> getMessagesBySessionId(Integer sessionId);

    int addMessage(Message message);
}
