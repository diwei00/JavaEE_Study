package com.example.chatroom.controller;

import com.example.chatroom.common.ApplicationVariable;
import com.example.chatroom.common.CheckCodeTools;
import com.example.chatroom.common.UnifyResult;
import com.example.chatroom.entity.User;
import com.example.chatroom.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;

    // 邮件发送
    @Autowired
    private JavaMailSenderImpl mailSender;

    // 验证码工具类
    @Autowired
    private CheckCodeTools codeTools;

    // 用于加盐加密，由于关闭了框架的加载，这里需要new对象
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private StringRedisTemplate redisTemplate;

    // 验证码key，前缀
    private static final String codeKeyPrefix = "chatroom:code:";
    // 验证码key，后缀，全局唯一
    private int count = 0;

    // 文件实际保存路径
    @Value("${userImgpath}")
    private String userImgPath;


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
    public UnifyResult register(User user, String code) {
        // 非空校验
        if(!StringUtils.hasLength(user.getUsername()) || !StringUtils.hasLength(user.getPassword())
            || !StringUtils.hasLength(user.getEmail())) {
            return UnifyResult.fail(-1, "参数有误！");
        }

        // 进行验证码校验（从redis中取验证码）
        String codeDB = redisTemplate.opsForValue().get(codeKeyPrefix + user.getUsername());
        if(codeDB == null || !codeDB.equals(code)) {
            return UnifyResult.fail(-2, "验证码错误！");
        }

        try {
            // 密码进行加盐加密存储
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            // 设置用户默认头像
            user.setImg("defaultUserImg.png");
            int result = userService.insert(user);
        } catch (DuplicateKeyException e) {
            // 注册失败返回空对象
            User userFail = new User();
            return UnifyResult.fail(-3, "用户名重复！", userFail);

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
    @PostMapping("/email")
    public UnifyResult getCode(String email, String username) {
        if (!StringUtils.hasLength(email)) {
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
        message.setText("您的验证码是：" + code.toString() + "。有效期一分钟哦！");
        // 设置发送者邮箱
        message.setFrom("2945608334@qq.com");
        // 判定用户名在这一时刻是否有其他用户注册
        if(redisTemplate.opsForValue().get(codeKeyPrefix + username) != null) {
            return UnifyResult.fail(-2, "此刻有其他用户注册此用户名！");
        }
        message.setTo(email);

        mailSender.send(message);

        // 将验证码存储在redis中，点击注册后，后端进行校验，过期时间60s
        redisTemplate.opsForValue().set(codeKeyPrefix + username, String.valueOf(code), Duration.ofSeconds(60));

        return UnifyResult.success(1);
    }



    /**
     * 修改密码
     * @param newPassword
     * @param username
     * @return
     */
    @PostMapping ("/changePassword")
    public UnifyResult changePassword(String newPassword, String username, String code) {
        // 非空校验
        if(!StringUtils.hasLength(newPassword) && !StringUtils.hasLength(username) || !StringUtils.hasLength(code)) {
            return UnifyResult.fail(-1, "参数有误！");
        }
        // redis中读取验证码
        String codeDB = redisTemplate.opsForValue().get(codeKeyPrefix + username);
        if(codeDB == null || !codeDB.equals(code)) {
            return UnifyResult.fail(-3, "验证码错误！");
        }
        // 校验是否存在这个用户
        User user = userService.selectByName(username);
        if(user == null) {
            return UnifyResult.fail(-2, "不存在当前用户！");
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

    /**
     * 上传头像
     * @param multipartFile
     * @param request
     * @return
     */
    @PostMapping("/uploadUserImg")
    public UnifyResult uploadUserImg(MultipartFile multipartFile, HttpServletRequest request) {
        // 参数校验
        if(multipartFile == null) {
            return UnifyResult.fail(-1, "上传失败，缺少文件参数！");
        }
        // 文件类型校验
        if(!multipartFile.getContentType().split("/")[1].equals("png") &&
                !multipartFile.getContentType().split("/")[1].equals("jpg") &&
                !multipartFile.getContentType().split("/")[1].equals("jpeg")) {
            return UnifyResult.fail(-1, "上传失败，文件格式错误！");
        }
        // 文件大小校验
        if(multipartFile.getSize() > 20 * 1024 * 1024) {
            return UnifyResult.fail(-1, "上传失败，文件大于20MB");
        }
        File file = new File(userImgPath);
        if(!file.exists()) {
            // 创建此文件夹
            file.mkdir();
        }
        // 构造文件名
        String[] suffix = multipartFile.getOriginalFilename().split("\\.");
        String fileSuffix = suffix[suffix.length - 1];
        String filename = UUID.randomUUID().toString() + "." + fileSuffix;

        // 存储文件
        FileInputStream fileInputStream = null;
        BufferedOutputStream outputStream = null;
        try {
            fileInputStream = (FileInputStream) multipartFile.getInputStream();
            outputStream = new BufferedOutputStream(Files.newOutputStream(Paths.get(file.getPath(), filename)));
            outputStream.write(multipartFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                assert fileInputStream != null;
                fileInputStream.close();
                assert outputStream != null;
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 将文件名存储在数据库中，后续请求根据url映射，找到用户头像
        HttpSession session =  request.getSession(false);
        User user = (User) session.getAttribute(ApplicationVariable.SESSION_KEY_USERINFO);
        if(user == null) {
            return UnifyResult.fail(-1, "操作失败，用户未登录！");
        }
        int result = userService.saveUserImg(filename, user.getUserId());
        if(result < 1) {
            return UnifyResult.fail(-1, "存储用户头像失败！");
        }
        // 由于前端拿到的用户数据是从后端session中获取的，这里用户信息改变需要同步到session中
        user.setImg(filename);
        session.setAttribute(ApplicationVariable.SESSION_KEY_USERINFO, user);


        return UnifyResult.success(filename);
    }

}
