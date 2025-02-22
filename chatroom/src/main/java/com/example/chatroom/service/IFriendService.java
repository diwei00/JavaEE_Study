package com.example.chatroom.service;

import com.example.chatroom.entity.Friend;
import com.example.chatroom.entity.User;
import com.example.chatroom.entity.vo.AddFriendResponseVO;

import java.util.List;

/**
 * @Description
 * @Author wh
 * @Date 2025/2/22 15:31
 */
public interface IFriendService {

    List<Friend> selectFriendList(int userId);


    List<Friend> searchFriendList(String username);


    String selectFriendNameByUserId(int userId);

    User selectFriendByUserId(int userId);

    Integer addAddFriend(Integer fromId, Integer userId, String input);

    List<AddFriendResponseVO> getAddFriendList(Integer userId);

    Integer deleteAddFriend(Integer fromId, Integer userId);

    Integer agreeAddFriend(Integer fromId, Integer userId);
}
