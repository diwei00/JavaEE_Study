package com.example.demo.config;

import com.example.demo.common.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 自定义拦截器加入系统配置项中
//@Configuration
public class MyConfig implements WebMvcConfigurer {

    // 自定义拦截器
    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 自定义拦截器加入系统配置项中，配置相应拦截规则
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**") // 拦截所有url
                .excludePathPatterns("/user/login") // 排除的url
                .excludePathPatterns("/user/reg")
                .excludePathPatterns("/image/**");

    }
}
