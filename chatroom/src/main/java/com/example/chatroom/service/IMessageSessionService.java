package com.example.chatroom.service;

import com.example.chatroom.entity.Friend;
import com.example.chatroom.entity.MessageSession;
import com.example.chatroom.entity.MessageSessionUser;
import com.example.chatroom.entity.dto.MessageSessionDTO;

import java.util.List;

/**
 * @Description
 * @Author wh
 * @Date 2025/2/22 15:33
 */
public interface IMessageSessionService {

    List<Integer> getSessionIdsByUserId(Integer userId);

    List<Friend> getFriendsBySessionId(Integer sessionId, Integer selfUserId);


    int addMessageSession(MessageSession messageSession);

    void addMessageSessionUser(MessageSessionDTO item);

    List<Integer> selectAllSessionId();

    Integer selectMaxId();

    List<MessageSessionUser> selectRangeById(int startId, int endId);

    List<Integer> selectUsersBySessionId(Integer sessionId);
}


