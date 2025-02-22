package com.example.chatroom.service.chatAi;

import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.example.chatroom.entity.enums.AiChatType;

/**
 * @Description 请求处理策略
 * @Author wh
 * @Date 2025/2/22 16:18
 */
public interface RequestHandlingStrategy {


   /**
    * 处理请求
    * @param question 问题
    * @return 处理后的结果
    */
   String handleRequest(String question);


   /**
    * 获取实现类针对的类型
    * @return 具体类型
    */
   AiChatType getAiChatType();


}

