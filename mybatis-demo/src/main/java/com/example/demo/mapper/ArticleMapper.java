package com.example.demo.mapper;

import com.example.demo.entity.vo.ArticleInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleMapper {

    // 查询文章详情
    ArticleInfoVO getDetail(@Param("id") Integer id);

    // 查询作者的文章
    List<ArticleInfoVO> getListByUid(@Param("uid") Integer uid);

    // 获得文章列表（id, title）
    List<ArticleInfoVO> getListByIdOrTitle(@Param("id") Integer id, @Param("title") String title);

    // 修改id=1的状态
    int updateState(@Param("state") Integer state, @Param("rcount") Integer rcount);

    // 根据文章id集合批量删除文章
    int delByIdList(List<Integer> idList);
}
