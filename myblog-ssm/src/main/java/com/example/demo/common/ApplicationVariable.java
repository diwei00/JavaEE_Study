package com.example.demo.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ApplicationVariable {

    /**
     * 存放当前用户的 session key
     */
    public static final String SESSION_KEY_USERINFO = "SESSION_KEY_USERINFO";

    /**
     * 摘要截取长度
     */
    public static final int MAX_LENGTH = 150;

    /**
     * 验证码 session key
     */
    public static final String SESSION_KEY_CHECK_CODE = "SESSION_KEY_CHECK_CODE";

    /**
     * 全局线程池
     */
    public static final ExecutorService executorService = Executors.newFixedThreadPool(100);

}
