package com.xuecheng.learning.service;

import com.xuecheng.base.model.PageResult;
import com.xuecheng.learning.model.dto.MyCourseTableParams;
import com.xuecheng.learning.model.dto.XcChooseCourseDto;
import com.xuecheng.learning.model.dto.XcCourseTablesDto;
import com.xuecheng.learning.model.po.XcCourseTables;

public interface MyCourseTablesService {

    /**
     * 用户选课
     * @param userId 用户id
     * @param courseId 课程id
     * @return
     */
    XcChooseCourseDto addChooseCourse(String userId, Long courseId);


    /**
     * 获取学习资格
     * @param userId 用户id
     * @param courseId 课程id
     * @return XcCourseTablesDto 学习资格状态
     * [{"code":"702001","desc":"正常学习"},
     * {"code":"702002","desc":"没有选课或选课后没有支付"},
     * {"code":"702003","desc":"已过期需要申请续期或重新支付"}]
     */
    XcCourseTablesDto getLearningStatus(String userId, Long courseId);

    boolean saveChooseCourseStauts(String choosecourseId);

    /**
     * 我的课程表查询
     * @param params 查询参数
     * @return
     */
    PageResult<XcCourseTables> mycourestabls(MyCourseTableParams params);
}
