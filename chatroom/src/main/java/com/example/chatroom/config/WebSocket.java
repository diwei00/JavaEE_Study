package com.example.chatroom.config;

import com.example.chatroom.common.ApplicationVariable;
import com.example.chatroom.component.OnlineUserManager;
import com.example.chatroom.entity.Friend;
import com.example.chatroom.entity.Message;
import com.example.chatroom.entity.User;
import com.example.chatroom.entity.dto.AddFriendRequestDTO;
import com.example.chatroom.entity.dto.AgreeAddFriendRequestDTO;
import com.example.chatroom.entity.dto.MessageRequestDTO;
import com.example.chatroom.entity.vo.AddFriendResponseVO;
import com.example.chatroom.entity.vo.AgreeAddFriendResponseVO;
import com.example.chatroom.entity.vo.MessageResponseVO;
import com.example.chatroom.service.FriendService;
import com.example.chatroom.service.MessageService;
import com.example.chatroom.service.MessageSessionService;
import com.example.chatroom.util.RedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 配置 WebSocket
 * 服务器存储了所有用户的WebSocketSession对象，需要给那个用户发送数据，就操作和那个用户之间的WebSocketSession对象
 * 构造userId与WebSocketSession对象之间的映射关系，就可以根据sessionId查找userId进而查到对应的WebSocketSession，实现服务器转发消息功能
 */
@Component
@Slf4j
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

    @Autowired
    private RedisUtil redisUtil;

    // 创建线程池实例
    private final ThreadPoolExecutor executorServiceToDB;

    public WebSocket() {
        // 初始化线程池
        executorServiceToDB = new ThreadPoolExecutor(
                5, // 核心线程数
                10, // 最大线程数
                60L, // 线程空闲时间
                TimeUnit.SECONDS, // 时间单位
                new ArrayBlockingQueue<>(100) // 任务队列
        );
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // websocket建立成功后自动调用
        log.info("连接成功");
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
        log.info("[接收消息] " + message.toString());
        User user = (User) session.getAttributes().get(ApplicationVariable.SESSION_KEY_USERINFO);
        if(user == null) {
            log.info("当前用户未登录，无法进行消息转发！");
            return;
        }
        MessageRequestDTO messageRequest = null;
        AddFriendRequestDTO addFriendRequest = null;
        AgreeAddFriendRequestDTO agreeAddFriendRequest = null;
        // 判断websocket类型，根据不同类型进行不同类型的消息转发工作
        if(message.getPayload().toString().contains("message")) {
            log.info(message.getPayload());
            // 客户端发送的消息转换为java对象
             messageRequest = objectMapper.readValue(message.getPayload(), MessageRequestDTO.class);
            // 进行聊天消息转发
            transferMessage(user, messageRequest);
        }else if(message.getPayload().toString().contains("addFriend")){
            addFriendRequest = objectMapper.readValue(message.getPayload(), AddFriendRequestDTO.class);
            // 进行好友申请消息转发
            transferMessageAddFriend(user, addFriendRequest);
        }else if(message.getPayload().toString().contains("friend")){
            agreeAddFriendRequest = objectMapper.readValue(message.getPayload(), AgreeAddFriendRequestDTO.class);
            // 进行同意好友消息转发
            transferMessageFriend(user, agreeAddFriendRequest);
        }else {
            log.info("message type 有误！");
        }

    }

    /**
     * 同意好友消息转发
     * @param user 处理用户
     * @param agreeAddFriendRequest 被处理用户
     */
    private void transferMessageFriend(User user, AgreeAddFriendRequestDTO agreeAddFriendRequest) throws IOException {
        // 1. 删除好友申请数据库关于这个申请记录
        friendService.deleteAddFriend(agreeAddFriendRequest.getUserId(), user.getUserId());
        // 2. 转发给双方各自好友信息（用户在线）
        // 向当前处理好友请求用户通知
        // 构造响应对象
        User agreeUser = friendService.selectFriendByUserId(agreeAddFriendRequest.getUserId());
        AgreeAddFriendResponseVO agreeAddFriendResponse = new AgreeAddFriendResponseVO();
        agreeAddFriendResponse.setType("friend");
        agreeAddFriendResponse.setFriendId(agreeAddFriendRequest.getUserId());
        agreeAddFriendResponse.setFriendName(agreeUser.getUsername());
        agreeAddFriendResponse.setImg(agreeUser.getImg());
        String resp1 = objectMapper.writeValueAsString(agreeAddFriendResponse);
        WebSocketSession webSocketSession1 = onlineUserManager.getSession(user.getUserId());
        webSocketSession1.sendMessage(new TextMessage(resp1));
        log.info("[同意好友消息转发] " + resp1);

        // 向当前被处理用户通知
        AgreeAddFriendResponseVO agreeAddFriendResponse2 = new AgreeAddFriendResponseVO();
        agreeAddFriendResponse2.setType("friend");
        agreeAddFriendResponse2.setFriendName(user.getUsername());
        agreeAddFriendResponse2.setFriendId(user.getUserId());
        agreeAddFriendResponse2.setImg(user.getImg());
        String resp2 = objectMapper.writeValueAsString(agreeAddFriendResponse2);
        WebSocketSession webSocketSession2 = onlineUserManager.getSession(agreeAddFriendRequest.getUserId());
        webSocketSession2.sendMessage(new TextMessage(resp2));
        // 3. 保存用户之间好友信息
        friendService.agreeAddFriend(user.getUserId(), agreeAddFriendRequest.getUserId());
        log.info("[同意好友消息转发] " + resp2);
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
        addFriendResponse.setImg(user.getImg());

        String resp = objectMapper.writeValueAsString(addFriendResponse);
        // 2. 转发消息（用户在线就直接接收消息），（不在线，存储数据库，用户登录就会加载数据库，获得好友申请信息）
        WebSocketSession webSocketSession = onlineUserManager.getSession(addFriendRequest.getUserId());
        // 用户可能未在线，需要判断一下
        if(webSocketSession != null) {
            webSocketSession.sendMessage(new TextMessage(resp));
        }
        log.info("[添加好友消息转发] " + addFriendResponse.toString());

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
        log.info("[消息转发] " + messageRespJson);

        // 查找数据库，得到当前会话中的所有用户
        // 需要给自己也转发一份，因此这里也需要查询到自己
        // 首先查缓存，如果缓存为空则查数据库,并将数据写入缓存中
        List<Integer> friendIds = redisUtil.range(messageRequest.getSessionId(), 0, redisUtil.size(messageRequest.getSessionId()));
        log.info("[消息转发] 查询到缓存会话sessionId:{}, userIds:{}" , messageRequest.getSessionId(), friendIds.toString());
        if(CollectionUtils.isEmpty(friendIds)) {
            List<Friend> friends = messageSessionService.getFriendsBySessionId(messageRequest.getSessionId(), -1);
            if(!CollectionUtils.isEmpty(friends)) {
                friendIds = friends.stream().map(Friend::getFriendId).collect(Collectors.toList());
                log.info("[消息转发] 查询到数据库会话 sessionId:{}, userIds:{}" , messageRequest.getSessionId(), friendIds.toString());
            }
            // 缓存数据不存在，则将数据存储到缓存中
            Integer[] friendIdArr = new Integer[friendIds.size()];
            friendIds.toArray(friendIdArr);
            log.info("[消息转发] 缓存数据不存在，将数据存储到缓存中 sessionId:{}, userIds:{}", messageRequest.getSessionId(), friendIds.toString());
            redisUtil.rightPushAll(messageRequest.getSessionId(), friendIdArr);
        }
        if(CollectionUtils.isEmpty(friendIds)) {
            log.info("[消息转发] 获取好友列表为空, sessionId{}", messageRequest.getSessionId());
            throw new RuntimeException("获取好友列表为空，sessionId: " + messageRequest.getSessionId());
        }

        // 向每个用户转发消息
        for(Integer userId : friendIds) {
            WebSocketSession webSocketSession = onlineUserManager.getSession(userId);
            if(webSocketSession == null) {
                // 如果用户未在线，则不发送，这里将消息存储了数据库，用户登录可以看见历史消息
                continue;
            }
            webSocketSession.sendMessage(new TextMessage(messageRespJson));
        }
        // 存储消息到数据库，用户登录可以看见历史消息
        // 多线程存储消息到数据库
        executorServiceToDB.submit(() -> {
            Message message = new Message();
            message.setFromId(fromUser.getUserId());
            message.setSessionId(messageRequest.getSessionId());
            message.setContent(messageRequest.getContent());
            messageService.addMessage(message);
        });
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // 连接出现异常自动调用
        log.error("连接出现异常" + exception.toString());
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
        log.info("连接正常关闭" + status.toString());
        User user = (User) session.getAttributes().get(ApplicationVariable.SESSION_KEY_USERINFO);
        if(user == null) {
            return;
        }
        // 用户下线操作
        onlineUserManager.offline(user.getUserId(), session);
    }
}
