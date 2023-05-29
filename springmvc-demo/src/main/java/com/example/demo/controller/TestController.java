package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/test")
public class TestController {

    // 返回静态页面数据
    @RequestMapping("/index")
    public Object getIndex() {
        // /代表从根目录下查找这个静态文件
        return "/index.html";
    }

    // 返回非静态页面数据
    @RequestMapping("/index2")
    @ResponseBody  // 声明这个方法返回非静态页面数据
    public String getString() {
        return "wuhao";
    }

    // 请求转发（服务器处理转发）（客户端只发送一次请求）
    @RequestMapping("forward")
    public String forward() {
        return "forward:/index.html";
    }

    // 请求重定向（服务器返回url，客户端需要发送两次请求）
    @RequestMapping("redirect")
    public String redirect() {
        return "redirect:/index.html";
    }

    // 原始servlet办法
    @RequestMapping("redirect2")
    public void redirect2(HttpServletResponse response) throws IOException {
        response.sendRedirect("https://www.baidu.com");
    }
}
