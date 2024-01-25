package com.itheima.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.itheima.mp.domain.po.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testInsert() {
        User user = new User();
        user.setId(6L);
        user.setUsername("Lucy2");
        user.setPassword("123");
        user.setPhone("18688990011");
        user.setBalance(200);
        user.setInfo("{\"age\": 24, \"intro\": \"英文老师\", \"gender\": \"female\"}");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insert(user);
    }

    @Test
    void testSelectById() {
        User user = userMapper.selectById(5L);
        System.out.println("user = " + user);
    }


    @Test
    void testQueryByIds() {
        List<User> users = userMapper.selectBatchIds(List.of(1L, 2L, 3L, 4L));
        // 方法引用
        users.forEach(System.out::println);
    }

    @Test
    void testUpdateById() {
        User user = new User();
        user.setId(5L);
        user.setBalance(20000);
        user.setUsername("bbb");
        userMapper.updateById(user);
    }

    @Test
    void testDeleteUser() {
        userMapper.deleteById(5L);
    }

    @Test
    void testQueryById() {
        User user = userMapper.queryById(1L);
        System.out.println(user);
    }

    @Test
    void testQueryWrapper() {
        // 条件构造器
        // 构造查询字段，以及where部分
        // 模糊查询会自动加%x%
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                .select("id", "username")
                .like("username", "o")
                .ge("balance", 1000);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    void testUpdateByQueryWrapper() {

        QueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                .eq("username", "Jack");
        // 跟新数据，只会更新User中非空字段
        User user = new User();
        user.setBalance(10);
        userMapper.update(user, queryWrapper);
    }

    @Test
    void testUpdateWrapper() {
        List<Long> idList = List.of(1L, 2L, 3L);
        // 可以指定update 中的set部分
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<User>()
                .setSql("balance = balance - 10")
                .in("id", idList);
        userMapper.update(null, userUpdateWrapper);
    }

    @Test
    void testLambdaQueryWrapper() {
        // LambdaQueryWrapper，字段不用写死，可以通过反射拿到字段名
        // 合法，不存在魔法数字
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .select(User::getId, User::getUsername)
                .like(User::getUsername, "o")
                .ge(User::getBalance, 1000);

        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 自定义sql
     */
    @Test
    void testDeductBalanceByIds() {
        List<Long> idList = List.of(1L, 2L, 3L);
        // 构造where部分
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .in("id", idList);
        userMapper.deductBalanceByIds(wrapper, 10);

    }
}