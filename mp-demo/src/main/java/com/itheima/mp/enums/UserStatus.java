package com.itheima.mp.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 用户状态枚举
 */
@Getter
public enum UserStatus {
    NORMAL(1, "正常"),
    FREEZE(2, "冻结")
    ;

    // 标记枚举哪个字段作为数据库值
    @EnumValue // 数据库存储
    private final Integer value;
    // 标记json处理哪个字段作为model返回（此注解为SpringMVC级别）
    @JsonValue // 前端展示
    private final String desc;


    UserStatus(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
