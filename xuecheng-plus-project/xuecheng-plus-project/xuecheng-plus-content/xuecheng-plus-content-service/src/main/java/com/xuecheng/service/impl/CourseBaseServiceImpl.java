package com.xuecheng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.exception.ServiceException;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDTO;
import com.xuecheng.content.model.dto.EditCourseDTO;
import com.xuecheng.content.model.dto.QueryCourseParamsDTO;
import com.xuecheng.content.model.po.CourseBase;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.content.model.po.CourseCategory;
import com.xuecheng.content.model.po.CourseMarket;
import com.xuecheng.content.model.vo.CourseBaseInfoVO;
import com.xuecheng.mapper.CourseBaseMapper;
import com.xuecheng.mapper.CourseCategoryMapper;
import com.xuecheng.mapper.CourseMarketMapper;
import com.xuecheng.service.ICourseBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * <p>
 * 课程基本信息 服务实现类
 * </p>
 *
 */
@Slf4j
@Service
public class CourseBaseServiceImpl extends ServiceImpl<CourseBaseMapper, CourseBase> implements ICourseBaseService {

    @Autowired
    private CourseMarketMapper courseMarketMapper;

    @Autowired
    private CourseCategoryMapper courseCategoryMapper;

    /**
     * 课程信息查询
     * @param pageParams
     * @param queryCourseParamsDto
     * @return
     */
    @Override
    public PageResult<CourseBase> getCourseList(PageParams pageParams, QueryCourseParamsDTO queryCourseParamsDto) {
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

    @Transactional
    @Override
    public CourseBaseInfoVO createCourseBase(AddCourseDTO addCourseDTO, Long companyId) {
        // 新增课程基本信息
        CourseBase courseBase = new CourseBase();
        BeanUtils.copyProperties(addCourseDTO, courseBase);
        // 设置审核状态
        courseBase.setAuditStatus("202002");
        // 设置发布状态
        courseBase.setStatus("203001");
        // 设置机构id
        courseBase.setCompanyId(companyId);
        courseBase.setCreateDate(LocalDateTime.now());
        int result = this.baseMapper.insert(courseBase);
        if(result <= 0) {
            throw new ServiceException("新增课程失败！");
        }
        // 新增价格信息
        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(addCourseDTO, courseMarket);
        courseMarket.setId(courseBase.getId());
        result = saveCourseMarket(courseMarket);
        if(result <= 0) {
            throw new ServiceException("保存课程营销信息失败！");
        }

        // 查询课程基本信息返回
        return getCourseBaseInfo(courseBase.getId());
    }



    public CourseBaseInfoVO getCourseBaseInfo(Long courseId) {
        // 查询课程基本信息
        CourseBase courseBase = this.baseMapper.selectById(courseId);
        if(courseBase == null){
            return null;
        }
        // 查询课程营销信息
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        CourseBaseInfoVO courseBaseInfoVO = new CourseBaseInfoVO();
        BeanUtils.copyProperties(courseBase,courseBaseInfoVO);
        if(courseMarket != null){
            BeanUtils.copyProperties(courseMarket,courseBaseInfoVO);
        }
        // 查询分类名称
        CourseCategory courseCategoryBySt =
                courseCategoryMapper.selectById(courseBase.getSt());
        courseBaseInfoVO.setStName(courseCategoryBySt.getName());
        CourseCategory courseCategoryByMt =
                courseCategoryMapper.selectById(courseBase.getMt());
        courseBaseInfoVO.setMtName(courseCategoryByMt.getName());
        return courseBaseInfoVO;

    }

    private int saveCourseMarket(CourseMarket courseMarket) {
        // 参数校验
        if(!StringUtils.hasLength(courseMarket.getCharge())) {
            throw new ServiceException("收费规则没有选择！");
        }
        if(courseMarket.getCharge().equals("201001")) {
            if(courseMarket.getPrice() == null || courseMarket.getPrice() <= 0) {
                throw new ServiceException("收费价格非法！");
            }
        }
        // 查询课程营销信息
        CourseMarket courseMarketInfo =  courseMarketMapper.selectById(courseMarket.getId());
        if(courseMarketInfo == null) {
            // 新增
            return courseMarketMapper.insert(courseMarket);
        }else {
            // 更新
            BeanUtils.copyProperties(courseMarket, courseMarketInfo);
            courseMarketInfo.setId(courseMarket.getId());
            return courseMarketMapper.updateById(courseMarketInfo);
        }

    }


    @Override
    public CourseBaseInfoVO getCourseBaseById(Long courseId) {
        return getCourseBaseInfo(courseId);
    }

    @Override
    public CourseBaseInfoVO modifyCourseBase(EditCourseDTO editCourseDTO, Long companyId) {
        // 业务逻辑校验
        Long courseId = editCourseDTO.getId();
        CourseBase courseBase = this.baseMapper.selectById(courseId);
        if(courseBase == null) {
            throw new ServiceException("课程不存在！");
        }
        if(!companyId.equals(courseBase.getCompanyId())) {
            throw new ServiceException("修改失败，当前机构只能修改当前机构课程！");
        }

        BeanUtils.copyProperties(editCourseDTO, courseBase);
        courseBase.setChangeDate(LocalDateTime.now());

        this.baseMapper.updateById(courseBase);

        // 封装营销信息的数据
        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(editCourseDTO, courseMarket);
        // 持久化
        saveCourseMarket(courseMarket);
        // 查询课程信息
        return this.getCourseBaseInfo(courseId);

    }
}
