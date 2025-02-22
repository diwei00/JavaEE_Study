package com.example.chatroom.entity.enums;


/**
 * @Description
 * @Author wh
 * @Date 2025/2/22 15:50
 */
public enum ResponseEnum {

    SUCCESS(200, "成功"),

    ERROR(500, "服务器错误"),

    PARAM_ERROR(401, "参数错误"),

    USER_NOT_EXIST(402, "用户不存在"),

    USER_EXIST(403, "用户已存在"),

    USER_PASSWORD_ERROR(404, "用户名或密码错误"),

    USER_NOT_LOGIN(405, "用户未登录"),

    USER_NOT_FOUND(406, "用户未找到"),

    USER_NOT_FOUND_BY_USERNAME(407, "用户名未找到"),
    ;

    private int code;

    private String msg;

    ResponseEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
