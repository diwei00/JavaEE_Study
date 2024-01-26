package com.itheima.mp.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.itheima.mp.domain.vo.AddressVO;
import com.itheima.mp.enums.UserStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

// mybatis-plus 会按照驼峰转下划线进行转换
@Data
@TableName(value = "user",autoResultMap = true) // 指定数据库表名
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
     * 指定该字段的类型处理器
     * 插入数据库时，该字段会转换为json。
     * 查询数据时，json会转为该对象
     * 对象嵌套需要指定ResultMap，可以使用mybatis-plus自动指定
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private UserInfo info;

    /**
     * 使用状态（1正常 2冻结）
     */
    private UserStatus status;

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
