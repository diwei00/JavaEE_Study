package com.example.demo.common;

import com.example.demo.entity.Userinfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class UserSessionTools {

    public static Userinfo getLoginUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object tmp = null;
        // 为了只查询一次value，使用临时变量
        if(session != null && (tmp = session.getAttribute(ApplicationVariable.SESSION_KEY_USERINFO)) != null) {
            return (Userinfo) tmp;
        }
        return null;
    }
}
