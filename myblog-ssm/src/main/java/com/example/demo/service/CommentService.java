package com.example.demo.service;

import com.example.demo.entity.CommentInfo;
import com.example.demo.entity.vo.CommentInfoVO;
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
}
