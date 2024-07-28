package com.roadjava.mockito.standalone;

import com.roadjava.mockito.bean.req.UserUpdateReq;
import com.roadjava.mockito.bean.vo.UserVO;
import com.roadjava.mockito.service.UserService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * 参数匹配:通过方法签名(参数)来指定哪些方法调用需要被处理(插桩、verify验证)
 * @author zhaodaowen
 * @see <a href="http://www.roadjava.com">乐之者java</a>
 */
@ExtendWith(MockitoExtension.class)
public class ParamMatcherTest {
    @Mock
    private UserService mockUserService;

    @Test
    public void test4(){
        List<String> features = new ArrayList<>();
        mockUserService.add("乐之者java","123", features);
        // 校验参数为  "乐之者java","123", features 的add方法调用了1次
        Mockito.verify(mockUserService,Mockito.times(1)).add("乐之者java","123", features);

        // 报错: When using matchers, all arguments have to be provided by matchers
//        Mockito.verify(mockUserService,Mockito.times(1)).add(ArgumentMatchers.anyString(),"123", features);

        Mockito.verify(mockUserService,Mockito.times(1)).add(anyString(),anyString(), anyList());

    }
    /**
     * ArgumentMatchers.any拦截UserUpdateReq类型的任意对象,
     * 除了any,还有anyXx(anyLong,anyString...),注意:它们都不包括null
     */
    @Test
    public void test3(){
        Mockito.doReturn(99).when(mockUserService).modifyById(ArgumentMatchers.any(UserUpdateReq.class));
        UserUpdateReq userUpdateReq1 = new UserUpdateReq();
        userUpdateReq1.setId(1L);
        userUpdateReq1.setPhone("1L");
        int result1 = mockUserService.modifyById(userUpdateReq1);
        // 99
        System.out.println("result1 = " + result1);

        UserUpdateReq userUpdateReq2 = new UserUpdateReq();
        userUpdateReq2.setId(2L);
        userUpdateReq2.setPhone("2L");
        int result2 = mockUserService.modifyById(userUpdateReq2);
        // 99
        System.out.println("result2 = " + result2);
    }

    /**
     * 测试插桩时的参数匹配,只拦截userUpdateReq1
     */
    @Test
    public void test2(){
        UserUpdateReq userUpdateReq1 = new UserUpdateReq();
        userUpdateReq1.setId(1L);
        userUpdateReq1.setPhone("1L");
        // 指定参数为userUpdateReq1时调用mockUserService.modifyById返回99
        Mockito.doReturn(99).when(mockUserService).modifyById(userUpdateReq1);
        int result1 = mockUserService.modifyById(userUpdateReq1);
        int result3 = mockUserService.modifyById(userUpdateReq1);
        // 99
        System.out.println("result1 = " + result1);
        // result3 = 99
        System.out.println("result3 = " + result3);

        UserUpdateReq userUpdateReq2 = new UserUpdateReq();
        userUpdateReq2.setId(2L);
        userUpdateReq2.setPhone("2L");
        int result2 = mockUserService.modifyById(userUpdateReq2);
        // 0
        System.out.println("result2 = " + result2);
    }
    /**
     * 对于mock对象不会调用真实方法,直接返回mock对象的默认值:
     * 默认值(int)、null(UserVO)、空集合(List)
     */
    @Test
    public void test1(){
        UserVO userVO = mockUserService.selectById(1L);
        // null
        System.out.println("userVO = " + userVO);
        UserUpdateReq userUpdateReq1 = new UserUpdateReq();
        int i = mockUserService.modifyById(userUpdateReq1);
        // 0
        System.out.println("i = " + i);
    }
}
