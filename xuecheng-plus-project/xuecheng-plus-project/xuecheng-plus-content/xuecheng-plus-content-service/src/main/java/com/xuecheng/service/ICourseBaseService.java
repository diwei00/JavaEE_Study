package com.xuecheng.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程基本信息 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-02-11
 */


public interface ICourseBaseService extends IService<CourseBase> {

    PageResult<CourseBase> getCourseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto);
}
