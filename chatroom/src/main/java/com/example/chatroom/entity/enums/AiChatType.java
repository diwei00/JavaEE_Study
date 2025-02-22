package com.example.chatroom.entity.enums;

/**
 * @Description
 * @Author wh
 * @Date 2025/2/22 16:30
 */
public enum AiChatType {

    DEEP_SEEK("deepSeek", 100),

    CHAT_GPT("chatGpt", 200),

    TONG_YI("tongYi", 300);

    private String type;

    private Integer code;

    AiChatType(String type, Integer code) {
        this.type = type;
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public static AiChatType getAiChatType(String type) {
        for (AiChatType aiChatType : AiChatType.values()) {
            if (aiChatType.getType().equals(type)) {
                return aiChatType;
            }
        }
        return null;
    }
}
