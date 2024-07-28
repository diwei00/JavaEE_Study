package com.roadjava.mockito.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.roadjava.mockito.bean.entity.UserDO;
import com.roadjava.mockito.bean.entity.UserFeatureDO;
import com.roadjava.mockito.mapper.UserFeatureMapper;
import com.roadjava.mockito.mapper.UserMapper;
import com.roadjava.mockito.service.UserFeatureService;
import com.roadjava.mockito.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhaodaowen
 * @see <a href="http://www.roadjava.com">乐之者java</a>
 */
@Service
@Slf4j
public class UserFeatureServiceImpl extends ServiceImpl<UserFeatureMapper, UserFeatureDO> implements UserFeatureService {

    @Override
    public List<UserFeatureDO> selectByUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        LambdaQueryWrapper<UserFeatureDO> lqw = Wrappers.<UserFeatureDO>lambdaQuery()
                .eq(UserFeatureDO::getUserId,userId);
        return list(lqw);
    }
}
