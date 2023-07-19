package com.example.chatroom.controller;

import com.example.chatroom.common.ApplicationVariable;
import com.example.chatroom.common.CheckCodeTools;
import com.example.chatroom.common.UnifyResult;
import com.example.chatroom.entity.User;
import com.example.chatroom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Random;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    // 邮件发送
    @Autowired
    private JavaMailSenderImpl mailSender;

    // 验证码工具类
    @Autowired
    private CheckCodeTools codeTools;

    // 用于加盐加密，由于关闭了框架的加载，这里需要new对象
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 登录功能
     * @param username 用户名
     * @param password 密码
     * @param request
     * @return
     */
    @PostMapping("/login")
    public UnifyResult login(String username, String password, String code, HttpServletRequest request) {
        // 非空校验
        if(!StringUtils.hasLength(username) || !StringUtils.hasLength(password)  || !StringUtils.hasLength(code)) {
            return UnifyResult.fail(-1, "参数有误！");
        }
        // 根据用户名查找数据库得到User对象
        User user = userService.selectByName(username);
        // 使用加盐加密进行密码校验
        if(user == null || !passwordEncoder.matches(password, user.getPassword())) {
            User userFail = new User();
            return UnifyResult.fail(-1, "用户名或密码错误！", userFail);
        }
        // 校验验证码，从session中获取
        HttpSession sessionCode =  request.getSession(false);
        String checkCode = (String) sessionCode.getAttribute(ApplicationVariable.SESSION_KEY_CHECK_CODE);
        if(!checkCode.equals(code)) {
            return UnifyResult.fail(-2, "验证码错误！");
        }

        // 登录成功，创建用户会话
        HttpSession session = request.getSession(true);
        session.setAttribute(ApplicationVariable.SESSION_KEY_USERINFO, user);
        user.setPassword("");
        user.setEmail("");
        // 返回 userId 和 username
        return UnifyResult.success(user);
    }

    /**
     * 注册功能
     * @param user 前端注册参数，用对象接收
     * @return
     */
    @PostMapping("/reg")
    public UnifyResult register(User user) {
        // 非空校验
        if(!StringUtils.hasLength(user.getUsername()) || !StringUtils.hasLength(user.getPassword())
            || !StringUtils.hasLength(user.getEmail())) {
            return UnifyResult.fail(-1, "参数有误！");
        }

        try {
            // 密码进行加盐加密存储
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            int result = userService.insert(user);
        } catch (DuplicateKeyException e) {
            // 注册失败返回空对象
            User userFail = new User();
            return UnifyResult.fail(-1, "注册失败，用户名重复！", userFail);

        }
        // 注册成功返回 userId 和 password
        user.setPassword("");
        user.setEmail("");
        return UnifyResult.success(user);

    }

    /**
     * 获取用户信息
     * @param request
     * @return
     */
    @GetMapping("/userInfo")
    public UnifyResult getUserInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session == null) {
            return UnifyResult.fail(-1, "用户未登录！");
        }

        User user = (User) session.getAttribute(ApplicationVariable.SESSION_KEY_USERINFO);
        if(user == null) {
            return UnifyResult.fail(-1, "用户未登录！");
        }
        // 登录成功返回用户信息
        user.setEmail("");
        user.setPassword("");
        return UnifyResult.success(user);
    }

    /**
     * 邮件发送
     * @param email
     * @return
     */
    @GetMapping("/email")
    public UnifyResult getCode(String email) {
        if (email == null) {
            return UnifyResult.fail(-1, "参数错误");
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
        message.setSubject("网页聊天室验证码");
        // 设置内容
        message.setText(code.toString());
        // 设置发送者邮箱
        message.setFrom("2945608334@qq.com");
        message.setTo(email);

        mailSender.send(message);
        return UnifyResult.success(code);
    }

    /**
     * 修改密码。邮箱验证
     * @param username
     * @return
     */
    @PostMapping("/verifyEmail")
    public UnifyResult verifyEmail(String username) {
        if(!StringUtils.hasLength(username)) {
            return UnifyResult.fail(-1, "参数有误！");
        }
        String emailDB = userService.getEmail(username);
        return UnifyResult.success(emailDB);
    }

    /**
     * 修改密码
     * @param newPassword
     * @param username
     * @return
     */
    @PostMapping ("/changePassword")
    public UnifyResult changePassword(String newPassword, String username) {
        // 非空校验
        if(!StringUtils.hasLength(newPassword) && !StringUtils.hasLength(username)) {
            return UnifyResult.fail(-1, "参数有误！");
        }
        int result = userService.changePassword(passwordEncoder.encode(newPassword), username);
        return UnifyResult.success(result);
    }



    /**
     * 生成session 保存图片验证码
     * @param request 验证码路径（需要映射）
     * @return
     */
    @RequestMapping("/getcode")
    public UnifyResult getCode(HttpServletRequest request) {
        // 存储验证码图片 图片名称 + 验证码
        String[] codeArr = codeTools.createImage();

        // 将验证码存储到session中
        HttpSession session = request.getSession(true);
        session.setAttribute(ApplicationVariable.SESSION_KEY_CHECK_CODE, codeArr[1]);

        // 响应图片名字到前端
        return UnifyResult.success("/image/" + codeArr[0]);

    }

}
