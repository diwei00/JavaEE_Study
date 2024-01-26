package com.itheima.mp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.mp.domain.dto.UserDTO;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.UserVO;

import java.util.List;

public interface IUserService extends IService<User> {

    Boolean deductBalance(Long id, Integer money);

    List<UserVO> getUserList(UserDTO userDTO);

    UserVO getUserAndAddressById(Long id);

    List<UserVO> getUsers(List<Long> ids);
}
