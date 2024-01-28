package com.itheima.mp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.itheima.mp.domain.dto.PageDTO;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.po.Address;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.AddressVO;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.enums.UserStatus;
import com.itheima.mp.service.IUserService;
import com.itheima.mp.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
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
    public List<UserVO> getUserList(UserQuery userQuery) {
        // lambdaQuery实现复杂条件查询（动态sql）
        List<User> userList = this.lambdaQuery()
                .like(userQuery.getName() != null, User::getUsername, userQuery.getName())
                .eq(userQuery.getStatus() != null, User::getStatus, userQuery.getStatus())
                .ge(userQuery.getMinBalance() != null, User::getBalance, userQuery.getMinBalance())
                .le(userQuery.getMaxBalance() != null, User::getBalance, userQuery.getMaxBalance())
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

    @Override
    public PageDTO<UserVO> getUserListPage(UserQuery query) {
//        // 构造page对象
//        Page<User> page = Page.of(query.getPageNumber(), query.getPageSize());
//        // 指定排序规则
//        if(StringUtils.hasLength(query.getSortBy())) {
//            // 前端指定字段排序
//            page.addOrder(new OrderItem(query.getSortBy(), query.getIsAsc()));
//        }else {
//            // 默认字段排序
//            page.addOrder(new OrderItem("update_time", false));
//        }


//        Page<User> page = query.toMpPageDefaultSortByCreateTimeDesc();
        Page<User> page = query.toMpPage();




        // 分页查询
        Page<User> p = this.lambdaQuery()
                .like(query.getName() != null, User::getUsername, query.getName())
                .eq(query.getStatus() != null, User::getStatus, query.getStatus())
                .page(page);

        return PageDTO.of(p, UserVO.class);
    }
}
