package com.roadjava.mockito.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.roadjava.mockito.bean.req.UserUpdateReq;
import com.roadjava.mockito.bean.vo.UserVO;
import com.roadjava.mockito.bean.entity.UserDO;

import java.util.List;

/**
 * @author zhaodaowen
 * @see <a href="http://www.roadjava.com">乐之者java</a>
 */
public interface UserService extends IService<UserDO> {
    /**
     * 通过用户id查询用户信息
     * @param userId 用户id
     * @return 用户dto
     */
    UserVO selectById(Long userId);

    /**
     * 方便等会测试mockito的api,所以参数分开传
     * 添加一个用户和其对应的特征列表
     * @param username 用户名
     * @param phone 电话
     * @param features 用户对应的特征列表
     */
    void add(String username, String phone, List<String> features);

    /**
     * 通过id更新用户信息
     * @param updateReq 待更新用户信息
     * @return 1:成功  0:失败
     */
    int modifyById(UserUpdateReq updateReq);

    /**
     * 返回一个固定数字
     * @return 固定数字
     */
    int getNumber();
}
