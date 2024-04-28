package com.itheima.mp.controller;

import cn.hutool.core.bean.BeanUtil;
import com.itheima.mp.domain.dto.PageDTO;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.dto.UserFormDTO;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("用户管理接口")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;




    @ApiOperation("新增用户")
    @PostMapping("/add")
    public Boolean addUser(@RequestBody UserFormDTO userFormDTO) {
        User user = BeanUtil.copyProperties(userFormDTO, User.class);
        return userService.save(user);
    }

    @ApiOperation("删除用户")
    @PostMapping("/del/{id}")
    public Boolean delUser(@PathVariable("id") Long id) {
        return userService.removeById(id);
    }

    @ApiOperation("根据id查询用户")
    @GetMapping("getUser/{id}")
    public UserVO getUser(@PathVariable("id") Long id) {

        return userService.getUserAndAddressById(id);
    }

    @ApiOperation("根据id集合查询用户")
    @GetMapping("/getUserList/{id}")
    public List<UserVO> getUserList(@PathVariable("id") List<Long> ids) {
//        List<User> userList = userService.listByIds(ids);
        return userService.getUsers(ids);
    }


    @ApiOperation("根据id扣减余额")
    @PostMapping("/deduction/{id}/{money}")
    public Boolean deductBalance(@PathVariable("id") Long id, @PathVariable("money") Integer money) {
        return userService.deductBalance(id, money);
    }

    @ApiOperation("复杂条件查询用户")
    @GetMapping("/list")
    public List<UserVO> getUserList(UserQuery userQuery) {
        return userService.getUserList(userQuery);
    }


    @ApiOperation("分页查询用户")
    @GetMapping("/page")
    public PageDTO<UserVO> getUserListPage(UserQuery userQuery) {
        return userService.getUserListPage(userQuery);
    }

    @PostMapping("/test")
    public String test(String id) {
        return id;
    }
}
