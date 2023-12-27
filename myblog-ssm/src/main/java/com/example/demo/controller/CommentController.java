package com.example.demo.controller;

import com.example.demo.common.AjaxResult;
import com.example.demo.common.ApplicationVariable;
import com.example.demo.entity.ArticleInfo;
import com.example.demo.entity.CommentInfo;
import com.example.demo.entity.Userinfo;
import com.example.demo.entity.vo.CommentInfoVO;
import com.example.demo.entity.vo.CommentVO;
import com.example.demo.entity.vo.ReceiveCommentVO;
import com.example.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    /**
     * 添加评论
     * @param commentInfo 评论对象
     * @param request
     * @return
     */
    @RequestMapping("/add_comment")
    public AjaxResult addComment(CommentInfo commentInfo, HttpServletRequest request) {
        // 非空校验
        if (!StringUtils.hasLength(commentInfo.getContent()) || commentInfo.getAid() <= 0) {
            return AjaxResult.fail(-1, "参数错误！");

        }
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(ApplicationVariable.SESSION_KEY_USERINFO) == null) {
            return AjaxResult.fail(-2, "用户未登录！");

        }
        Userinfo userinfo = (Userinfo) session.getAttribute(ApplicationVariable.SESSION_KEY_USERINFO);
        commentInfo.setUid(userinfo.getId());
        commentInfo.setCreatetime(LocalDateTime.now());
        int result = commentService.addComment(commentInfo);
        return AjaxResult.success(result);
    }

    /**
     * 获取评论
     * @param aid 文章id
     * @return
     */
    @RequestMapping("/get_comment")
    public AjaxResult getComment(Integer aid) {
        if (aid == null || aid <= 0) {
            return AjaxResult.fail(-1, "参数错误！");
        }
        List<CommentInfoVO> list = commentService.getComment(aid);
        return AjaxResult.success(list);
    }

    /**
     * 获取 发布评论 & 接收评论
     * @param request
     * @return
     */
    @RequestMapping("/get_publish_receive_comment")
    public AjaxResult getPublishComment(HttpServletRequest request) {
       HttpSession session =  request.getSession(false);
        if (session == null || session.getAttribute(ApplicationVariable.SESSION_KEY_USERINFO) == null) {
            return AjaxResult.fail(-2, "用户未登录！");

        }
        Userinfo userinfo = (Userinfo) session.getAttribute(ApplicationVariable.SESSION_KEY_USERINFO);
        // 发布的评论
        List<ReceiveCommentVO> publishCommentList = commentService.selectCommentByUid(userinfo.getId());
        if(!CollectionUtils.isEmpty(publishCommentList)) {
            // 处理评论 和 标题
            for(ReceiveCommentVO commentVO : publishCommentList) {
                if(commentVO.getTitle().length() > 6) {
                    commentVO.setTitle(commentVO.getTitle().substring(0, 7) + "...");
                }
                if(commentVO.getContent().length() > 6) {
                    commentVO.setContent(commentVO.getContent().substring(0, 7) + "...");
                }
            }
        }
        // 接收评论
        List<ReceiveCommentVO> receiveCommentList = new ArrayList<>();
        List<ReceiveCommentVO> articleInfoList = commentService.selectArticleByUid(userinfo.getId());
        if(!CollectionUtils.isEmpty(articleInfoList)) {
            for(ReceiveCommentVO article : articleInfoList) {
                ReceiveCommentVO commentVO = new ReceiveCommentVO();
                commentVO.setUserName(article.getUserName());
                // 查询该文章评论
                List<String> contents = commentService.selectCommentByAid(article.getId());
                // 处理评论
                StringBuilder contentStr = new StringBuilder();
                for(String content : contents) {
                    contentStr.append(content);
                }
                if(contentStr.length() > 6) {
                    commentVO.setContent(contentStr.substring(0, 7) + "...");
                }else {
                    commentVO.setContent(contentStr.toString());
                }
                // 处理标题
                if(article.getTitle().length() > 6) {
                    commentVO.setTitle(article.getTitle().substring(0, 7) + "...");
                }else {
                    commentVO.setTitle(article.getTitle());
                }
                receiveCommentList.add(commentVO);

            }
        }
        // 处理结果
        List<List<ReceiveCommentVO>> result = new ArrayList<>();
        result.add(publishCommentList);
        result.add(receiveCommentList);
        return AjaxResult.success(result);
    }
}
