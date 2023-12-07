package com.example.demo.entity.vo;

import com.example.demo.entity.CommentInfo;
import lombok.Data;

/**
 * 用于评论区（需要显示用户名）
 */
@Data
public class CommentInfoVO extends CommentInfo {
    private String username;
}
