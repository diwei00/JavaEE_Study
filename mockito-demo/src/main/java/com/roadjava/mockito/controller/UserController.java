package com.roadjava.mockito.controller;

import com.roadjava.mockito.bean.req.UserAddReq;
import com.roadjava.mockito.bean.req.UserUpdateReq;
import com.roadjava.mockito.bean.vo.UserVO;
import com.roadjava.mockito.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zhaodaowen
 * @see <a href="http://www.roadjava.com">乐之者java</a>
 */
@Slf4j
@RestController
@Validated
public class UserController {
    @Resource
    private UserService userService;
    @GetMapping("/selectById")
    public UserVO selectById(@NotNull Long userId){
        return userService.selectById(userId);
    }

    @PostMapping("/add")
    public String add(@RequestBody @Validated UserAddReq addReq) {
        userService.add(addReq.getUsername(),addReq.getPhone(),addReq.getFeatures());
        return "ok";
    }

    @PostMapping("/modifyById")
    public String modifyById(@RequestBody @Validated UserUpdateReq updateReq) {
        userService.modifyById(updateReq);
        return "ok";
    }
}
