package com.example.chatroom.config;

import com.example.chatroom.common.ApplicationVariable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * 登录校验拦截器
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断用户是否登录
        HttpSession session = request.getSession(false);
        if(session != null && session.getAttribute(ApplicationVariable.SESSION_KEY_USERINFO) != null) {
            // 用户以登陆
            return true;
        }
        // 用户未登录
        response.sendRedirect("/login.html");
        return false;
    }
}
