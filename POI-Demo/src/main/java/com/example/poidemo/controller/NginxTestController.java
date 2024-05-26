package com.example.poidemo.controller;

import com.example.poidemo.common.CommonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author wh
 * @Date 2024/5/25 14:42
 */

@RestController
public class NginxTestController {


   @GetMapping("/test")
   public CommonResult<String> test() {
      System.out.println("aaaa");
      return CommonResult.success();
   }

}
