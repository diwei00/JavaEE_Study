package com.example.chatroom.component;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 实现用户在线管理
 */
@Component
@Slf4j
public class OnlineUserManager {

    // 这里可能涉及并发，多个客户端同时登录
    private ConcurrentHashMap<Integer, WebSocketSession> sessions = new ConcurrentHashMap<>();

    // 用户上线
    public void online(Integer userId, WebSocketSession session) {
        // 防止一个用户多个客户端同时上线
        if(sessions.get(userId) != null) {
            log.info("用户已经登录，登录失败！");
            return;
        }
        // 用户上线，插入键值对到hash表中
        sessions.put(userId, session);
        log.info("[userId = " + userId + "] 上线");
    }

    // 用户下线
    public void offline(Integer userId, WebSocketSession session) {
        // 这里判断是为了防止用户多开，防止其他客户端删除了已经上线的客户端上线键值对
        if(sessions.get(userId) == session) {
            sessions.remove(userId);
            log.info("[userId = " + userId + "] 下线");
        }
    }

    // 根据userI获取到WebSocketSession
    public WebSocketSession getSession(Integer userId) {
        return sessions.get(userId);
    }
}
