package com.example.demo.controller;

import com.example.demo.common.*;
import com.example.demo.entity.Userinfo;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Random;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    // 用于邮件发送
    @Autowired
    private JavaMailSenderImpl mailSender;

    @Autowired
    private CheckCodeTools codeTools;

    // 用于加盐加密，由于关闭了框架的加载，这里需要new对象
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 注册功能
     *
     * @param userInfo
     * @return
     */
    @RequestMapping("/reg")
    public AjaxResult reg(Userinfo userInfo) {
        // 非空判断
        if (userInfo == null || !StringUtils.hasLength(userInfo.getUsername()) ||
                !StringUtils.hasLength(userInfo.getPassword())) {
            return AjaxResult.fail(-1, "参数有误");
        }
        // 密码进行加盐加密 encode
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        // 调用UserService执行添加方法，并将返回的结果添加 AjaxResult.data 进行返回
        int result = userService.reg(userInfo);
        return AjaxResult.success(result);
    }

    /**
     * 登录功能
     *
     * @param username 用户名
     * @param password 请求对象
     * @return
     */
    @RequestMapping("/login")
    public AjaxResult login(String username, String password, String code, HttpServletRequest request) {
        // 非空判断
        if (!StringUtils.hasLength(username) || !StringUtils.hasLength(password)) {
            return AjaxResult.fail(-1, "参数有误");
        }
        Userinfo userinfo = userService.login(username);
        if (userinfo == null || userinfo.getId() <= 0) {
            return AjaxResult.fail(-2, "用户名或密码输入错误！");
        }
        // 使用加盐加密进行登录密码校验  matches加盐验证
        if (!passwordEncoder.matches(password, userinfo.getPassword())) {
            return AjaxResult.fail(-2, "用户名或密码输入错误！");
        }
        // 得到存储验证码session
        HttpSession sessionCode = request.getSession(false);
        String finalcode = (String) sessionCode.getAttribute(ApplicationVariable.SESSION_KEY_CHECK_CODE);
        if(!code.equals(finalcode)) {
            return AjaxResult.fail(-3, "验证码错误！");
        }

        // 用户登录成功，存储用户session
        HttpSession session = request.getSession(true);
        session.setAttribute(ApplicationVariable.SESSION_KEY_USERINFO, userinfo);

        // 前后端约定 data = 1 表示成功
        return AjaxResult.success(1);
    }

    /**
     * 注销功能
     * 移除session中的user
     */
    @RequestMapping("/logout")
    public AjaxResult logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        session.removeAttribute(ApplicationVariable.SESSION_KEY_USERINFO);
        return AjaxResult.success(1);
    }

    /**
     * 判断用户是否登录
     * 1 为登录  0 为未登录
     *
     * @param request
     * @return
     */
    @RequestMapping("/islogin")
    public AjaxResult isLogin(HttpServletRequest request) {
        Userinfo userinfo = UserSessionTools.getLoginUser(request);
        if (userinfo != null) {
            return AjaxResult.success(1);
        }
        return AjaxResult.success(0);
    }

    /**
     * 注册页面，用作邮箱验证
     *
     * @param emailCode 邮箱地址
     * @return
     */
    @RequestMapping("/email")
    public AjaxResult contextLoads(String emailCode) {
        if (emailCode == null) {
            return AjaxResult.fail(-1, "参数错误");
        }
        // 生成随机数，用作验证码
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(9));
        }
        // 封装简单的消息
        SimpleMailMessage message = new SimpleMailMessage();
        // 设置主题
        message.setSubject("博客系统验证码");
        // 设置内容
        message.setText(code.toString());
        // 设置发送者邮箱
        message.setFrom("2945608334@qq.com");
        message.setTo(emailCode);

        mailSender.send(message);
        return AjaxResult.success(code);

    }

    @RequestMapping("/check_password")
    public AjaxResult checkPassword(String oldPassword, String newPassword, HttpServletRequest request) {
        // 非空校验
        if (!StringUtils.hasLength(oldPassword) || !StringUtils.hasLength(newPassword)) {
            return AjaxResult.fail(-1, "参数错误");
        }
        Userinfo userinfo = UserSessionTools.getLoginUser(request);


        // 使用加盐的方式判断
        if (!passwordEncoder.matches(oldPassword, userinfo.getPassword())) {
            return AjaxResult.fail(0, "原密码错误");
        }
        // 存储加盐后的密码
        int result = userService.modifyPassword(userinfo.getId(), passwordEncoder.encode(newPassword));
        return AjaxResult.success(result);
    }

    /**
     * 生成session 保存图片验证码
     * @param request 验证码路径（需要映射）
     * @return
     */
    @RequestMapping("/getcode")
    public AjaxResult getCode(HttpServletRequest request) {
        // 存储验证码图片 图片名称 + 验证码
        String[] codeArr = codeTools.createImage();

        // 将验证码存储到session中
        HttpSession session = request.getSession(true);
        session.setAttribute(ApplicationVariable.SESSION_KEY_CHECK_CODE, codeArr[1]);

        return AjaxResult.success("/image/" + codeArr[0]);

    }

    /**
     * 找回密码
     * 验证用户名和邮箱地址
     * @param username 用户名
     * @return
     */

    @RequestMapping("/get_user")
    public AjaxResult getUserByUsername(String username) {
        // 非空校验
        if(!StringUtils.hasLength(username)) {
            return AjaxResult.fail(-1,"参数错误");
        }
        Userinfo userinfo = userService.getUserByUsername(username);
        return AjaxResult.success(userinfo);
    }

    /**
     * 找回密码
     * 修改数据库密码
     * @param userInfo
     * @return
     */
    @RequestMapping("/update_password")
    public AjaxResult updatePassword(Userinfo userInfo) {
        if(userInfo == null ||
                !StringUtils.hasLength(userInfo.getUsername()) ||
                !StringUtils.hasLength(userInfo.getPassword())) {
            return AjaxResult.fail(-1, "参数错误");
        }
        // 修改数据库中密码
        // 对用户密码加盐处理
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        int result = userService.updatePassword(userInfo);
        return AjaxResult.success(result);


    }
}
