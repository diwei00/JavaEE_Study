package com.xuecheng.content.api;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDTO;
import com.xuecheng.content.model.dto.QueryCourseParamsDTO;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.vo.CourseBaseInfoVO;
import com.xuecheng.service.ICourseBaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(value = "课程信息管理接口",tags = "课程信息管理接口")
@RestController
public class CourseBaseInfoController {

    @Autowired
    private ICourseBaseService courseBaseService;

    @ApiOperation("课程查询接口")
    @PostMapping("/course/list")
    public PageResult<CourseBase> getCourseList(PageParams pageParams,
                                                @RequestBody(required = false) QueryCourseParamsDTO queryCourseParamsDto) {

        return courseBaseService.getCourseList(pageParams, queryCourseParamsDto);

    }

    @ApiOperation("新增课程基本信息")
    @PostMapping("/course")
    public CourseBaseInfoVO createCourseBase(@RequestBody AddCourseDTO addCourseDTO) {

        // todo: 机构id，用户登录之后获取
        Long companyId = 1232141425L;
        return courseBaseService.createCourseBase(addCourseDTO, companyId);

    }

}
