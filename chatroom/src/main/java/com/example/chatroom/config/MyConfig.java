package com.example.chatroom.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


/**
 * 配置拦截器到项目中
 */
@Configuration
public class MyConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;


    // 读取配置文件验证码保存路径
    @Value("${imgpath}")
    private String imagePath;

    /**
     * 注册静态资源处理器
     *
     * 映射外部图片请求路径到实际图片存储路径
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/image/**").addResourceLocations("file:" + imagePath);
    }

    /**
     * 添加拦截规则
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login.html")
                .excludePathPatterns("/register.html")
                .excludePathPatterns("/forget_password.html")
                .excludePathPatterns("/css/**")
                .excludePathPatterns("/img/**")
                .excludePathPatterns("/image/**")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/reg")
                .excludePathPatterns("/user/email")
                .excludePathPatterns("/user/verifyEmail")
                .excludePathPatterns("/user/changePassword")
                .excludePathPatterns("/user/getcode")
                .excludePathPatterns("/test.html")
                .excludePathPatterns("/favicon.ico");
    }
}
