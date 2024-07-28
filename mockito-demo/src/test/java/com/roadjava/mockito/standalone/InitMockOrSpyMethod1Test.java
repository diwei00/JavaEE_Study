package com.roadjava.mockito.standalone;

import com.roadjava.mockito.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * 初始化mock/spy对象的方式有三种,第一种
 * @author zhaodaowen
 * @see <a href="http://www.roadjava.com">乐之者java</a>
 */
@ExtendWith(MockitoExtension.class)
public class InitMockOrSpyMethod1Test {
    @Mock
    private UserService mockUserService;
    @Spy
    private UserService spyUserService;

    @Test
    public void test1(){
        // true 判断某对象是不是mock对象
        System.out.println("Mockito.mockingDetails(mockUserService).isMock() = " + Mockito.mockingDetails(mockUserService).isMock());
        // false
        System.out.println("Mockito.mockingDetails(mockUserService).isSpy() = " + Mockito.mockingDetails(mockUserService).isSpy());
        // true
        System.out.println("Mockito.mockingDetails(spyUserService).isSpy() = " + Mockito.mockingDetails(spyUserService).isSpy());
        // true spy对象另一种不同类型的mock对象
        System.out.println("Mockito.mockingDetails(spyUserService).isMock() = " + Mockito.mockingDetails(spyUserService).isMock());
    }
}
