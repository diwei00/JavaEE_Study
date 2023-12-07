package com.example.demo.common;


import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理
 */
@RestControllerAdvice
public class MyExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public AjaxResult doException(Exception e) {
        return AjaxResult.fail(-1, e.getMessage());
    }
}
