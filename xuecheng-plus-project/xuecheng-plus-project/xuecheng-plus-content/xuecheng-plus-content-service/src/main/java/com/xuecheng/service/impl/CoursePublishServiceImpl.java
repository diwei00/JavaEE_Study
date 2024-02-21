package com.xuecheng.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.utils.StringUtils;
import com.xuecheng.base.exception.ServiceException;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.po.CourseMarket;
import com.xuecheng.content.model.po.CoursePublishPre;
import com.xuecheng.content.model.vo.CourseBaseInfoVO;
import com.xuecheng.content.model.vo.CoursePreviewVO;
import com.xuecheng.content.model.vo.TeachplanVO;
import com.xuecheng.mapper.CourseBaseMapper;
import com.xuecheng.mapper.CourseMarketMapper;
import com.xuecheng.mapper.CoursePublishPreMapper;
import com.xuecheng.service.ICourseBaseService;
import com.xuecheng.service.ICoursePublishService;
import com.xuecheng.service.ITeachplanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CoursePublishServiceImpl implements ICoursePublishService {
    @Autowired
    private ICourseBaseService courseBaseService;
    @Autowired
    private ITeachplanService teachplanService;
    @Autowired
    private CourseBaseMapper courseBaseMapper;
    @Autowired
    private CourseMarketMapper courseMarketMapper;
    @Autowired
    private CoursePublishPreMapper coursePublishPreMapper;


    /**
     * 获取课程预览信息
     *
     * @param courseId 课程 id
     * @return
     */
    @Override
    public CoursePreviewVO getCoursePreviewInfo(Long courseId) {
        // 课程基本信息、营销信息
        CourseBaseInfoVO courseBaseInfo = courseBaseService.getCourseBaseInfo(courseId);
        // 课程计划信息
        List<TeachplanVO> teachplanTree = teachplanService.getTreeNodes(courseId);

        CoursePreviewVO coursePreviewDto = new CoursePreviewVO();
        coursePreviewDto.setCourseBase(courseBaseInfo);
        coursePreviewDto.setTeachplans(teachplanTree);
        return coursePreviewDto;
    }

    /**
     * 提交审核
     * @param companyId 机构id
     * @param courseId 课程id
     */
    @Transactional
    @Override
    public void commitAudit(Long companyId, Long courseId) {
        // 约束校验
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        // 课程审核状态
        String auditStatus = courseBase.getAuditStatus();
        // 当前审核状态为已提交不允许再次提交
        if ("202003".equals(auditStatus)) {
            throw new ServiceException("当前为等待审核状态，审核完成可以再次提交。");
        }
        // 本机构只允许提交本机构的课程
        if (!courseBase.getCompanyId().equals(companyId)) {
            throw new ServiceException("不允许提交其它机构的课程.");
        }
        // 课程图片是否填写
        if (StringUtils.isEmpty(courseBase.getPic())) {
            throw new ServiceException("提交失败，请上传课程图片");
        }
        // 添加课程预发布记录
        CoursePublishPre coursePublishPre = new CoursePublishPre();
        // 课程基本信息加部分营销信息
        CourseBaseInfoVO courseBaseInfo = courseBaseService.getCourseBaseInfo(courseId);
        BeanUtils.copyProperties(courseBaseInfo, coursePublishPre);
        // 课程营销信息
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        //转为 json
        String courseMarketJson = JSON.toJSONString(courseMarket);
        // 将课程营销信息 json 数据放入课程预发布表
        coursePublishPre.setMarket(courseMarketJson);
        // 查询课程计划信息
        List<TeachplanVO> teachplanTree = teachplanService.getTreeNodes(courseId);
        if (teachplanTree.size() <= 0) {
            throw new ServiceException("提交失败，还没有添加课程计划");
        }
        // 转 json
        String teachplanTreeString = JSON.toJSONString(teachplanTree);
        coursePublishPre.setTeachplan(teachplanTreeString);
        // 设置预发布记录状态,已提交
        coursePublishPre.setStatus("202003");
        // 教学机构 id
        coursePublishPre.setCompanyId(companyId);
        // 提交时间
        coursePublishPre.setCreateDate(LocalDateTime.now());
        coursePublishPre.setId(courseId);
        CoursePublishPre coursePublishPreUpdate = coursePublishPreMapper.selectById(courseId);
        if (coursePublishPreUpdate == null) {
            // 添加课程预发布记录
            coursePublishPreMapper.insert(coursePublishPre);
        } else {
            coursePublishPreMapper.updateById(coursePublishPre);
        }
        // 更新课程基本表的审核状态
        courseBase.setAuditStatus("202003");
        courseBaseMapper.updateById(courseBase);
    }
}
