package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleInfo {
    // 文章id
    private int id;
    // 标题
    private String title;
    // 正文
    private String content;
    // 设置时间格式 + 时区
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    // 创建时间
    private LocalDateTime createtime;
    // 更新时间
    private LocalDateTime updatetime;
    // 用户id
    private int uid;
    // 阅读量
    private int rcount;
    // 状态
    private int state;
}
