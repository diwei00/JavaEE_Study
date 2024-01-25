package com.itheima.mp.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user") // 指定数据库表名
public class User {

    /**
     * 用户id
     * 指定id，id生成策略
     *
     */

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     * 指定字段名称，映射数据库字段名
     * 情况：
     *    字段名与数据库字段名不一致
     *    字段名以isxxx开头，mybatis-plus会去除is，以其他字母作为数据库字段名
     *    变量名为数据库关键字（需要转义）
     */
    @TableField("username")
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 注册手机号
     */
    private String phone;

    /**
     * 详细信息
     */
    private String info;

    /**
     * 使用状态（1正常 2冻结）
     */
    private Integer status;

    /**
     * 账户余额
     */
    private Integer balance;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
