package com.example.demo.mapper;

import com.example.demo.entity.CommentInfo;
import com.example.demo.entity.vo.CommentInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {
    // 保存评论
    int addComment(CommentInfo commentInfo);

    // 获得文章评论
    List<CommentInfoVO> getComment(@Param("aid")Integer aid);
}
