package com.itheima.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.itheima.mp.domain.po.Address;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.po.UserInfo;
import com.itheima.mp.enums.UserStatus;
import com.itheima.mp.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IUserService userService;

    @Test
    void testInsert() {
        User user = new User();
//        user.setId(7L);
        user.setUsername("Lucy5");
        user.setPassword("123");
        user.setPhone("18688990011");
        user.setBalance(200);
        user.setInfo(new UserInfo(10, "语文老师", "aaa"));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setStatus(UserStatus.NORMAL);
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


    /**
     * 批量新增
     * 逐条插入数据
     * 网络通信最多，sql执行次数最多，效率最低
     * 156129
     */
    @Test
    void testSaveOneByOne() {
        long b = System.currentTimeMillis();
        for (int i = 1; i <= 100000; i++) {
            userMapper.insert(buildUser(i));
        }
        long e = System.currentTimeMillis();
        System.out.println("耗时：" + (e - b));
    }

    private User buildUser(int i) {
        User user = new User();
        user.setUsername("user_" + i);
        user.setPassword("123");
        user.setPhone("" + (18688190000L + i));
        user.setBalance(2000);
        user.setInfo(new UserInfo(10, "语文老师", "aaa"));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(user.getCreateTime());
        return user;
    }

    /**
     * mybatis-plus 批处理数据
     * 性能提升了10倍左右
     * 底层是采用jdbc预编译模式，先把数据转换为sql
     * 这里一次转换为1000条sql，然后进行网络通信，mysql服务器执行这1000条sql
     * 12297
     *
     * 目标：将1000条sql合并为1条sql（最佳性能）
     * 配置mysql连接时参数，rewriteBatchedStatements=true，重写批处理sql语句，合并为一条（mysql驱动做的）
     * 4611
     */
    @Test
    void testSaveBatch() {
        List<User> list = new ArrayList<>(1000);
        long a = System.currentTimeMillis();
        for(int i = 0; i < 100000; i++) {
            list.add(buildUser(i));
            if(i % 1000 == 0) {
                userService.saveBatch(list);
                list.clear();
            }
        }
        long b = System.currentTimeMillis();
        System.out.println(b - a);
    }

    // 删除数据
    @Test
    void deleteData() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().gt(User::getId, 7);
        userMapper.delete(wrapper);
    }

    @Test
    void deleteFlag() {
        // 配置逻辑删除后，这里sql自动变为逻辑删除
        // UPDATE address SET deleted=1 WHERE id=? AND deleted=0
        Db.removeById(59L, Address.class);
    }

    @Test
    void selectAddress() {
        // 配置逻辑删除后，查询会自动过滤删除标志
        //  SELECT * FROM address WHERE id=? AND deleted=0
        Address address = Db.getById(60L, Address.class);
//        System.out.println(address);

        User user = Db.getById(1L, User.class);
        System.out.println(user);
    }
}