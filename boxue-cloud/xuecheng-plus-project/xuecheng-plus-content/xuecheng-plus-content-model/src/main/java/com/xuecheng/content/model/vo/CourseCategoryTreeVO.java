package com.xuecheng.content.model.vo;

import com.xuecheng.content.model.po.CourseCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 树状接口课程类型模型类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseCategoryTreeVO extends CourseCategory {


    // 子节点数据
    List<CourseCategoryTreeVO> childrenTreeNodes;
}
