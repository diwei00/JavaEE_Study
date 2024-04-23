package com.example.easyexceldemo.domain;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author wh
 * @Date 2024/4/23 16:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

   @ExcelIgnore
   private int id;
   @ExcelProperty(value = "姓名", order = 0)
   private String name;
   @ExcelProperty(value = "年龄", order = 1)
   private int age;
   @ExcelProperty(value = "性别", order = 2)
   private String sex;

}
