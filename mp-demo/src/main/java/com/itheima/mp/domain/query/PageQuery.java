package com.itheima.mp.domain.query;


import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "分页查询基础实体")
public class PageQuery {
    @ApiModelProperty("页码")
    private Long pageNumber;

    @ApiModelProperty("页数")
    private Long pageSize;

    @ApiModelProperty("排序字段")
    private String sortBy;

    @ApiModelProperty("是否升序")
    private Boolean isAsc;

    /**
     * 转换实体对象为Page
     * @param orders 排序规则
     * @return
     * @param <T>
     */
    public <T> Page<T> toMpPage(OrderItem ... orders) {
        // 分页条件
        Page<T> page = new Page<>(pageNumber, pageSize);
        // 排序条件
        if(StringUtils.hasLength(sortBy)) {
            // 用户指定
            page.addOrder(new OrderItem(sortBy, isAsc));
            return page;
        }
        // 用户手动指定排序字段
        if(orders != null) {
            page.addOrder(orders);
        }
        return page;
    }

    /**
     * 按照指定默认字段排序
     * @param defaultSortBy
     * @param isAsc
     * @return
     * @param <T>
     */
    public <T> Page<T> toMpPage(String defaultSortBy, boolean isAsc){
        return this.toMpPage(new OrderItem(defaultSortBy, isAsc));
    }

    /**
     * 默认按照创建时间排
     * @return
     * @param <T>
     */
    public <T> Page<T> toMpPageDefaultSortByCreateTimeDesc() {
        return toMpPage("create_time", false);
    }

    /**
     * 默认按照更新时间排
     * @return
     * @param <T>
     */
    public <T> Page<T> toMpPageDefaultSortByUpdateTimeDesc() {
        return toMpPage("update_time", false);
    }

}
