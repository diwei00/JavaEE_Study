package com.example.demo.common;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// 自定义拦截器，实现的AOP思想
@Component
public class LoginInterceptor implements HandlerInterceptor {
    // 调用目标方法之前执行的方法
    // 此方法返回 boolean 类型的值：
    //      如果返回的是 true 表示(拦截器)验证成功，继续走后续的流程，执行目标方法。
    //      如果返回 false，这表示拦截器执行失败，验证未通过，后续的流程和目标方法不要执行了。
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 用户登录判断业务
        HttpSession session = request.getSession(false);
        if(session != null && session.getAttribute("session_userinfo") != null) {
            // 用户以登陆
            return true;
        }
        // 返回状态码，表示用户没有访问权限，需要身份验证
//        response.setStatus(401);
        response.setContentType("application/json; charset=utf8");
        response.getWriter().write("{\"code\":-1,\"msg\":\"登录失败\",\"data\":\"\"}");
        return false;
    }
}
