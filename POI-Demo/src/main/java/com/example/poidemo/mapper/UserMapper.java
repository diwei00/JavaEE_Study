package com.example.poidemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.poidemo.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description
 * @Author wh
 * @Date 2024/5/16 19:59
 */

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
