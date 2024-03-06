package com.xuecheng.ucenter.service;

import com.xuecheng.ucenter.model.dto.AuthParamsDTO;
import com.xuecheng.ucenter.model.dto.XcUserExt;

/**
 * 所有认证类型都需要实现此接口，实现针对不同登录类型校验
 * 策略模式
 */
public interface AuthService {

    /**
     *  认证方法
     * @param authParamsDto 认证参数
     * @return com.xuecheng.ucenter.model.po.XcUser 用户信息
     */
    XcUserExt execute(AuthParamsDTO authParamsDto);
}
