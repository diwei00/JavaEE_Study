package com.example.demo.entity.vo;

import com.example.demo.entity.ArticleEntity;
import com.example.demo.entity.UserEntity;
import lombok.Data;


// 查询文章正文 + username
@Data
public class ArticleInfoVO extends ArticleEntity {
    private String username;

    // 为了打印父类属性，重写toString，lombok只生成当前类属性的toString
    @Override
    public String toString() {
        return "ArticleInfoVO{" +
                "username='" + username + '\'' +
                "} " + super.toString();


    }
}
