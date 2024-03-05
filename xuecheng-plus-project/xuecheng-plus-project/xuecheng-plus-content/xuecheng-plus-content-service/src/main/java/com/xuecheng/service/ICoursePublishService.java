package com.xuecheng.service;

import com.xuecheng.content.model.po.CoursePublish;
import com.xuecheng.content.model.vo.CoursePreviewVO;

import java.io.File;

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


    /**
     * 课程静态化
     * @param courseId 课程id
     * @return
     */
    File generateCourseHtml(Long courseId);

    /**
     * 上传课程静态化页面
     * @param courseId 课程id
     * @param file 静态化页面
     */
    void uploadCourseHtml(Long courseId,File file);

    CoursePublish getCoursePublish(Long courseId);

    /**
     *  查询缓存中的课程信息
     */
    CoursePublish getCoursePublishCache(Long courseId);
}
