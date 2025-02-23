package com.example.chatroom.controller;

import com.example.chatroom.component.AiChatFactory;
import com.example.chatroom.service.aiChat.RequestHandlingStrategy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController("aiChat")
public class AiChatController {


    /**
     * 获取AI的回答
     * @param question 问题
     * @param aiChatType Ai类型
     * @return 回答
     */
    @PostMapping("/getAiChatAnswer/{aiChatType}")
    public String getAiChatAnswer(@RequestBody String question, @RequestParam Integer aiChatType) {
        RequestHandlingStrategy aiChatBean = AiChatFactory.getBean(aiChatType);
        return aiChatBean.handleRequest(question, 0);
    }
}