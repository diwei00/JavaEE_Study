package com.example.poidemo.controller;

import com.example.poidemo.entity.User;
import com.example.poidemo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description
 * @Author wh
 * @Date 2024/5/16 20:00
 */

@RestController
@RequestMapping("/user")
@Api(tags = "用户")
public class UserController {

   @Autowired
   private UserService userService;


   @ApiOperation(value = "导出用户信息", notes = "export", produces = "application/octet-stream")
   @GetMapping("/getWord")
   public String getUser(String id, HttpServletResponse response) {
      if(!StringUtils.hasLength(id)) {
         return "id非法";
      }
      return userService.getUserToWord(id, response);
   }

   @ApiOperation(value = "导出用户信息", notes = "export", produces = "application/octet-stream")
   @GetMapping("/getExcel")
   public String getUserExcel(String ids, HttpServletResponse response) {
      if(!StringUtils.hasLength(ids)) {
         return "ids非法";
      }
      Boolean result = userService.getUserToExcel(ids, response);
      if(result) {
         return "导出成功";
      }
      return "导出失败";
   }
}
