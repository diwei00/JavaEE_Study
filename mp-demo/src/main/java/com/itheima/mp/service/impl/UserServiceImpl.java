package com.itheima.mp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.mp.domain.dto.UserDTO;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.service.IUserService;
import com.itheima.mp.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

// 继承ServiceImpl，此类实现了IService接口中的方法
// 指定Service对应的mapper（实质还是调用mapper）和操作的实体类（映射表）
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>implements IUserService {

    @Override
    public Boolean deductBalance(Long id, Integer money) {
        // 查询用户
        User user = this.getById(id);
        if(user == null) {
            throw new RuntimeException("该用户不存在！");
        }
        // 判断用户状态
        if(user.getStatus() == 2) {
            throw new RuntimeException("该用户状态异常！");
        }
        // 判断余额是否充足
        if(user.getBalance() < money) {
            throw new RuntimeException("用户余额不足！");
        }
        // 扣减余额
//        this.baseMapper.deductBalanceById(id, money);

        int remainBalance = user.getBalance() - money;
        Boolean result = this.lambdaUpdate()
                .set(User::getBalance, remainBalance)
                .set(remainBalance == 0, User::getStatus, 2) // 动态拼接set部分
                .eq(User::getId, id)
                .eq(User::getBalance, user.getBalance()) // 并发问题，设置乐观锁
                .update();
        return result;
    }

    @Override
    public List<UserVO> getUserList(UserDTO userDTO) {
        // lambdaQuery实现复杂条件查询（动态sql）
        List<User> userList = this.lambdaQuery()
                .like(userDTO.getName() != null, User::getUsername, userDTO.getName())
                .eq(userDTO.getStatus() != null, User::getStatus, userDTO.getStatus())
                .ge(userDTO.getMinBalance() != null, User::getBalance, userDTO.getMinBalance())
                .le(userDTO.getMaxBalance() != null, User::getBalance, userDTO.getMaxBalance())
                .list();

        // one() 查询一个结果
        // list() 查询多个结果
        // count() 数量统计

        // 处理结果集
        return BeanUtil.copyToList(userList, UserVO.class);
    }
}
