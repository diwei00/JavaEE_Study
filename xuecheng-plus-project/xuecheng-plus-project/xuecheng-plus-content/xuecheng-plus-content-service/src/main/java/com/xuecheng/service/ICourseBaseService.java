package com.xuecheng.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDTO;
import com.xuecheng.content.model.dto.QueryCourseParamsDTO;
import com.xuecheng.content.model.po.CourseBase;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.vo.CourseBaseInfoVO;

/**
 * <p>
 * 课程基本信息 服务类
 * </p>
 *
 */


public interface ICourseBaseService extends IService<CourseBase> {

    PageResult<CourseBase> getCourseList(PageParams pageParams, QueryCourseParamsDTO queryCourseParamsDto);

    CourseBaseInfoVO createCourseBase(AddCourseDTO addCourseDTO, Long companyId);
}
