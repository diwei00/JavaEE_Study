package com.example.chatroom.common;

import lombok.Data;

/**
 * 统一数据返回
 */
@Data
public class UnifyResult {
    private int code;
    private String msg;
    private Object data;

    /**
     * 成功数据返回
     * @param data
     * @return
     */
    public static UnifyResult success(Object data) {
        UnifyResult unifyResult = new UnifyResult();
        unifyResult.setCode(200);
        unifyResult.setMsg("");
        unifyResult.setData(data);
        return unifyResult;

    }
    public static UnifyResult success(String msg, Object data) {
        UnifyResult unifyResult = new UnifyResult();
        unifyResult.setCode(200);
        unifyResult.setMsg(msg);
        unifyResult.setData(data);
        return unifyResult;

    }

    /**
     * 失败数据返回
     * @param code
     * @param msg
     * @return
     */

    public static UnifyResult fail(int code, String msg) {
        UnifyResult unifyResult = new UnifyResult();
        unifyResult.setCode(code);
        unifyResult.setMsg(msg);
        unifyResult.setData("");
        return unifyResult;
    }
    public static UnifyResult fail(int code, String msg, Object data) {
        UnifyResult unifyResult = new UnifyResult();
        unifyResult.setCode(code);
        unifyResult.setMsg(msg);
        unifyResult.setData(data);
        return unifyResult;
    }

}
