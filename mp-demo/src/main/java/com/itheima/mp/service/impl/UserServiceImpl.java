package com.itheima.mp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.itheima.mp.domain.dto.UserDTO;
import com.itheima.mp.domain.po.Address;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.AddressVO;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.enums.UserStatus;
import com.itheima.mp.service.IUserService;
import com.itheima.mp.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        if(user.getStatus() == UserStatus.FREEZE) {
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
                .eq(User::getBalance, user.getBalance()) // 并发问题，设置乐观锁（修改前比较一下）
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

    /**
     * 查询用户的同时把地址也查出来
     * @param id 用户id
     * @return
     */
    @Override
    public UserVO getUserAndAddressById(Long id) {
        // 查询用户
        User user = this.getById(id);
        if(user == null) {
            throw new RuntimeException("该用户不存在");
        }
        // 查询地址
        // 使用静态工具，需要指定实体类，mybatis-plus才知道操作那张表
        List<Address> addressList = Db.lambdaQuery(Address.class)
                .eq(Address::getUserId, id)
                .list();
        // 构造结果对象
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        if(!CollectionUtils.isEmpty(addressList)) {
            List<AddressVO> addressVOList = BeanUtil.copyToList(addressList, AddressVO.class);
            userVO.setAddressList(addressVOList);
        }

        return userVO;
    }

    @Override
    public List<UserVO> getUsers(List<Long> ids) {
        // 查询用户
        List<User> userList = this.listByIds(ids);
        if(CollectionUtils.isEmpty(userList)) {
            throw new RuntimeException("用户都不存在！");
        }
        // 获取用户ids
        List<Long> userIdList = userList.stream().map(User::getId).collect(Collectors.toList());
        // 查询地址, 使用静态工具
        List<Address> addressList = Db.lambdaQuery(Address.class)
                .in(Address::getUserId, userIdList)
                .list();
        List<AddressVO> addressVOList = BeanUtil.copyToList(addressList, AddressVO.class);
        // 处理数据
        Map<Long, List<AddressVO>> adddressMap = null;
        if(!CollectionUtils.isEmpty(addressVOList)) {
            adddressMap = addressVOList.stream().collect(Collectors.groupingBy(AddressVO::getUserId));
        }

        // 封装对象
        List<UserVO> result = new ArrayList<>(userList.size());
        for(User user : userList) {
            UserVO vo = BeanUtil.copyProperties(user, UserVO.class);
            // 获取改用户地址
            vo.setAddressList(adddressMap.get(user.getId()));
            result.add(vo);
        }
        return result;
    }
}
