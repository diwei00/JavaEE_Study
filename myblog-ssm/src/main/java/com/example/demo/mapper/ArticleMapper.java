package com.example.demo.mapper;

import com.example.demo.entity.ArticleInfo;
import com.example.demo.entity.CommentInfo;
import com.example.demo.entity.vo.ArticleInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ArticleMapper {
    int add(ArticleInfo articleInfo);

    ArticleInfo getDetailById(@Param("id") Integer id, @Param("uid") Integer uid);

    int update(ArticleInfo articleInfo);

    ArticleInfoVO getDetail(@Param("id") Integer id);

    int increaseRCount(@Param("id") Integer id);

    List<ArticleInfo> getListByUid(@Param("uid") Integer uid, @Param("pageSize") Integer pageSize, @Param("offsetValue") Integer offsetValue);

    int deleteById(@Param("id") Integer id, @Param("uid") Integer uid);

    List<ArticleInfo> getListByPage(@Param("pageSize") Integer pageSize, @Param("offset") Integer offset);

    int getArtCount(@Param("uid") Integer uid);

    int getCount();

    int saveDraft(ArticleInfo articleInfo);


    // 修改定时发布文章状态码
    int modifState(@Param("id")Integer id, @Param("createtime")LocalDateTime createTime);

    List<ArticleInfo> getMyCatalogue(@Param("uid") Integer uid);

    List<ArticleInfo> getAllCatalogue();


}
