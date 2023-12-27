package com.example.demo.entity.vo;


import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReceiveCommentVO extends CommentVO {
    private String userName;
    // 文章id
    private Integer id;
}
