package com.example.demo.service;

import com.example.demo.entity.ArticleInfo;
import com.example.demo.entity.CommentInfo;
import com.example.demo.entity.vo.CommentInfoVO;
import com.example.demo.entity.vo.CommentVO;
import com.example.demo.entity.vo.ReceiveCommentVO;
import com.example.demo.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;
    // 添加评论
    public int addComment(CommentInfo commentInfo) {
        return commentMapper.addComment(commentInfo);
    }

    // 获取评论
    public List<CommentInfoVO> getComment(Integer aid) {
        return commentMapper.getComment(aid);
    }

    public List<ReceiveCommentVO> selectCommentByUid(Integer uid) {
        return commentMapper.selectPublishCommentByUid(uid);
    }

    public List<ReceiveCommentVO> selectArticleByUid(Integer uid) {
        return commentMapper.selectArticleByUid(uid);
    }

    public List<String> selectCommentByAid(Integer aid) {
        return commentMapper.selectCommentByAid(aid);
    }


}
