package com.example.demo.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 统一数据返回
 */
@ControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice {

    // json转换
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if(body instanceof AjaxResult) {
            return body;
        }
        // springMVC转换字符串会出错，这里特殊处理
        if(body instanceof String) {
            return objectMapper.writeValueAsString(AjaxResult.success(body));
        }
        if(body instanceof Integer && (Integer)body <= 0) {
            return AjaxResult.fail((Integer) body, "参数错误");
        }
        return AjaxResult.success(body);
    }
}
