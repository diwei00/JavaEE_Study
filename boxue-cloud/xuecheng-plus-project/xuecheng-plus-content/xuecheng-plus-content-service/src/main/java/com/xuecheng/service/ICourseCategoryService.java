package com.xuecheng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.po.CourseCategory;
import com.xuecheng.content.model.vo.CourseCategoryTreeVO;

import java.util.List;

/**
 * <p>
 * 课程分类 服务类
 * </p>
 *
 */
public interface ICourseCategoryService extends IService<CourseCategory> {

    List<CourseCategoryTreeVO> queryTreeNodes(String id);
}
