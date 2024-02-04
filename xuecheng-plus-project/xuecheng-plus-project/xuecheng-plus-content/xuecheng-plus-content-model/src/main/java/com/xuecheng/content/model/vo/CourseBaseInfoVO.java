package com.xuecheng.content.model.vo;

import com.xuecheng.content.model.po.CourseBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *   课程基本信息dto
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseBaseInfoVO extends CourseBase {


 /**
  * 收费规则，对应数据字典
  */
 private String charge;

 /**
  * 价格
  */
 private Float price;


 /**
  * 原价
  */
 private Float originalPrice;

 /**
  * 咨询qq
  */
 private String qq;

 /**
  * 微信
  */
 private String wechat;

 /**
  * 电话
  */
 private String phone;

 /**
  * 有效期天数
  */
 private Integer validDays;

 /**
  * 大分类名称
  */
 private String mtName;

 /**
  * 小分类名称
  */
 private String stName;

}
