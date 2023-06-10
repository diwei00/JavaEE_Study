package com.example.demo.service;

import com.example.demo.entity.ArticleInfo;
import com.example.demo.entity.CommentInfo;
import com.example.demo.entity.vo.ArticleInfoVO;
import com.example.demo.mapper.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    public int add(ArticleInfo articleInfo) {
        return articleMapper.add(articleInfo);
    }

    public ArticleInfo getDetailById(Integer id, Integer uid) {
        return articleMapper.getDetailById(id, uid);
    }

    public int update(ArticleInfo articleInfo) {
        return articleMapper.update(articleInfo);
    }

    public ArticleInfoVO getDetail(Integer id) {
        return articleMapper.getDetail(id);
    }
    public int increaseRCount(Integer id) {
        return articleMapper.increaseRCount(id);
    }
    public List<ArticleInfo> getListByUid(Integer uid, Integer pageSize, Integer offsetValue) {
        return articleMapper.getListByUid(uid, pageSize, offsetValue);
    }

    public int deleteById(Integer id, Integer uid) {
        return articleMapper.deleteById(id, uid);
    }

    public List<ArticleInfo> getListByPage(Integer pageSize, Integer offset) {
        return articleMapper.getListByPage(pageSize, offset);
    }
    public int getArtCount(Integer uid) {
        return articleMapper.getArtCount(uid);
    }

    public int getCount() {
        return articleMapper.getCount();
    }

    // 保存草稿
    public int saveDraft(ArticleInfo articleInfo) {
        return articleMapper.saveDraft(articleInfo);
    }

   public int addComment(CommentInfo commentInfo) {
        return articleMapper.addComment(commentInfo);
   }

   public List<CommentInfo> getComment(Integer aid) {
        return articleMapper.getComment(aid);
   }

   // 用于定时发布文章
   public int modifState(Integer id, LocalDateTime createtime) {
        return articleMapper.modifState(id, createtime);
   }


}
