package com.example.demo.common;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

// 统一异常处理
// 增强版ControllerAdvice
// 这个类会监视项目中所有类，如果哪个类抛异常了，就会被这个类接收，
// 然后返回指定错误信息，200 OK
// 当有多个异常时，首先匹配当前异常类，然后逐级向父类匹配
@ControllerAdvice
@ResponseBody
public class MyExceptionAdvice {

    /**
     * 空指针异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(NullPointerException.class)
    public HashMap<String, Object> doNullPointerException(NullPointerException e) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("code", 300);
        result.put("msg", "空指针" + e.getMessage());
        result.put("date", null);
        return result;

    }

    /**
     * 默认异常处理（当具体异常不匹配时就会执行此方法）
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public HashMap<String, Object> doException(Exception e) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("code", "-1");
        result.put("msg", "Exception" + e.getMessage());
        result.put("date", null);
        return result;
    }



}
