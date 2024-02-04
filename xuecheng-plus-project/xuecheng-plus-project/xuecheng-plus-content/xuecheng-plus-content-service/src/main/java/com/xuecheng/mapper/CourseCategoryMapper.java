package com.xuecheng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuecheng.content.model.po.CourseCategory;
import com.xuecheng.content.model.vo.CourseCategoryTreeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 课程分类 Mapper 接口
 * </p>
 *
 */
public interface CourseCategoryMapper extends BaseMapper<CourseCategory> {

    List<CourseCategoryTreeVO> queryTreeNodes(@Param("id") String id);

}
