package com.example.chatroom.service;

import com.example.chatroom.entity.Friend;
import com.example.chatroom.entity.MessageSession;

import com.example.chatroom.entity.MessageSessionUser;
import com.example.chatroom.entity.dto.MessageSessionDTO;
import com.example.chatroom.mapper.MessageSessionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageSessionService {

    @Autowired
    private MessageSessionMapper messageSessionMapper;

    public List<Integer> getSessionIdsByUserId(Integer userId) {
        return messageSessionMapper.getSessionIdsByUserId(userId);
    }

    public List<Friend> getFriendsBySessionId(Integer sessionId, Integer selfUserId) {
        return messageSessionMapper.getFriendsBySessionId(sessionId, selfUserId);
    }

    // 整合Mapper，添加会话并且添加会话中的用户及好友，返回sessionId
    public int addMessageSession(MessageSession messageSession) {
        // 添加会话，并且得到sessionId
        return messageSessionMapper.addMessageSession(messageSession);


    }
    // 添加会话中的UserId
    public void addMessageSessionUser(MessageSessionDTO item) {
        messageSessionMapper.addMessageSessionUser(item);
    }


    public List<Integer> selectAllSessionId() {
        return messageSessionMapper.selectAllSessionId();
    }

    public Integer selectMaxId() {
        return messageSessionMapper.selectMaxId();
    }


    public List<MessageSessionUser> selectRangeById(int startId, int endId) {
        return messageSessionMapper.selectRangeById(startId, endId);
    }

    public List<Integer> selectUsersBySessionId(Integer sessionId) {
        return messageSessionMapper.selectUsersBySessionId(sessionId);
    }
}
