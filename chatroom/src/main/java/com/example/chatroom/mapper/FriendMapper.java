package com.example.chatroom.mapper;

import com.example.chatroom.entity.Friend;
import com.example.chatroom.entity.User;
import com.example.chatroom.entity.dto.AddFriendRequestDTO;
import com.example.chatroom.entity.vo.AddFriendResponseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FriendMapper {
    List<Friend> selectFriendList(@Param("userId") Integer userId);

    List<Friend> searchFriendList(@Param("username") String username);

    String selectFriendNameByUserId(@Param("userId") Integer userId);

    // 添加好友申请
    Integer addAddFriend(@Param("fromId") Integer fromId, @Param("userId") Integer userId, @Param("input") String input);

    List<AddFriendResponseVO> getAddFriendList(@Param("userId") Integer userId);

    int deleteAddFriend(@Param("fromId") Integer fromId, @Param("userId") Integer userId);

    int addFriend(@Param("friendId") Integer fromId, @Param("userId") Integer userId);

    User selectFriendByUserId(int userId);
}
