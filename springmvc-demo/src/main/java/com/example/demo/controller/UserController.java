package com.example.demo.controller;


import com.example.demo.entity.UserInfo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.spi.SyncResolver;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class UserController {

    // 注册路由，和http请求中的url对应（同时也就把浏览器和代码连接起来了）
    // 支持多种类型的请求（Get，Post，Put...）
    // method 可设置具体支持的请求类型
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        return "Hello SpringMVC";
    }

    // 只支持Get请求
    @GetMapping("/hello2")
    public String hello2() {
        return "Hello SpringMVC2";

    }

    // 只支持Post请求
    @PostMapping("/hello3")
    public String hello3() {
        return "Hello SpringMVC3";
    }

    // 获取前端传递的单个参数
    // 根据key-value形式进行参数匹配，所以后端需要和前端传递的key值相等
    // 如果前端不传递参数，那么结果就是null（包装类型）
    @RequestMapping("/hello4")
    public String hello4(String name) {
        return "name: " + name;
    }

    @RequestMapping("/hello6")
    public String hello6(Integer name) {
        return "name: " + name;
    }

    // 基础数据类型接收，前端不传递参数，服务器直接抛异常（所以一般使用包装类型进行参数接收）
    @RequestMapping("/hello5")
    public String hello5(int id) {
        return "name: " + id;
    }

    // 传递多个参数，根据对应的key进行注入，和顺序没有关系
    @RequestMapping("/hello7")
    public String hello7(String name, Integer id) {
        return "name: " + name + " id: " + id;
    }

    // SpringMVC是使用Servlet实现的，支持Servlet的原始写法
    @RequestMapping("/hello8")
    public String hello8(HttpServletRequest request, HttpServletResponse response) {
        return request.getParameter("name");
    }

    // SpringMVC返回数据时会检查数据的格式，就会设置响应的格式了
    @RequestMapping("/hello9")
    public Object hello9() {
        return "<h1>我是一级标题</h1>";
    }

    // 以对象作为接收前端数据，前端只需要传递对象中的属性，SpringMVC会自动进行对象的初始化（参数映射）
    @RequestMapping("/hello10")
    public Object hello10(UserInfo userInfo) {
        System.out.println(userInfo);
        return userInfo;
    }

    // 前端传递参数 key = username 会赋值到当前name参数中
    // 默认 required = true 意味着前端必须传递username的key，否则就会报400请求错误。
    // 可以手动置为 required = false 意味着前端就可以不传递这个参数
    @RequestMapping("/hello11")
    public Object hello11(@RequestParam(value = "username", required = false) String name, String password) {
        return name + ": " + password;
    }

    // 接收from表单发送的数据，from表单会将数据构造到QueryString中
    @RequestMapping("/hello12")
    public String hello12(String username, String password) {
        return "<h1>" + "username: " + username + " password: " + password + "</h1>";
    }

    // 添加注解@RequestBody 接收json对象
    // spring MVC会自动解析json对象
    @RequestMapping("/hello13")
    public Object hello13(@RequestBody UserInfo userInfo) {
        System.out.println(userInfo);
        return userInfo;
    }

    // 从基础URL中获取参数（不是query string中） 例如：http://localhost:8080/hello14/wuhao/123
    // @RequestMapping的设置会解析这样的URL，后面花括号中就是key
    // 优点1：搜索引擎抓取关键字权重更高
    // 优点2：url更简洁
    @RequestMapping("/hello14/{name}/{pwd}")
    public Object hello14(@PathVariable String name, @PathVariable(name = "pwd", required = false) String password) {
        return "name: " + name + " password: " + password;
    }

    // 上传文件
    // 路径如果保持不变，上传文件就会覆盖
    // 使用UUID生成唯一文件名 + 上传文件后缀
    @RequestMapping("/upload")
    public Object upload(@RequestPart("myimg") MultipartFile file) {
        // 文件名 + 文件后缀
        String fileName = UUID.randomUUID() + file.getOriginalFilename().substring(
                file.getOriginalFilename().lastIndexOf('.'));

        File saveFile = new File("E:\\tmp\\" + fileName);
        try {
            file.transferTo(saveFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 获取Cookie
    @RequestMapping("/cookie")
    public Object getCookie(@CookieValue(value = "java", required = false) String java) {
        return java;
    }

    // 获取header
    @RequestMapping("/header")
    public Object getHeader(@RequestHeader("Cookie") String header) {
        return header;
    }

    // 存储Session
    @RequestMapping("/setsession")
    public void setSession(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        session.setAttribute("name", "123456");
    }

    // 获取Session
    @RequestMapping("/getsession")
    public Object getSession(@SessionAttribute("name") String name) {
        return name;
    }




}
