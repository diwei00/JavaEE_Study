package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
     * 映射外部图片路径
     * 配置指定的静态文件资源路径
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/image/**").addResourceLocations("file:" + imagePath);
    }

    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 配置拦截规则
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login.html")
                .excludePathPatterns("/reg.html")
                .excludePathPatterns("/blog_list.html")
                .excludePathPatterns("/blog_content.html")
                .excludePathPatterns("/forgot_password.html")
                .excludePathPatterns("/favicon.ico")
                .excludePathPatterns("/css/**")
                .excludePathPatterns("/editor.md/**")
                .excludePathPatterns("/img/**")
                .excludePathPatterns("/image/**")
                .excludePathPatterns("/js/**")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/reg")
                .excludePathPatterns("/user/islogin")
                .excludePathPatterns("/user/email")
                .excludePathPatterns("/user/getcode")
                .excludePathPatterns("/user/get_user")
                .excludePathPatterns("/user/update_password")
                .excludePathPatterns("/art/getdetail")
                .excludePathPatterns("/art/increasercount")
                .excludePathPatterns("/art/getlistbypage")
                .excludePathPatterns("/art/getcount")
                .excludePathPatterns("/comment/add_comment")
                .excludePathPatterns("/comment/get_comment")
                .excludePathPatterns("/art/getAllCatalogue");

    }
}
