package com.example.demo.common;

import org.springframework.util.StringUtils;

/**
 * 字符串处理工具类
 */
public class StringTools {

    // 截取字符串
    public static String subString(String src, int length) {
        if(!StringUtils.hasLength(src) || length <= 0) {
            return src;
        }
        if(src.length() <= length) {
            return src;
        }
        return src.substring(0, length);
    }
}
