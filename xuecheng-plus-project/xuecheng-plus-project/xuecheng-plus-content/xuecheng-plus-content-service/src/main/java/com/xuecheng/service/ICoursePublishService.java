package com.xuecheng.service;

import com.xuecheng.content.model.vo.CoursePreviewVO;

public interface ICoursePublishService {
    /**
     * @description 获取课程预览信息
     * @param courseId 课程 id
     * @return com.xuecheng.content.model.dto.CoursePreviewDto
     */
    CoursePreviewVO getCoursePreviewInfo(Long courseId);

    /**
     * 课程提交审核
     * @param companyId 机构id
     * @param courseId 课程id
     */
    void commitAudit(Long companyId,Long courseId);


    /**
     * 课程发布
     * @param companyId 机构id
     * @param courseId 课程id
     */
    void publish(Long companyId,Long courseId);
}
