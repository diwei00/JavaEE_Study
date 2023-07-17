package com.example.chatroom.mapper;

import com.example.chatroom.entity.Friend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FriendMapper {
    List<Friend> selectFriendList(@Param("userId") Integer userId);
}
