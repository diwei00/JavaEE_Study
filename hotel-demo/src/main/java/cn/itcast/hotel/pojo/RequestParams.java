package cn.itcast.hotel.pojo;


import lombok.Data;

@Data
public class RequestParams {
    private String key;  // 用户输入搜索数据
    private Integer page; // 页码
    private Integer size; // 一页中显示的数量
    private String sortBy; // 排序规则
    private String city; // 城市
    private String brand; // 品牌
    private String starName; // 星级
    private Integer minPrice; // 最小价格
    private Integer maxPrice; // 最大价格
    private String location; // 地理坐标

}
