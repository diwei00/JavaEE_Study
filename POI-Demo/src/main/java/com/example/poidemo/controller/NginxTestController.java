package com.example.poidemo.controller;

import com.example.poidemo.common.CommonResult;
import com.example.poidemo.util.IpUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description
 * @Author wh
 * @Date 2024/5/25 14:42
 */

@RestController
public class NginxTestController {


   @GetMapping("/test")
   public CommonResult<String> test(HttpServletRequest request) {
      System.out.println(IpUtil.getIpAddr(request));
      System.out.println("aaaa");
      return CommonResult.success();
   }

}
