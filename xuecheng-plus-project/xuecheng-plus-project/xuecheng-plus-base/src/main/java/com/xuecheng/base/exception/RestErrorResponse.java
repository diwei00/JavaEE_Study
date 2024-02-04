package com.xuecheng.base.exception;

import java.io.Serializable;

/**
 * 统一错误数据返回
 */
public class RestErrorResponse implements Serializable {
    private String errMessage;

    public RestErrorResponse(String errMessage) {
        this.errMessage = errMessage;

    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }
}