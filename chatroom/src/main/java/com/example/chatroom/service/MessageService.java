package com.example.chatroom.service;

import com.example.chatroom.entity.Message;
import com.example.chatroom.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;
    public String getLastMessageBySessionId(Integer sessionId) {
        return messageMapper.getLastMessageBySessionId(sessionId);
    }

    public List<Message> getMessagesBySessionId(Integer sessionId) {
        return messageMapper.getMessagesBySessionId(sessionId);
    }

    public int addMessage(Message message) {
        return messageMapper.addMessage(message);
    }
}
