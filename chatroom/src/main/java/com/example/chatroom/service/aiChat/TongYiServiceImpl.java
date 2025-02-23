package com.example.chatroom.service.aiChat;

import cn.hutool.core.util.StrUtil;
import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationOutput;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.aigc.generation.models.QwenParam;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.Constants;
import com.example.chatroom.common.exception.ServiceException;
import com.example.chatroom.entity.enums.AiChatType;
import com.example.chatroom.entity.enums.ResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * @Description
 * @Author wh
 * @Date 2025/2/22 16:56
 */

@Slf4j
@Service
public class TongYiServiceImpl implements RequestHandlingStrategy{


    @Value("${tongYi.api.key}")
    private String API_KEY;

    /**
     * 最大重试次数
     */
    private static final int MAX_RETRIES = 3;

    @Override
    public String handleRequest(String question, Integer retryCount) {
        if (retryCount >= MAX_RETRIES) {
            log.info("请求TongYi失败！已达最大重试次数，retryCount:{}", retryCount);
            throw new ServiceException(ResponseEnum.ERROR.getMsg());
        }
        try {
            Constants.apiKey = API_KEY;
            Generation gen = new Generation();
            // 构建请求体
            Message systemMsg = Message.builder()
                    .role(Role.SYSTEM.getValue())
                    .content("You are a helpful assistant.")
                    .build();
            Message userMsg = Message.builder()
                    .role(Role.USER.getValue())
                    .content(question)
                    .build();
            List<Message> messages = List.of(systemMsg, userMsg);
            // 构建请求参数
            QwenParam param = QwenParam.builder()
                    .model(Generation.Models.QWEN_TURBO)
                    .messages(messages)
                    .resultFormat(QwenParam.ResultFormat.MESSAGE)
                    .topP(0.8)
                    .enableSearch(true)
                    .build();
            // 发送请求
            GenerationResult result = gen.call(param);
            List<GenerationOutput.Choice> choices = result.getOutput().getChoices();
            if(CollectionUtils.isEmpty(choices)) {
                log.info("请求TongYi失败！选择项为空，进行重试... 当前重试次数:{}", retryCount + 1);
                retryRequest(question, retryCount + 1);
            }
            GenerationOutput.Choice choice = choices.get(0);
            if (Objects.isNull(choice.getMessage()) || StrUtil.isBlank(choice.getMessage().getContent())) {
                log.info("TongYi响应中没有消息内容！");
                throw new ServiceException(ResponseEnum.ERROR.getMsg());
            }
            return choice.getMessage().getContent();
        }catch (NoApiKeyException e) {
            log.info("请求TongYi失败, 缺少ApiKey", e);
            throw new ServiceException(ResponseEnum.ERROR.getMsg());
        } catch (Exception e) {
            log.info("请求TongYi失败！", e);
            throw new ServiceException(ResponseEnum.ERROR.getMsg());
        }
    }

    private void retryRequest(String question, Integer retryCount) {
        handleRequest(question, retryCount);
    }


    @Override
    public AiChatType getAiChatType() {
        return AiChatType.TONG_YI;
    }
}
