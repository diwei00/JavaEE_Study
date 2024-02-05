package com.xuecheng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.content.model.dto.SaveTeachplanDTO;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.vo.TeachplanVO;
import com.xuecheng.mapper.TeachplanMapper;
import com.xuecheng.service.ITeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 课程计划 服务实现类
 * </p>
 */
@Slf4j
@Service
public class TeachplanServiceImpl extends ServiceImpl<TeachplanMapper, Teachplan> implements ITeachplanService {
    @Autowired
    private TeachplanMapper teachplanMapper;

    @Override
    public List<TeachplanVO> getTreeNodes(Long courseId) {
        return teachplanMapper.getTreeNodes(courseId);
    }

    @Override
    public void saveTeachplan(SaveTeachplanDTO teachplanDTO) {
        // 课程计划 id
        Long id = teachplanDTO.getId();
        if (id != null) {
            // 更新课程计划
            Teachplan teachplan = teachplanMapper.selectById(id);
            BeanUtils.copyProperties(teachplanDTO, teachplan);
            teachplanMapper.updateById(teachplan);
        } else {
            // 新增课程计划
            // 取出同父同级别的课程计划数量
            int count = getTeachplanCount(teachplanDTO.getCourseId(), teachplanDTO.getParentid());
            Teachplan teachplanNew = new Teachplan();
            BeanUtils.copyProperties(teachplanDTO, teachplanNew);
            // 设置排序号
            teachplanNew.setOrderby(count + 1);
            teachplanMapper.insert(teachplanNew);
        }
    }

    /**
     * @param courseId 课程 id
     * @param parentId 父课程计划 id
     * @return int 最新排序号
     * @description 获取最新的排序号
     */
    private int getTeachplanCount(long courseId, long parentId) {
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getCourseId, courseId);
        queryWrapper.eq(Teachplan::getParentid, parentId);
        return teachplanMapper.selectCount(queryWrapper);
    }
}
