package com.example.chatroom.controller;

import com.example.chatroom.common.ApplicationVariable;
import com.example.chatroom.common.UnifyResult;
import com.example.chatroom.entity.Friend;
import com.example.chatroom.entity.User;
import com.example.chatroom.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("friend")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @GetMapping("friendList")
    public UnifyResult selectFriendList(HttpServletRequest request) {
        // 由于拦截器，这个接口用户肯定会登录，为了确保系统安全性这里判断了一下
        HttpSession session = request.getSession(false);
        if(session == null) {
            return UnifyResult.fail(-1, "用户未登录！");
        }
        User user = (User) session.getAttribute(ApplicationVariable.SESSION_KEY_USERINFO);
        if(user == null) {
            return UnifyResult.fail(-1, "用户未登录");
        }
        // 返回好友列表
        List<Friend> friendList = friendService.selectFriendList(user.getUserId());
        return UnifyResult.success(friendList);

    }
}
