package com.example.poidemo.common;

import lombok.Data;

/**
 * 统一返回对象
 * @param <T>
 */
@Data
public class CommonResult<T> {
 
    // 操作代码
    Integer code;
 
    // 提示信息
    String message;
 
    // 结果数据
    T data;
 
    public CommonResult(ResultEnum resultCode) {
        this.code = resultCode.code();
        this.message = resultCode.message();
    }
 
    public CommonResult(ResultEnum resultCode, T data) {
        this.code = resultCode.code();
        this.message = resultCode.message();
        this.data = data;
    }
    public CommonResult(String message) {
        this.code = ResultEnum.FAIL.code();
        this.message = message;
    }
    //成功返回封装-无数据
    public static CommonResult<String> success() {
        return new CommonResult<String>(ResultEnum.SUCCESS);
    }

    //成功返回封装-带数据
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<T>(ResultEnum.SUCCESS, data);
    }

    //失败返回封装-使用默认提示信息
    public static CommonResult<String> fail() {
        return new CommonResult<String>(ResultEnum.FAIL);
    }

    //失败返回封装-使用返回结果枚举提示信息
    public static CommonResult<String> fail(ResultEnum resultCode) {
        return new CommonResult<String>(resultCode);
    }

    //失败返回封装-使用自定义提示信息
    public static CommonResult<String> fail(String message) {
        return new CommonResult<String>(message);
 
    }
}