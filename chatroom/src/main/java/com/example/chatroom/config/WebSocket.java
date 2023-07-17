package com.example.chatroom.config;

import com.example.chatroom.common.ApplicationVariable;
import com.example.chatroom.component.OnlineUserManager;
import com.example.chatroom.entity.Friend;
import com.example.chatroom.entity.Message;
import com.example.chatroom.entity.User;
import com.example.chatroom.entity.dto.AddFriendRequestDTO;
import com.example.chatroom.entity.dto.MessageRequestDTO;
import com.example.chatroom.entity.vo.AddFriendResponseVO;
import com.example.chatroom.entity.vo.MessageResponseVO;
import com.example.chatroom.service.FriendService;
import com.example.chatroom.service.MessageService;
import com.example.chatroom.service.MessageSessionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;

/**
 * 配置 WebSocket
 * 服务器存储了所有用户的WebSocketSession对象，需要给那个用户发送数据，就操作和那个用户之间的WebSocketSession对象
 * 构造userId与WebSocketSession对象之间的映射关系，就可以根据sessionId查找userId进而查到对应的WebSocketSession，实现服务器转发消息功能
 */
@Component
public class WebSocket extends TextWebSocketHandler {

    @Autowired
    private OnlineUserManager onlineUserManager;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MessageSessionService messageSessionService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private FriendService friendService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // websocket建立成功后自动调用
        System.out.println("连接成功");
        // 通过拦截器得到HttpSession中的value
        User user = (User) session.getAttributes().get(ApplicationVariable.SESSION_KEY_USERINFO);
        if(user == null) {
            return;
        }
        // 用户上线
        onlineUserManager.online(user.getUserId(), session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 收到消息之后，自动调用
        System.out.println("[接收消息] " + message.toString());
        User user = (User) session.getAttributes().get(ApplicationVariable.SESSION_KEY_USERINFO);
        if(user == null) {
            System.out.println("当前用户未登录，无法进行消息转发！");
            return;
        }
        MessageRequestDTO messageRequest = null;
        AddFriendRequestDTO addFriendRequest = null;
        // 判断websocket类型，根据不同类型进行不同类型的消息转发工作
        if(message.getPayload().toString().contains("message")) {
            System.out.println(message.getPayload());
            // 客户端发送的消息转换为java对象
             messageRequest = objectMapper.readValue(message.getPayload(), MessageRequestDTO.class);
            // 进行聊天消息转发
            transferMessage(user, messageRequest);
        }else if(message.getPayload().toString().contains("addFriend")){
            addFriendRequest = objectMapper.readValue(message.getPayload(), AddFriendRequestDTO.class);
            // 进行好友申请消息转发
            transferMessageAddFriend(user, addFriendRequest);
        }else {
            System.out.println("message type 有误！");
        }

    }

    /**
     * 好友申请消息转发
     * @param user
     * @param addFriendRequest
     */
    private void transferMessageAddFriend(User user, AddFriendRequestDTO addFriendRequest) throws IOException {
        // 1. 构造响应对象
        AddFriendResponseVO addFriendResponse = new AddFriendResponseVO();
        addFriendResponse.setInput(addFriendRequest.getInput());
        // 根据userId查找数据库
        String username = friendService.selectFriendNameByUserId(user.getUserId());
        addFriendResponse.setUsername(username);
        addFriendResponse.setUserId(user.getUserId());


        String resp = objectMapper.writeValueAsString(addFriendResponse);
        // 2. 转发消息（用户在线就直接接收消息），（不在线，存储数据库，用户登录就会加载数据库，获得好友申请信息）
        WebSocketSession webSocketSession = onlineUserManager.getSession(addFriendRequest.getUserId());
        // 用户可能未在线，需要判断一下
        if(webSocketSession != null) {
            webSocketSession.sendMessage(new TextMessage(resp));
        }
        System.out.println("[添加好友消息转发] " + addFriendResponse.toString());

        // 3. 存储数据库
        friendService.addAddFriend(user.getUserId(), addFriendRequest.getUserId(), addFriendRequest.getInput());

    }

    /**
     * 进行聊天消息转发工作
     * @param fromUser 发送消息的用户
     * @param messageRequest 客户端发送的消息
     */
    private void transferMessage(User fromUser, MessageRequestDTO messageRequest) throws IOException {
        // 构造响应对象
        MessageResponseVO messageResponse = new MessageResponseVO();
        messageResponse.setType("message");
        messageResponse.setFromId(fromUser.getUserId());
        messageResponse.setFromName(fromUser.getUsername());
        messageResponse.setSessionId(messageRequest.getSessionId());
        messageResponse.setContent(messageRequest.getContent());
        // 将响应对象转换为json字符串
        String messageRespJson = objectMapper.writeValueAsString(messageResponse);
        System.out.println("[消息转发] " + messageRespJson);

        // 查找数据库，得到当前会话中的所有用户
        // 需要给自己也转发一份，因此这里也需要查询到自己
        List<Friend> friends = messageSessionService.getFriendsBySessionId(messageRequest.getSessionId(), -1);

        // 向每个用户转发消息
        for(Friend friend : friends) {
            WebSocketSession webSocketSession = onlineUserManager.getSession(friend.getFriendId());
            if(webSocketSession == null) {
                // 如果用户未在线，则不发送，这里将消息存储了数据库，用户登录可以看见历史消息
                continue;
            }
            webSocketSession.sendMessage(new TextMessage(messageRespJson));
        }
        // 存储消息到数据库，用户登录可以看见历史消息
        Message message = new Message();
        message.setFromId(fromUser.getUserId());
        message.setSessionId(messageRequest.getSessionId());
        message.setContent(messageRequest.getContent());
        messageService.addMessage(message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // 连接出现异常自动调用
        System.out.println("连接出现异常" + exception.toString());
        User user = (User) session.getAttributes().get(ApplicationVariable.SESSION_KEY_USERINFO);
        if(user == null) {
            return;
        }
        // 用户下线操作
        onlineUserManager.offline(user.getUserId(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 连接正常关闭之后正常调用
        System.out.println("连接正常关闭" + status.toString());
        User user = (User) session.getAttributes().get(ApplicationVariable.SESSION_KEY_USERINFO);
        if(user == null) {
            return;
        }
        // 用户下线操作
        onlineUserManager.offline(user.getUserId(), session);
    }
}
