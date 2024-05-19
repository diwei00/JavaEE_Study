package com.example.poidemo.service;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description
 * @Author wh
 * @Date 2024/5/16 19:58
 */
public interface UserService {
    String getUserToWord(String id, HttpServletResponse response);

}
