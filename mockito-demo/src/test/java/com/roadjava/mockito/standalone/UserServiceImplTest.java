package com.roadjava.mockito.standalone;

import com.roadjava.mockito.bean.entity.UserDO;
import com.roadjava.mockito.bean.entity.UserFeatureDO;
import com.roadjava.mockito.bean.vo.UserVO;
import com.roadjava.mockito.mapper.UserFeatureMapper;
import com.roadjava.mockito.mapper.UserMapper;
import com.roadjava.mockito.service.UserFeatureService;
import com.roadjava.mockito.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;

/**
 * 实战讲解
 * @author zhaodaowen
 * @see <a href="http://www.roadjava.com">乐之者java</a>
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    @Spy
    private UserServiceImpl userService;

    @Mock
    private UserFeatureService userFeatureService;

    @Mock
    private UserMapper userMapper;

    @Test
    public void testSelectById3() {
        // 配置方法getById的返回值
        UserDO ret = new UserDO();
        ret.setId(1L);
        ret.setUsername("乐之者java");
        ret.setPhone("http://www.roadjava.com");
        doReturn(ret).when(userService).getById(1L);
        // 配置userFeatureService.selectByUserId的返回值
        List<UserFeatureDO> userFeatureDOList = new ArrayList<>();
        UserFeatureDO userFeatureDO = new UserFeatureDO();
        userFeatureDO.setId(88L);
        userFeatureDO.setUserId(1L);
        userFeatureDO.setFeatureValue("aaaa");
        userFeatureDOList.add(userFeatureDO);
        doReturn(userFeatureDOList).when(userFeatureService).selectByUserId(1L);
        // 执行测试
        UserVO userVO = userService.selectById(1L);
        // 断言
        Assertions.assertEquals(1,userVO.getFeatureValue().size());
    }


    @Test
    public void testSelectById2() {
        // 配置方法getById的返回值
        UserDO ret = new UserDO();
        ret.setId(1L);
        ret.setUsername("乐之者java");
        ret.setPhone("http://www.roadjava.com");
        doReturn(ret).when(userService).getById(1L);
        UserVO userVO = userService.selectById(1L);
        Assertions.assertNotNull(userVO);
    }

    @Test
    public void testSelectById1() {
        // 配置
        doReturn(userMapper).when(userService).getBaseMapper();
        UserVO userVO = userService.selectById(1L);
        Assertions.assertNull(userVO);
    }
}
