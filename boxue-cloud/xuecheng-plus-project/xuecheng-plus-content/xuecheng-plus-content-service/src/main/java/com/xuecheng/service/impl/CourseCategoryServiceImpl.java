package com.xuecheng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.content.model.po.CourseCategory;
import com.xuecheng.content.model.vo.CourseCategoryTreeVO;
import com.xuecheng.mapper.CourseCategoryMapper;
import com.xuecheng.service.ICourseCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程分类 服务实现类
 * </p>
 *
 */
@Slf4j
@Service
public class CourseCategoryServiceImpl extends ServiceImpl<CourseCategoryMapper, CourseCategory> implements ICourseCategoryService {

    @Autowired
    private CourseCategoryMapper categoryMapper;

    @Override
    public List<CourseCategoryTreeVO> queryTreeNodes(String id) {

        List<CourseCategoryTreeVO> courseCategorieList = categoryMapper.queryTreeNodes("1");
        // 将 list 转 map, 排除根节点 key: id   value: 对象
        Map<String, CourseCategoryTreeVO> mapTemp =
                courseCategorieList.stream().filter(item -> !id.equals(item.getId())).
                        collect(Collectors.toMap(key -> key.getId(), value -> value, (key1, key2) -> key2));

        // 结果集合
        List<CourseCategoryTreeVO> result = new ArrayList<>();
        courseCategorieList.stream().filter(item -> !id.equals(item.getId())).forEach(item -> {
            // 添加二级节点
            if(item.getParentid().equals(id)) {
                result.add(item);
            }

            // 获取父节点 todo: 此处可以修改result里数据
            CourseCategoryTreeVO courseCategoryTreeVO = mapTemp.get(item.getParentid());
            if(courseCategoryTreeVO != null) {
                if(courseCategoryTreeVO.getChildrenTreeNodes() == null) {
                    courseCategoryTreeVO.setChildrenTreeNodes(new ArrayList<>());
                }
                // 添加字节点
                courseCategoryTreeVO.getChildrenTreeNodes().add(item);

            }
        });

        return result;
    }
}
