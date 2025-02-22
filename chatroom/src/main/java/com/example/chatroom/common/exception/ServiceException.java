package com.example.chatroom.common.exception;

/**
 * @Description
 * @Author wh
 * @Date 2025/2/22 15:57
 */
public class ServiceException extends RuntimeException {

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
