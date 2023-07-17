package com.example.chatroom.controller;

import com.example.chatroom.common.ApplicationVariable;
import com.example.chatroom.common.UnifyResult;
import com.example.chatroom.entity.Friend;
import com.example.chatroom.entity.MessageSession;
import com.example.chatroom.entity.User;
import com.example.chatroom.entity.dto.MessageSessionDTO;
import com.example.chatroom.service.MessageService;
import com.example.chatroom.service.MessageSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 会话管理模块
 * 1）得到会话
 * 2）创建会话
 */
@RestController
@RequestMapping("/session")
public class MessageSessionController {

    @Autowired
    private MessageSessionService messageSessionService;

    @Autowired
    private MessageService messageService;


    /**
     * 获取会话列表
     * @param request
     * @return
     */
    @GetMapping("/sessionList")
    public UnifyResult getMessageSessionList(HttpServletRequest request) {
        // 由于拦截器这个接口用户一定会登录，这里就不需要判空了
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(ApplicationVariable.SESSION_KEY_USERINFO);
        if(user == null) {
            return UnifyResult.fail(-1, "用户未登录！");
        }
        // 用户前端返回数据list
        List<MessageSession> messageSessionList = new ArrayList<>();

        // 根据userId得到当前用户所有sessionId
        List<Integer> sessionIdList = messageSessionService.getSessionIdsByUserId(user.getUserId());
        // 遍历sessionId，得到当前会话好友信息
        for(Integer sessionId : sessionIdList) {
            MessageSession messageSession = new MessageSession();
            List<Friend> friends = messageSessionService.getFriendsBySessionId(sessionId, user.getUserId());
            messageSession.setSessionId(sessionId);
            messageSession.setFriends(friends);

            // 根据sessionId得到每条会话最后一条消息
            String lastMessage = messageService.getLastMessageBySessionId(sessionId);
            if(lastMessage == null) {
                lastMessage = "";
            }
            messageSession.setLastMessage(lastMessage);
            messageSessionList.add(messageSession);
        }
        return UnifyResult.success(messageSessionList);

    }

    /**
     * 添加会话
     * 操作需要是原子的
     * @param toUserId 聊天好友
     * @param user 用户
     * @return 返回 sessionId
     */
    @Transactional
    @PostMapping("/createSession")
    public UnifyResult addMessageSessionAndUser(Integer toUserId, @SessionAttribute(ApplicationVariable.SESSION_KEY_USERINFO) User user) {
        HashMap<String, Integer> result = new HashMap<>();
        // 用于存储SessionId
        MessageSession messageSession = new MessageSession();
        messageSessionService.addMessageSession(messageSession);

        // 存储会话中的用户
        MessageSessionDTO item1 = new MessageSessionDTO();
        item1.setSessionId(messageSession.getSessionId());
        item1.setUserId(user.getUserId());
        messageSessionService.addMessageSessionUser(item1);

        MessageSessionDTO item2 = new MessageSessionDTO();
        item2.setSessionId(messageSession.getSessionId());
        item2.setUserId(toUserId);
        messageSessionService.addMessageSessionUser(item2);

        result.put("sessionId", messageSession.getSessionId());
        return UnifyResult.success(result);

    }

}
