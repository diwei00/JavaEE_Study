package com.xuecheng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.dto.BindTeachplanMediaDTO;
import com.xuecheng.content.model.dto.SaveTeachplanDTO;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import com.xuecheng.content.model.vo.TeachplanVO;

import java.util.List;

/**
 * <p>
 * 课程计划 服务类
 * </p>
 *
 */
public interface ITeachplanService extends IService<Teachplan> {

    List<TeachplanVO> getTreeNodes(Long courseId);

    void saveTeachplan(SaveTeachplanDTO teachplanDTO);


    TeachplanMedia associationMedia(BindTeachplanMediaDTO bindTeachplanMediaDto);
}
