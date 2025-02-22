package com.example.chatroom.common.exception;


import com.example.chatroom.common.UnifyResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理
 * 保证服务器抛异常前端不会直接500，异常会被这里接收所封装
 */
@RestControllerAdvice
public class MyExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public UnifyResult doExpection(Exception e) {
        return UnifyResult.fail(-1, e.getMessage());
    }
}
