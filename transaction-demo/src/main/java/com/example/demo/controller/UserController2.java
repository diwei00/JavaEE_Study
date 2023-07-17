package com.example.demo.controller;

import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user2")
public class UserController2 {
    @Autowired
    private UserService userService;

    // 在方法开始之前开启事务，方法正常执行结束提交事务，如果执行途中发生异常则回滚事务
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @RequestMapping("/del")
    public int del(Integer id) {
        if (id == null || id < 0) {
            return 0;
        }
        int result = userService.del(id);
        System.out.println("删除：" + id);
        // 如果代码中使用了catch捕获了异常，那么自己就得处理
        // 这个时候@Transactional已经感知不到代码中抛异常了，那么就不会回滚
        try {
            int a = 10 / 0;
        }catch (Exception e) {
            // 将异常继续抛出去，让框架感知到也会触发回滚（不推荐，不优雅）
            //throw e;
            // 手动回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return result;
    }


}
