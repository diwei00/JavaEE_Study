package com.xuecheng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.vo.TeachplanVO;

import java.util.List;

/**
 * <p>
 * 课程计划 Mapper 接口
 * </p>
 *
 */
public interface TeachplanMapper extends BaseMapper<Teachplan> {

    List<TeachplanVO> getTreeNodes(Long courseId);

}
