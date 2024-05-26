package com.example.poidemo.controller;

import com.example.poidemo.common.CommonResult;
import com.example.poidemo.service.SpringQuartzService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author wh
 * @Date 2024/5/26 16:28
 */

@RestController
@Api(tags = "SpringQuartz测试")
public class SpringQuartzController {
   @Autowired
   private SpringQuartzService springQuartzService;


   @PostMapping("/test")
   @ApiOperation(value = "测试")
   public CommonResult<String> test(String cron) {
      if(springQuartzService.test(cron)) {
         return CommonResult.success();
      }
      return CommonResult.fail();
   }
}
