package com.example.chatroom.controller;

import com.example.chatroom.common.UnifyResult;
import com.example.chatroom.entity.Message;
import com.example.chatroom.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 获取历史消息
     * @param sessionId
     * @return
     */
    @GetMapping("/getMessage")
    public UnifyResult getMessages(Integer sessionId) {
        // 参数校验
        if(sessionId == null || sessionId <= 0) {
            return UnifyResult.fail(-1, "参数有误！");
        }
        List<Message> messages = messageService.getMessagesBySessionId(sessionId);

        // 数据库对消息进行了降序，而前端展示消息需要升序
        Collections.reverse(messages);

        return UnifyResult.success(messages);
    }

}
