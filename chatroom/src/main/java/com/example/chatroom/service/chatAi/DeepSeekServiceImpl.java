package com.example.chatroom.service.chatAi;

import com.alibaba.fastjson.JSON;
import com.example.chatroom.common.exception.ServiceException;
import com.example.chatroom.entity.enums.AiChatType;
import com.example.chatroom.entity.enums.ResponseEnum;
import com.example.chatroom.entity.aiChat.DeepSeekRequest;
import com.example.chatroom.entity.aiChat.DeepSeekResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;

@Slf4j
@Service
public class DeepSeekServiceImpl implements RequestHandlingStrategy {

    @Value("${deepseek.api.key}")
    private String apiKey;

    @Value("${deepseek.api.url}")
    private String apiUrl;


    private final OkHttpClient client = new OkHttpClient();


    @Override
    public String handleRequest(String question){
        // 构建请求体
        DeepSeekRequest.Message message = DeepSeekRequest.Message.builder()
                .role("user")
                .content(question)
                .build();
        // 指定火山引擎模型
        DeepSeekRequest requestBody = DeepSeekRequest.builder()
                .model("deepseek-v3-241226")
                .messages(Collections.singletonList(message))
                .build();

        // 创建HTTP请求
        Request request = new Request.Builder()
                .url(apiUrl)
                .post(RequestBody.create(JSON.toJSONString(requestBody), MediaType.get("application/json")))
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        // 发送请求并处理响应
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                log.info("请求DeepSeek失败！ response:{}", JSON.toJSONString(response));
                throw new ServiceException(ResponseEnum.ERROR.getMsg());
            }
            String responseBody = response.body().string();
            DeepSeekResponse deepSeekResponse = JSON.parseObject(responseBody, DeepSeekResponse.class);
            if (CollectionUtils.isEmpty(deepSeekResponse.getChoices())) {
                log.info("DeepSeek响应中没有选择项！");
                throw new ServiceException(ResponseEnum.ERROR.getMsg());
            }
            // todo: 暂时支持一次问一个问题
            DeepSeekResponse.Choice choice = deepSeekResponse.getChoices().get(0);
            if (choice.getMessage() == null || choice.getMessage().getContent() == null) {
                log.info("DeepSeek响应中没有消息内容！");
                throw new ServiceException(ResponseEnum.ERROR.getMsg());
            }
            return choice.getMessage().getContent();
        }catch (Exception e) {
            log.info("请求DeepSeek失败！", e);
            throw new ServiceException(ResponseEnum.ERROR.getMsg());
        }
    }

    @Override
    public AiChatType getAiChatType() {
        return AiChatType.DEEP_SEEK;
    }
}
