package com.roadjava.mockito.standalone;

import com.roadjava.mockito.bean.req.UserUpdateReq;
import com.roadjava.mockito.bean.vo.UserVO;
import com.roadjava.mockito.service.UserService;
import com.roadjava.mockito.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author zhaodaowen
 * @see <a href="http://www.roadjava.com">乐之者java</a>
 */
@ExtendWith(MockitoExtension.class)
public class StubTest {
    @Mock
    private List<String> mockList;
    @Mock
    private UserServiceImpl mockUserServiceImpl;
    @Spy
    private UserServiceImpl spyUserServiceImpl;

    /**
     * 测试verify
     */
    @Test
    public void test8() {
        mockList.add("one");
        // true:调用mock对象的写操作方法是没效果的
        Assertions.assertEquals(0,mockList.size());
        mockList.clear();
        // 验证调用过1次add方法,且参数必须是one
        verify(mockList)
                // 指定要验证的方法和参数,这里不是调用,也不会产生调用效果
                .add("one");
        // 等价于上面verify(mockedList)
        verify(mockList,times(1)).clear();

        // 校验没有调用的两种方式
        verify(mockList, times(0)).get(1);
        verify(mockList, never()).get(1);

        // 校验最少或最多调用了多少次
        verify(mockList,atLeast(1)).clear();
        verify(mockList,atMost(1)).clear();
    }

    /**
     * 执行真正的原始方法
     */
    @Test
    public void test7(){
        // 对mock对象插桩让他执行原始方法
        when(mockUserServiceImpl.getNumber()).thenCallRealMethod();
        int number = mockUserServiceImpl.getNumber();
        Assertions.assertEquals(0,number);

        // spy对象默认就会调用真实方法,如果不想让它调用,需要单独为它进行插桩
        int spyResult = spyUserServiceImpl.getNumber();
        Assertions.assertEquals(0,spyResult);

        // 不让spy对象调用真实方法
        doReturn(1000).when(spyUserServiceImpl).getNumber();
        spyResult = spyUserServiceImpl.getNumber();
        Assertions.assertEquals(1000,spyResult);

    }

    /**
     * thenAnswer 实现指定逻辑的插桩
     */
    @Test
    public void test6(){
        when(mockList.get(anyInt())).thenAnswer(new Answer<String>() {
            /**
             * 泛型表示要插桩方法的返回值类型
             */
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                // getArgument表示获取插桩方法(此处就是list.get)的第几个参数值
                Integer argument = invocation.getArgument(0, Integer.class);
                return String.valueOf(argument * 100);
            }
        });
        String result = mockList.get(3);
        Assertions.assertEquals("300",result);
    }

    /**
     * 多次插桩
     */
    @Test
    public void test5(){
        // 第一次调用返回1,第2次调用返回2,第3次及之后的调用都返回3
//        when(mockList.size()).thenReturn(1).thenReturn(2).thenReturn(3);
        // 可间写为:
        when(mockList.size()).thenReturn(1,2,3);
        Assertions.assertEquals(1,mockList.size());
        Assertions.assertEquals(2,mockList.size());
        Assertions.assertEquals(3,mockList.size());
        Assertions.assertEquals(3,mockList.size());
    }
    /**
     * 抛出异常
     */
    @Test
    public void test4() {
        // 方法一
        doThrow(RuntimeException.class).when(mockList).clear();
        try {
            mockList.clear();
            // 走到下面这行说明插桩失败了
            Assertions.fail();
        }catch (Exception e) {
            // 断言表达式为真
            Assertions.assertTrue(e instanceof RuntimeException);
        }

        // 方法二
        when(mockList.get(anyInt())).thenThrow(RuntimeException.class);
        try {
            mockList.get(4);
            Assertions.fail();
        }catch (Exception e) {
            Assertions.assertTrue(e instanceof RuntimeException);
        }
    }
    /**
     * 插桩的两种方式
     */
    @Test
    public void test3() {
        when(mockUserServiceImpl.getNumber()).thenReturn(99);
        // mockUserServiceImpl.getNumber() = 99
        System.out.println("mockUserServiceImpl.getNumber() = " + mockUserServiceImpl.getNumber());

        when(spyUserServiceImpl.getNumber()).thenReturn(99);
        /*
        getNumber
        spyUserServiceImpl.getNumber() = 99
        spy对象在没有插桩时是调用真实方法的,写在when中会导致先执行一次原方法,达不到mock的目的,
        需使用doXxx().when(obj).someMethod()
         */
        System.out.println("spyUserServiceImpl.getNumber() = " + spyUserServiceImpl.getNumber());

        doReturn(1000).when(spyUserServiceImpl).getNumber();
        // spyUserServiceImpl.getNumber() = 1000
        System.out.println("spyUserServiceImpl.getNumber() = " + spyUserServiceImpl.getNumber());

    }

    /**
     * void返回值方法插桩
     */
    @Test
    public void test2() {
        // 调用mockList.clear的时候什么也不做
        doNothing().when(mockList).clear();
        mockList.clear();
        // 验证调用了一次clear
        verify(mockList, times(1)).clear();
    }

    /**
     * 指定返回值
     */
    @Test
    public void test1() {
        // 方法一
        doReturn("zero").when(mockList).get(0);
        // junit5里面自带的断言:如果返回值不相等则本单元测试会失败
        Assertions.assertEquals("zero", mockList.get(0));

        // 方法二
        when(mockList.get(1)).thenReturn("one");
        Assertions.assertEquals("one", mockList.get(1));
    }
}
