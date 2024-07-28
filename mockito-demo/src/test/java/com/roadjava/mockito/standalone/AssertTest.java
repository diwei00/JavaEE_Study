package com.roadjava.mockito.standalone;

import org.assertj.core.api.Assertions;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.when;

/**
 * @author zhaodaowen
 * @see <a href="http://www.roadjava.com">乐之者java</a>
 */
@ExtendWith(MockitoExtension.class)
public class AssertTest {
    @Mock
    private List<String> mockList;

    @Test
    public void test1() {
        when(mockList.size()).thenReturn(999);
        // 测试hamcrest的断言
        MatcherAssert.assertThat(mockList.size(), IsEqual.equalTo(999));
        // 测试 assertJ assertThat:参数为实际的值
        Assertions.assertThat(mockList.size()).isEqualTo(999);
        // junit5原生断言
        org.junit.jupiter.api.Assertions.assertEquals(999,mockList.size());
        // junit4原生断言
//        org.junit.Assert.assertEquals(999,mockList.size());
    }
}
