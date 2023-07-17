package com.example.demo.controller;

import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    // 编程式事务，需要操作这两个对象（内置在Spring框架中）
    @Autowired
    private DataSourceTransactionManager transactionManager;
    @Autowired
    private TransactionDefinition transactionDefinition;

    @RequestMapping("/del")
    public int del(Integer id) {
        if(id == null || id < 0) {
            return 0;
        }
        int result = 0;
        TransactionStatus transactionStatus = null;
        try {
            // 开启事务（获得当前事务）
            transactionManager.getTransaction(transactionDefinition);
            // 执行业务
            result = userService.del(id);
            System.out.println("删除：" + result);
            int a = 10 / 0;
            // 提交事务/回滚事务
            // 正常执行就提交事务
            transactionManager.commit(transactionStatus);
        }catch (Exception e) {
            // 出现异常就回滚事务
            if(transactionStatus != null) {
                transactionManager.rollback(transactionStatus);
            }
        }
        return result;

    }

}
