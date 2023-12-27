package com.example.demo.mapper;

import com.example.demo.entity.ArticleInfo;
import com.example.demo.entity.CommentInfo;
import com.example.demo.entity.vo.CommentInfoVO;
import com.example.demo.entity.vo.CommentVO;
import com.example.demo.entity.vo.ReceiveCommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {
    // 保存评论
    int addComment(CommentInfo commentInfo);

    // 获得文章评论
    List<CommentInfoVO> getComment(@Param("aid")Integer aid);

    List<ReceiveCommentVO> selectPublishCommentByUid(@Param("uid") Integer uid);

    List<ReceiveCommentVO> selectArticleByUid(@Param("uid") Integer uid);

    List<String> selectCommentByAid(@Param("aid") Integer aid);


}
