package com.example.chatroom.component;

import com.example.chatroom.service.chatAi.RequestHandlingStrategy;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description Ai聊天工厂
 * @Author wh
 * @Date 2025/2/22 16:38
 */
@Component
public class AiChatFactory implements ApplicationContextAware {

    private static final Map<Integer, RequestHandlingStrategy> AI_CHAT_FACTORY_MAP = new ConcurrentHashMap<>();

    public static RequestHandlingStrategy getBean(Integer aiChatType) {
        return AI_CHAT_FACTORY_MAP.get(aiChatType);
    }


    /**
     * 初始化工厂
     * @param applicationContext 上下文
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        Map<String, RequestHandlingStrategy> beansOfType = applicationContext.getBeansOfType(RequestHandlingStrategy.class);
        beansOfType.forEach((key, value) -> {
            AI_CHAT_FACTORY_MAP.put(value.getAiChatType().getCode(), value);
        });

    }
}
