package com.example.chatroom.service;

import com.example.chatroom.entity.Friend;
import com.example.chatroom.entity.User;
import com.example.chatroom.mapper.FriendMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendService {
    @Autowired
    private FriendMapper friendMapper;

    public List<Friend> selectFriendList(int userId) {
        return friendMapper.selectFriendList(userId);
    }
}
