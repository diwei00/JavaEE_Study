package com.itheima.mp.domain.dto;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("分页结果")
public class PageDTO<T> {

    @ApiModelProperty("总条数")
    private Long total;
    @ApiModelProperty("总页数")
    private Long pages;
    @ApiModelProperty("数据集合")
    private List<T> list;

    public static <VO, PO> PageDTO<VO> empty(Page<PO> page){
        return new PageDTO<>(page.getTotal(), page.getPages(), Collections.emptyList());
    }


    /**
     * 将MybatisPlus分页结果转为 VO分页结果
     * @param page MybatisPlus的分页结果
     * @param voClass 目标VO类型的字节码
     * @param <VO> 目标VO类型
     * @param <PO> 原始PO类型
     * @return VO的分页对象
     */
    public static <VO, PO> PageDTO<VO> of(Page<PO> page, Class<VO> voClass) {
        // 获取数据集合
        List<PO> poList = page.getRecords();
        if(CollectionUtils.isEmpty(poList)) {
            return empty(page);
        }
        // 数据转换
        List<VO> voList = BeanUtil.copyToList(poList, voClass);

        return new PageDTO<>(page.getTotal(), page.getPages(), voList);
    }

    /**
     * 将MybatisPlus分页结果转为 VO分页结果，允许用户自定义PO到VO的转换方式
     * @param p MybatisPlus的分页结果
     * @param convertor PO到VO的转换函数
     * @param <V> 目标VO类型
     * @param <P> 原始PO类型
     * @return VO的分页对象
     */
    public static <V, P> PageDTO<V> of(Page<P> p, Function<P, V> convertor) {
        // 1.非空校验
        List<P> records = p.getRecords();
        if (records == null || records.size() <= 0) {
            // 无数据，返回空结果
            return empty(p);
        }
        // 2.数据转换
        List<V> vos = records.stream().map(convertor).collect(Collectors.toList());
        // 3.封装返回
        return new PageDTO<>(p.getTotal(), p.getPages(), vos);
    }


}
