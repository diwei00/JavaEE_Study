package com.itheima.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.mp.domain.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

// 集成 mybatis-plus 提供的BaseMapper接口
// 它提供对单表的增删改查方法
// 通过User实体类映射到数据库具体的表，驼峰字段转下划线进行字段映射（根据变量类型进行推断字段类型）
// 特殊：属性为is开头的，mybatis-plus会删除is，其他字母作为数据库字段名
// 名为id的作为主键
// 上述为mybatis-plus的约定，如果和约定不符则需要使用注解显示指定

@Mapper
public interface UserMapper extends BaseMapper<User> {

//    void saveUser(User user);
//
//    void deleteUser(Long id);
//
//    void updateUser(User user);
//
//    User queryUserById(@Param("id") Long id);
//
//    List<User> queryUserByIds(@Param("ids") List<Long> ids);

    User queryById(Long id);

    // 这里条件构造器别名必须为ew（规定）
    void deductBalanceByIds(@Param("ew") QueryWrapper<User> wrapper, @Param("money") Integer money);
}
