package com.example.demo.common;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;


/**
 * 验证码工具类
 */
@Component
public class CheckCodeTools {
    // 读取配置文件中的图片保存路径
    @Value("${imgpath}")
    private String imagePath;

    /**
     * 生成验证码
     * @return 返回图片名称 + 验证码
     */
    public String[] createImage() {
        // 定义图片名称
        String imageName = UUID.randomUUID().toString() + ".png";
        // 定义图形验证码的长和宽
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(110, 40);
        // 图形验证码写出，可以写出到文件，也可以写出到流
        lineCaptcha.write(imagePath + imageName);
        // 得到验证码
        String code = lineCaptcha.getCode();

        String[] result = new String[]{imageName, code};
        return result;

    }




}
