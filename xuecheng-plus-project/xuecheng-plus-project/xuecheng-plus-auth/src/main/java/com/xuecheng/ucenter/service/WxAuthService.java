package com.xuecheng.ucenter.service;

import com.xuecheng.ucenter.model.po.XcUser;

import java.util.Map;


public interface WxAuthService {
    // 微信认证接口
    XcUser wxAuth(String code);

    XcUser addWxUser(Map userInfo_map);
}