package com.example.chatroom.mapper;

import com.example.chatroom.entity.Friend;
import com.example.chatroom.entity.MessageSession;
import com.example.chatroom.entity.dto.MessageSessionDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageSessionMapper {
    // 查找当前用户所有会话
    List<Integer> getSessionIdsByUserId(@Param("userId") Integer userId);

    // 查找当前会话用户信息（不需要当前用户信息）
    List<Friend> getFriendsBySessionId(@Param("sessionId") Integer sessionId, @Param("selfUserId") Integer selfUserId);

    // 创建会话，并且得到SessionId
    int addMessageSession(MessageSession messageSession);

    // 根据SessionId插入当前会话所设计的userId
    void addMessageSessionUser(MessageSessionDTO messageSessionItem);
}
