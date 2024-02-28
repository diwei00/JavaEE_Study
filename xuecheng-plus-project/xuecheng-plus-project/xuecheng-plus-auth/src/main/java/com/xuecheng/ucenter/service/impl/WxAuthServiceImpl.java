package com.xuecheng.ucenter.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.ucenter.mapper.XcUserMapper;
import com.xuecheng.ucenter.mapper.XcUserRoleMapper;
import com.xuecheng.ucenter.model.dto.AuthParamsDTO;
import com.xuecheng.ucenter.model.dto.XcUserExt;
import com.xuecheng.ucenter.model.po.XcUser;
import com.xuecheng.ucenter.model.po.XcUserRole;
import com.xuecheng.ucenter.service.AuthService;
import com.xuecheng.ucenter.service.WxAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * 微信扫码认证
 */
@Slf4j
@Service("wx_authservice")
public class WxAuthServiceImpl implements AuthService, WxAuthService {
    @Autowired
    private XcUserMapper xcUserMapper;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private XcUserRoleMapper xcUserRoleMapper;
    @Autowired
    private WxAuthService currentProxy;

    // 从微信频台申请
    @Value("${weixin.appid}")
    String appid;
    @Value("${weixin.secret}")
    String secret;

    @Override
    public XcUserExt execute(AuthParamsDTO authParamsDto) {
        //账号
        String username = authParamsDto.getUsername();

        XcUser user = xcUserMapper.selectOne(new LambdaQueryWrapper<XcUser>().eq(XcUser::getUsername, username));
        if (user == null) {
            //返回空表示用户不存在
            throw new RuntimeException("账号不存在");
        }
        XcUserExt xcUserExt = new XcUserExt();
        BeanUtils.copyProperties(user, xcUserExt);
        return xcUserExt;
    }

    @Override
    public XcUser wxAuth(String code) {
        //收到 code 调用微信接口申请 access_token
        Map<String, String> access_token_map = getAccess_token(code);
        if (access_token_map == null) {
            return null;
        }
        //获取用户信息
        System.out.println(access_token_map);
        String openid = access_token_map.get("openid");
        String access_token = access_token_map.get("access_token");
        //拿 access_token 查询用户信息
        Map<String, String> userinfo = getUserinfo(access_token, openid);

        // 添加用户到数据库
        // 非事务方法调用事务方法，使用代理对象调用该方法
        XcUser xcUser = currentProxy.addWxUser(userinfo);
        return xcUser;
    }

    // 获取Token
    private Map<String, String> getAccess_token(String code) {
        String wxUrl_template = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
        //请求微信地址
        String wxUrl = String.format(wxUrl_template, appid, secret, code);
        log.info("调用微信接口申请 access_token, url:{}", wxUrl);
        // 远程调用微信接口
        ResponseEntity<String> exchange = restTemplate.exchange(wxUrl, HttpMethod.POST, null, String.class);
        // 获取响应数据
        String result = exchange.getBody();
        log.info("调用微信接口申请 access_token: 返回值:{}", result);
        // 转为map
        Map<String, String> resultMap = JSON.parseObject(result, Map.class);
        return resultMap;
    }

    // 获取用户数据
    private Map<String, String> getUserinfo(String access_token, String openid) {
        String wxUrl_template = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";
        //请求微信地址
        String wxUrl = String.format(wxUrl_template, access_token, openid);
        log.info("调用微信接口申请 access_token, url:{}", wxUrl);
        // 调用微信接口获取用户数据
        ResponseEntity<String> exchange = restTemplate.exchange(wxUrl, HttpMethod.POST, null, String.class);
        //防止乱码进行转码
        String result = new String(exchange.getBody().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        log.info("调用微信接口申请 access_token: 返回值:{}", result);
        Map<String, String> resultMap = JSON.parseObject(result, Map.class);
        return resultMap;
    }

    // 保存用户数据
    @Transactional
    public XcUser addWxUser(Map userInfo_map) {
        // 用户在微信系统中唯一id
        String unionid = userInfo_map.get("unionid").toString();
        //根据 unionid 查询数据库
        XcUser xcUser = xcUserMapper.selectOne(new LambdaQueryWrapper<XcUser>().eq(XcUser::getWxUnionid, unionid));
        if (xcUser != null) {
            return xcUser;
        }
        String userId = UUID.randomUUID().toString();
        xcUser = new XcUser();
        xcUser.setId(userId);
        xcUser.setWxUnionid(unionid);
        //记录从微信得到的昵称
        xcUser.setNickname(userInfo_map.get("nickname").toString());
        xcUser.setUserpic(userInfo_map.get("headimgurl").toString());
        xcUser.setName(userInfo_map.get("nickname").toString());
        xcUser.setUsername(unionid);
        xcUser.setPassword(unionid);
        xcUser.setUtype("101001");//学生类型
        xcUser.setStatus("1");//用户状态
        xcUser.setCreateTime(LocalDateTime.now());
        xcUserMapper.insert(xcUser);

        // 用户对应角色
        XcUserRole xcUserRole = new XcUserRole();
        xcUserRole.setId(UUID.randomUUID().toString());
        xcUserRole.setUserId(userId);
        xcUserRole.setRoleId("17");//学生角色
        xcUserRoleMapper.insert(xcUserRole);
        return xcUser;
    }

}