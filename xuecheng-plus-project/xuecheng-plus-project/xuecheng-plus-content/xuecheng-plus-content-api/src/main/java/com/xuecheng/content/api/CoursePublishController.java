package com.xuecheng.content.api;


import com.alibaba.fastjson.JSON;
import com.xuecheng.content.model.po.CoursePublish;
import com.xuecheng.content.model.vo.CourseBaseInfoVO;
import com.xuecheng.content.model.vo.CoursePreviewVO;
import com.xuecheng.content.model.vo.TeachplanVO;
import com.xuecheng.service.ICoursePublishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Api(value = "课程发布接口", tags = "课程发布接口")
@Controller
public class CoursePublishController {

    @Autowired
    private ICoursePublishService coursePublishService;

    /**
     * 课程预览接口
     *
     * @param courseId 课程id
     * @return
     */
    @ApiOperation("课程预览接口")
    @GetMapping("/coursepreview/{courseId}")
    public ModelAndView preview(@PathVariable("courseId") Long courseId) {
        //获取课程预览信息
        CoursePreviewVO coursePreviewInfo = coursePublishService.getCoursePreviewInfo(courseId);
        ModelAndView modelAndView = new ModelAndView();
        // 向模板塞数据
        modelAndView.addObject("model", coursePreviewInfo);
        // 模板
        modelAndView.setViewName("course_template");
        return modelAndView;
    }

    /**
     * 提交审核接口
     * @param courseId
     */
    @ResponseBody
    @PostMapping("/courseaudit/commit/{courseId}")
    public void commitAudit(@PathVariable("courseId") Long courseId){

        Long companyId = 1232141425L;
        coursePublishService.commitAudit(companyId,courseId);
    }

    @ApiOperation("课程发布")
    @ResponseBody
    @PostMapping ("/coursepublish/{courseId}")
    public void coursepublish(@PathVariable("courseId") Long courseId){
        Long companyId = 1232141425L;
        coursePublishService.publish(companyId,courseId);
    }

    @ApiOperation("查询课程发布信息")
    @ResponseBody
    @GetMapping("/r/coursepublish/{courseId}")
    public CoursePublish getCoursepublish(@PathVariable("courseId") Long courseId) {
        CoursePublish coursePublish = coursePublishService.getCoursePublish(courseId);
        return coursePublish;
    }

    @ApiOperation("获取课程发布信息")
    @ResponseBody
    @GetMapping("/course/whole/{courseId}")
    public CoursePreviewVO getCoursePublish(@PathVariable("courseId") Long courseId) {
        //查询课程发布信息
        CoursePublish coursePublish = coursePublishService.getCoursePublish(courseId);
        if (coursePublish == null) {
            return new CoursePreviewVO();
        }
        //课程基本信息
        CourseBaseInfoVO courseBase = new CourseBaseInfoVO();
        BeanUtils.copyProperties(coursePublish, courseBase);
        //课程计划
        List<TeachplanVO> teachplans = JSON.parseArray(coursePublish.getTeachplan(), TeachplanVO.class);
        CoursePreviewVO coursePreviewInfo = new CoursePreviewVO();
        coursePreviewInfo.setCourseBase(courseBase);
        coursePreviewInfo.setTeachplans(teachplans);
        return coursePreviewInfo;
    }
}
