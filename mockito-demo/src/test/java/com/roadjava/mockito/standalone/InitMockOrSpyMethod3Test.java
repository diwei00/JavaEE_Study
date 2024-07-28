package com.roadjava.mockito.standalone;

import com.roadjava.mockito.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

/**
 * 初始化mock/spy对象的方式有三种,第三种
 * @author zhaodaowen
 * @see <a href="http://www.roadjava.com">乐之者java</a>
 */
public class InitMockOrSpyMethod3Test {
    @Mock
    private UserService mockUserService;
    @Spy
    private UserService spyUserService;

    @BeforeEach
    public void init(){
        // 识别本类中的@Mock 或 @Spy 等mockito注解并创建对应的对象
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void test1(){
        // true
        System.out.println("Mockito.mockingDetails(mockUserService).isMock() = " + Mockito.mockingDetails(mockUserService).isMock());
        // false
        System.out.println("Mockito.mockingDetails(mockUserService).isSpy() = " + Mockito.mockingDetails(mockUserService).isSpy());
        // true
        System.out.println("Mockito.mockingDetails(spyUserService).isSpy() = " + Mockito.mockingDetails(spyUserService).isSpy());
        // true
        System.out.println("Mockito.mockingDetails(spyUserService).isMock() = " + Mockito.mockingDetails(spyUserService).isMock());
    }
}
