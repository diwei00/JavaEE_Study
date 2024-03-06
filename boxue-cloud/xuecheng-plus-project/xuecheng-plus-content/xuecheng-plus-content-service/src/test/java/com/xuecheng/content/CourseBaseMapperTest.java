package com.xuecheng.content;


import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.mapper.CourseBaseMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CourseBaseMapperTest {

    @Autowired
    private CourseBaseMapper courseBaseMapper;

    @Test
    public void selectTest() {
        CourseBase courseBase = courseBaseMapper.selectById(1L);
        System.out.println("*************************");
        System.out.println(courseBase);
    }
}
