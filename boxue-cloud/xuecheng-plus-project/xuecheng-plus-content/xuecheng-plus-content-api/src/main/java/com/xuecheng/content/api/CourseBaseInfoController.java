package com.xuecheng.content.api;

import com.xuecheng.base.exception.ValidationGroups;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDTO;
import com.xuecheng.content.model.dto.EditCourseDTO;
import com.xuecheng.content.model.dto.QueryCourseParamsDTO;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.vo.CourseBaseInfoVO;
import com.xuecheng.service.ICourseBaseService;
import com.xuecheng.utile.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Api(value = "课程信息管理接口",tags = "课程信息管理接口")
@RestController
public class CourseBaseInfoController {

    @Autowired
    private ICourseBaseService courseBaseService;

    /**
     *
     * @param pageParams 分页参数
     * @param queryCourseParamsDto 查询数据
     * PreAuthorize注解：
     *    配置接口权限
     *    访问接口，携带Token，判断是否拥有该接口权限
     */
    @ApiOperation("课程查询接口")
    @PreAuthorize("hasAuthority('xc_teachmanager_course_list')")
    @PostMapping("/course/list")
    public PageResult<CourseBase> getCourseList(PageParams pageParams,
                                                @RequestBody(required = false) QueryCourseParamsDTO queryCourseParamsDto) {
        SecurityUtil.XcUser user = SecurityUtil.getUser();
        //机构 id
        String companyId = user.getCompanyId();


        return courseBaseService.getCourseList(Long.parseLong(companyId), pageParams, queryCourseParamsDto);

    }

    @ApiOperation("新增课程基本信息")
    @PostMapping("/course")
    public CourseBaseInfoVO createCourseBase(@RequestBody @Validated({ValidationGroups.Inster.class})
                                                 AddCourseDTO addCourseDTO) {
        // todo: 机构id，用户登录之后获取
        Long companyId = 1232141425L;
        return courseBaseService.createCourseBase(addCourseDTO, companyId);
    }

    @ApiOperation("根据课程 id 查询课程基础信息")
    @GetMapping("/course/{courseId}")
    public CourseBaseInfoVO getCourseBaseById(@PathVariable Long courseId){
        // SpringSecurity校验完成用户身份，会将jwt令牌中用户数据存储在当前线程上下文中
        // 这里可以获取到SpringSecurity上下文对象，获取用户数据
        // 取出当前用户身份
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SecurityUtil.XcUser user = SecurityUtil.getUser();
        System.out.println(user);
        return courseBaseService.getCourseBaseById(courseId);
    }

    @ApiOperation("修改课程信息")
    @PutMapping("/course")
    public CourseBaseInfoVO modifyCourseBase(@RequestBody @Validated EditCourseDTO editCourseDTO) {
        // todo: 机构id，用户登录之后获取
        return courseBaseService.modifyCourseBase(editCourseDTO, 1232141425L);

    }

}
