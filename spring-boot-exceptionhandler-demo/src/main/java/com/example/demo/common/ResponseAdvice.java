package com.example.demo.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.HashMap;

/**
 * 统一数据格式处理
 * 需要重写两个方法 supports beforeBodyWrite
 * 数据返回时这个类就在监视，如果数据不是统一格式，这个类就会对数据进行重写
 */
@ControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice {
    // 处理json字符串，spring boot内置了ObjectMapper对象
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 是否执行 beforeBodyWrite 方法，true=执行
     * @param returnType
     * @param converterType
     * @return
     */

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    /**
     * 对返回数据进行重写
     * @param body   原始返回值
     * @param returnType
     * @param selectedContentType
     * @param selectedConverterType
     * @param request
     * @param response
     * @return
     */
    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if(body instanceof HashMap) {
            return body;
        }

        HashMap<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", body);
        result.put("msg", "");
        // spring mvc有一个bug：返回值为String时，将Hash表转换为json字符串会使用StringHttpMessageConverter转换器转换，就会报错
        // 非String类型使用HttpMessageConverter转换器转换就不会存在问题
        // 解决方案：
        //       去除StringHttpMessageConverter转换器（配置文件中）
        //       在字符串处理时，单独处理字符串，手动将HashMap转换为json字符串
        if(body instanceof String) {
            return objectMapper.writeValueAsString(result);
        }
        return result;

    }
}
