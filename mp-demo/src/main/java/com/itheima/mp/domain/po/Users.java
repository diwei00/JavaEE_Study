package com.itheima.mp.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Description 测试类
 * @Author wh
 * @Date 2024/4/2 9:44
 */

@Data
@TableName("users")
public class Users {
   @TableId(value = "id", type = IdType.AUTO)
   private Integer id;
   private String name;
   private Integer age;
}
