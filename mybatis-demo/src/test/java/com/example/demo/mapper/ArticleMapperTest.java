package com.example.demo.mapper;

import com.example.demo.entity.vo.ArticleInfoVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class ArticleMapperTest {

    @Autowired
    private ArticleMapper articleMapper;

    @Test
    void getDetail() {
        ArticleInfoVO articleInfoVO = articleMapper.getDetail(1);
        System.out.println(articleInfoVO);
    }

    @Test
    void getListByUid() {
        List<ArticleInfoVO> list = articleMapper.getListByUid(1);
        // 多线程遍历
        list.stream().parallel().forEach(System.out::println);
    }

    @Test
    void getListByIdOrTitle() {
        List<ArticleInfoVO> list = articleMapper.getListByIdOrTitle(null, null);
        list.stream().parallel().forEach(System.out::println);


    }

    @Transactional
    @Test
    void updateState() {
        int result = articleMapper.updateState(0, null);
        System.out.println(result);
    }

    @Transactional
    @Test
    void delByIdList() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        int result = articleMapper.delByIdList(list);
        System.out.println(result);
    }
}