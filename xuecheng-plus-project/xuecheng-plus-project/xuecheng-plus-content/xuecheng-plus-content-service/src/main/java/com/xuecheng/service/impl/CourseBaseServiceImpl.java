package com.xuecheng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.mapper.CourseBaseMapper;
import com.xuecheng.service.ICourseBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 课程基本信息 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class CourseBaseServiceImpl extends ServiceImpl<CourseBaseMapper, CourseBase> implements ICourseBaseService {

    /**
     * 课程信息查询
     * @param pageParams
     * @param queryCourseParamsDto
     * @return
     */
    @Override
    public PageResult<CourseBase> getCourseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto) {
        // 构造page
        Page<CourseBase> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        LambdaQueryWrapper<CourseBase> wrapper = new LambdaQueryWrapper<>();
        // 构造wrapper
        wrapper.
                like(StringUtils.hasLength(queryCourseParamsDto.getCourseName()), CourseBase::getName,
                        queryCourseParamsDto.getCourseName())
                .eq(StringUtils.hasLength(queryCourseParamsDto.getAuditStatus()), CourseBase::getAuditStatus,
                        queryCourseParamsDto.getAuditStatus())
                .eq(StringUtils.hasLength(queryCourseParamsDto.getPublishStatus()), CourseBase::getStatus,
                        queryCourseParamsDto.getPublishStatus());

        Page<CourseBase> pageResult = this.baseMapper.selectPage(page, wrapper);
        // 处理结果集
        PageResult<CourseBase> result = new PageResult<>(pageResult.getRecords(), pageResult.getTotal(),
                pageParams.getPageNo(), pageParams.getPageSize());

        return result;
    }
}
