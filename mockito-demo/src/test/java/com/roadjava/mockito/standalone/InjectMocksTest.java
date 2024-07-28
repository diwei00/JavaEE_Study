package com.roadjava.mockito.standalone;

import com.roadjava.mockito.service.UserFeatureService;
import com.roadjava.mockito.service.UserService;
import com.roadjava.mockito.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

/**
 * @author zhaodaowen
 * @see <a href="http://www.roadjava.com">乐之者java</a>
 */
@ExtendWith(MockitoExtension.class)
public class InjectMocksTest {
    /**
     * 1.被@InjectMocks标注的属性必须是实现类,因为mockito会创建对应的实例对象,默认创建的对象就是
     * 未经过mockito处理的普通对象,因此常配合@Spy注解使其变为默认调用真实方法的mock对象,被测试的类
     * 一般都需要标记这俩注解
     * 2.mockito会使用spy对象或mock对象注入到@InjectMocks对应的实例对象中
     */
    @InjectMocks
    @Spy
    private UserServiceImpl userService;

    @Mock
    private UserFeatureService userFeatureService;

    @Mock
    private List<String> mockList;

    @Test
    public void test1() {
        int number = userService.getNumber();
        Assertions.assertEquals(0,number);
    }
}
