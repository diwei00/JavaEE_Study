package com.example.poidemo.test;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.lang.NonNull;

/**
 * @Description
 * @Author wh
 * @Date 2025/2/13 21:31
 */
public abstract class AbstractOrderStateTransferProcessor implements IOrderStateTransferProcessor{


    protected void afterProcess() {
        System.out.println("执行抽象类方法afterProcess");
    }


    @Override
    public void process() {
        this.afterProcess();
    }




}
