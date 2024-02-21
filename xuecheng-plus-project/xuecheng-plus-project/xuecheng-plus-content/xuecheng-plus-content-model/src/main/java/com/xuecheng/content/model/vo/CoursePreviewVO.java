package com.xuecheng.content.model.vo;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Slf4j
public class CoursePreviewVO {
    //课程基本信息,课程营销信息
    CourseBaseInfoVO courseBase;
    //课程计划信息
    List<TeachplanVO> teachplans;

    // todo:师资信息
}
