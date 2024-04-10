package com.itheima.mp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.mp.domain.po.Users;
import com.itheima.mp.mapper.UsersMapper;
import com.itheima.mp.service.IUsersService;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author wh
 * @Date 2024/4/2 9:48
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService{
}
