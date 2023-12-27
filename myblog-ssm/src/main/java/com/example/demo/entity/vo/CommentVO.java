package com.example.demo.entity.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentVO {
    // 文章标题
    private String title;
    // 该文章评论
    private String content;
}
