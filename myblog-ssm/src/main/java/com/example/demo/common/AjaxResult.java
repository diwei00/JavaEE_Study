package com.example.demo.common;

import lombok.Data;



/**
 * 统一返回对象
 */
@Data
public class AjaxResult {
    // 状态码，状态码描述，数据
    private int code;
    private String msg;
    private Object data;

    /**
     * 成功数据返回
     *
     * @param data
     * @return
     */
    public static AjaxResult success(Object data) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setCode(200);
        ajaxResult.setMsg("");
        ajaxResult.setData(data);
        return ajaxResult;
    }

    public static AjaxResult success(String msg, Object data) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setCode(200);
        ajaxResult.setMsg(msg);
        ajaxResult.setData(data);
        return ajaxResult;

    }

    /**
     * 失败数据返回
     *
     * @param code
     * @param msg
     * @return
     */
    public static AjaxResult fail(Integer code, String msg) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setCode(code);
        ajaxResult.setMsg(msg);
        ajaxResult.setData("");
        return ajaxResult;
    }

    public static AjaxResult fail(Integer code, String msg, Object data) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setCode(code);
        ajaxResult.setMsg(msg);
        ajaxResult.setData(data);
        return ajaxResult;
    }


}
