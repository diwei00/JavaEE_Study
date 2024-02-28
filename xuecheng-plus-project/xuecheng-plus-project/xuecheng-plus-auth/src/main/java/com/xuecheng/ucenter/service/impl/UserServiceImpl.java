package com.xuecheng.ucenter.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.ucenter.mapper.XcMenuMapper;
import com.xuecheng.ucenter.mapper.XcUserMapper;
import com.xuecheng.ucenter.model.dto.AuthParamsDTO;
import com.xuecheng.ucenter.model.dto.XcUserExt;
import com.xuecheng.ucenter.model.po.XcMenu;
import com.xuecheng.ucenter.model.po.XcUser;
import com.xuecheng.ucenter.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class UserServiceImpl implements UserDetailsService {
    @Autowired
    private XcUserMapper xcUserMapper;

    @Autowired
    private XcMenuMapper menuMapper;

    // spring容器
    @Autowired
    private ApplicationContext applicationContext;

    // 重新loadUserByUsername，查询数据库
    // s: username
    // 返回值: 作为jwt数据部分（可用于jwt用户信息扩充）, 并且存储在当前线程上下文中（接口获取用户登录信息）
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // 统一认证接口（支持多种方式登录）
        AuthParamsDTO authParamsDto = null;
        try {
            //将认证参数转为 AuthParamsDto 类型
            authParamsDto = JSON.parseObject(s, AuthParamsDTO.class);
        } catch (Exception e) {
            log.info("认证请求不符合项目要求:{}", s);
            throw new RuntimeException("认证请求数据格式不对");
        }

        //认证类型
        String authType = authParamsDto.getAuthType();
        // 指定名称获取bean
        AuthService authService = applicationContext.getBean(authType + "_authservice", AuthService.class);
        // 开始认证
        XcUserExt user = authService.execute(authParamsDto);
        // 返回UserDetails对象
        return getUserPrincipal(user);
    }

    /**
     * @param user 用户 id，主键
     * @return com.xuecheng.ucenter.model.po.XcUser 用户信息
     * @description 查询用户信息
     */
    public UserDetails getUserPrincipal(XcUserExt user) {
        //用户权限,如果不加报 Cannot pass a null GrantedAuthoritycollection
        String[] authorities = {};
        //查询用户权限
        List<XcMenu> xcMenus = menuMapper.selectPermissionByUserId(user.getId());
        List<String> permissions = new ArrayList<>();
        if (xcMenus.size() <= 0) {
            //用户权限,如果不加则报 Cannot pass a null GrantedAuthoritycollection
            // 默认权限
            permissions.add("p1");
        } else {
            xcMenus.forEach(menu -> {
                permissions.add(menu.getCode());
            });
        }
        authorities = permissions.toArray(new String[0]);

        String password = user.getPassword();
        //为了安全在令牌中不放密码
        user.setPassword(null);
        //将 user 对象转 json
        String userString = JSON.toJSONString(user);
        //创建 UserDetails 对象
        UserDetails userDetails = User.withUsername(userString).password(password).authorities(authorities).build();
        return userDetails;
    }
}
