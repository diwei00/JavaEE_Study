package com.roadjava.mockito.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.roadjava.mockito.bean.entity.UserDO;
import com.roadjava.mockito.bean.entity.UserFeatureDO;

import java.util.List;

/**
 * @author zhaodaowen
 * @see <a href="http://www.roadjava.com">乐之者java</a>
 */
public interface UserFeatureService extends IService<UserFeatureDO> {
    /**
     * 通过用户id查询该用户对应的特征值列表
     * @param userId 用户id
     * @return 特征值列表
     */
    List<UserFeatureDO> selectByUserId(Long userId);
}
