package com.example.demo.controller;

import com.example.demo.entity.UserInfo;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 演示事务传播机制
 */
@RestController
@RequestMapping("/user3")
public class UserController3 {

    @Autowired
    private UserService userService;

    // propagation = Propagation.REQUIRED 默认事务传播机制
    // REQUIRES_NEW 会新建事务，两个独立事务，相互不会影响
    // REQUIRED：加入该事务，一个事务回滚所有事务都回滚（外部事务回滚，不会报错）（内部事务回滚就会报错，外部事务不知道是提交还是回滚）
    // NESTED：嵌套事务，都是独立的事务，相互之间不影响，可以局部进行事务回滚

    @RequestMapping("/add")
    @Transactional(propagation = Propagation.NESTED)
    public int add(String username, String password) {
        // 非空判断
        if(username == null || username.equals("") || password == null || password.equals("")) {
            return 0;
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPassword(password);
        int result = userService.add(userInfo);

        return result;

    }

}
