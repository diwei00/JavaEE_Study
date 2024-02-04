package com.xuecheng.content.api;


import com.xuecheng.content.model.vo.CourseCategoryTreeVO;
import com.xuecheng.service.ICourseCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api("课程分类接口")
@RestController
public class CourseCategoryController {

    @Autowired
    private ICourseCategoryService courseCategoryService;


    @ApiOperation("课程分类查询接口")
    @GetMapping("/course-category/tree-nodes")
    public List<CourseCategoryTreeVO> queryTreeNodes() {
        return courseCategoryService.queryTreeNodes("1");
    }




}
