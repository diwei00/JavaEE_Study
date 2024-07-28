package com.roadjava.mockito.service.impl;

import com.roadjava.mockito.bean.entity.UserDO;
import com.roadjava.mockito.bean.entity.UserFeatureDO;
import com.roadjava.mockito.bean.req.UserUpdateReq;
import com.roadjava.mockito.bean.vo.UserVO;
import com.roadjava.mockito.service.UserFeatureService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * squaretest使用:
 * 1.先运行一下,根据错误进行修改
 * 2.被测试类一般要加上@Spy(因为可能需要对某些方法进行插桩)
 * 3.一次检查各个方法,根据错误提示进行修改(对一些方法进行插桩、修改返回值、修改断言.....)
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserFeatureService mockUserFeatureService;

    @InjectMocks
    @Spy
    private UserServiceImpl userServiceImplUnderTest;

    @Test
    void testSelectById() {
        // Setup
        // Configure userServiceImplUnderTest.getById(...).
        UserDO existedEntity = new UserDO();
        existedEntity.setId(0L);
        existedEntity.setUsername("username");
        existedEntity.setPhone("phone");
        doReturn(existedEntity).when(userServiceImplUnderTest).getById(0L);

        // Configure UserFeatureService.selectByUserId(...).
        final UserFeatureDO userFeatureDO = new UserFeatureDO();
        userFeatureDO.setId(0L);
        userFeatureDO.setUserId(0L);
        userFeatureDO.setFeatureValue("featureValue");
        final List<UserFeatureDO> userFeatureDOList = Arrays.asList(userFeatureDO);
        when(mockUserFeatureService.selectByUserId(0L)).thenReturn(userFeatureDOList);

        // Run the test
        final UserVO result = userServiceImplUnderTest.selectById(0L);

        // Verify the results
        assertThat(result.getFeatureValue().size()).isEqualTo(1);
    }

    @Test
    void testSelectById_UserFeatureServiceReturnsNoItems() {
        // Setup
        // Configure userServiceImplUnderTest.getById(...).
        UserDO existedEntity = new UserDO();
        existedEntity.setId(0L);
        existedEntity.setUsername("username");
        existedEntity.setPhone("phone");
        doReturn(existedEntity).when(userServiceImplUnderTest).getById(0L);

        when(mockUserFeatureService.selectByUserId(0L)).thenReturn(Collections.emptyList());

        // Run the test
        final UserVO result = userServiceImplUnderTest.selectById(0L);

        // Verify the results
        assertThat(result.getFeatureValue()).isNull();
    }

    @Test
    void testAdd() {
        // Setup
        when(mockUserFeatureService.saveBatch(anyList())).thenReturn(true);
        // config
        doReturn(true).when(userServiceImplUnderTest).save(any(UserDO.class));
        // Run the test
        Assertions.assertDoesNotThrow(() -> userServiceImplUnderTest.add("username", "phone", Arrays.asList("value")));
        // Verify the results
        verify(mockUserFeatureService).saveBatch(anyList());
    }

    @Test
    void testModifyById() {
        // Setup
        final UserUpdateReq updateReq = new UserUpdateReq();
        updateReq.setId(0L);
        updateReq.setPhone("phone");
        // config
        doReturn(true).when(userServiceImplUnderTest).updateById(any(UserDO.class));
        // Run the test
        final int result = userServiceImplUnderTest.modifyById(updateReq);

        // Verify the results
        assertThat(result).isEqualTo(1);
    }

    @Test
    void testGetNumber() {
        assertThat(userServiceImplUnderTest.getNumber()).isEqualTo(0);
    }
}
