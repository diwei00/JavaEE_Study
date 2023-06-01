package com.example.demo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleEntity {
    private Integer id;
    private String title;
    private String content;
    private LocalDateTime createtime;
    private LocalDateTime updatetime;
    private Integer uid;
    private Integer rcount;
    private Integer state;

}
