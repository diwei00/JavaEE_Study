package com.example.chatroom.service;

import com.example.chatroom.controller.AiChatController;
import com.example.chatroom.entity.enums.AiChatType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

/**
 * @Description
 * @Author wh
 * @Date 2025/2/22 14:28
 */
@SpringBootTest
public class DeepSeekServiceTest {

    @Autowired
    private AiChatController aiChatController;


    @Test
    public void testGetResponse() throws IOException {
        String question = "你是谁";
        String aiChatAnswer = aiChatController.getAiChatAnswer(question, AiChatType.TONG_YI.getCode());

        System.out.println(aiChatAnswer);
    }


}
