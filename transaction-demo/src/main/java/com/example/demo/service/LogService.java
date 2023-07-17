package com.example.demo.service;

import com.example.demo.entity.Log;
import com.example.demo.mapper.LogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Service
public class LogService {
    @Autowired
    private LogMapper logMapper;

    @Transactional(propagation = Propagation.NESTED)
    public int add(Log log) {
        int result = logMapper.add(log);
        System.out.println("添加日志结果：" + result);
        // 手动回滚
//        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        // 手动回滚



        return result;
    }

}
