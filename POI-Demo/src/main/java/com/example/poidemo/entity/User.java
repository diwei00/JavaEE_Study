package com.example.poidemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @Description
 * @Author wh
 * @Date 2024/5/16 19:55
 */

@Data
@ApiModel
@TableName("user")
public class User {

   @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String username;
    private Integer age;
    private String sex;
    private Integer height;
    private Integer weight;
}
