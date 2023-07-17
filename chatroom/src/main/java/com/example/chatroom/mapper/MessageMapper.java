package com.example.chatroom.mapper;

import com.example.chatroom.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper {

    String getLastMessageBySessionId(@Param("sessionId") int sessionId);

    List<Message> getMessagesBySessionId(@Param("sessionId") int sessionId);

    int addMessage(Message message);


}
