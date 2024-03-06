package com.xuecheng.base.exception;

/**
 * 自定义异常
 */
public class ServiceException extends RuntimeException {
    private String errMessage;

    public ServiceException() {
        super();
    }

    public ServiceException(String errMessage) {
        super(errMessage);
        this.errMessage = errMessage;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public static void cast(CommonError commonError) {
        throw new ServiceException(commonError.getErrMessage());
    }

    public static void cast(String errMessage) {
        throw new ServiceException(errMessage);
    }
}