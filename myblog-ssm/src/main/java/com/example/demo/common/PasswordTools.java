package com.example.demo.common;

import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class PasswordTools {


    /**
     * 加盐加密
     *
     * @param password 明文密码
     * @return 数据库存的数据
     */
    public static String encrypt(String password) {
        // 产生盐值
        String salt = UUID.randomUUID().toString().replace("-", "");
        // 明文密码 + 盐值 -> 密码
        String finalPassword = DigestUtils.md5DigestAsHex((salt + password).getBytes(StandardCharsets.UTF_8));
        // 密码 + 盐值（存入数据库，存盐值就是为了登录验证密码）
        String dbPassword = salt + "$" + finalPassword;
        return dbPassword;
    }

    /**
     * 验证加盐加密密码
     * @param password 明文密码（用于验证）
     * @param dbPassword 数据库中存储密码（盐值 + $ + MD5（盐值+密码））
     * @return
     */
    public static boolean decrypt(String password, String dbPassword) {
        boolean result = false;
        // 非空校验
        if(StringUtils.hasLength(password) && StringUtils.hasLength(dbPassword) &&
                dbPassword.length() == 65 && dbPassword.contains("$")) {
            // 参数正确
            // 分解数据库中的密码，得到盐值
            String[] passwordArr = dbPassword.split("\\$");
            String salt = passwordArr[0];
            String finalPassword = passwordArr[1];
            // 使用 盐值 + 登录明文密码 进行MD5 与 数据库中密码进行校验
            String checkPassword = DigestUtils.md5DigestAsHex((salt + password).getBytes(StandardCharsets.UTF_8));
            if(checkPassword.equals(finalPassword)) {
                result = true;
            }
        }
        return result;

    }
}
