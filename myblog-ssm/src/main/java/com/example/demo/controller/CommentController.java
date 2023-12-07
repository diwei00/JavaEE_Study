package com.example.demo.controller;

import com.example.demo.common.AjaxResult;
import com.example.demo.common.ApplicationVariable;
import com.example.demo.entity.CommentInfo;
import com.example.demo.entity.Userinfo;
import com.example.demo.entity.vo.CommentInfoVO;
import com.example.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
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
}
