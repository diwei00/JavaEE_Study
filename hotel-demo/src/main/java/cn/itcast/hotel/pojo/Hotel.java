package cn.itcast.hotel.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_hotel")  // 将数据库tb_hotel表和Hotel实体类进行映射
public class Hotel {
    @TableId(type = IdType.INPUT) // 声明数据库主键（防止主键名字不一致value熟悉， type设置主键生成策略）
    private Long id;
    private String name;
    private String address;
    private Integer price;
    private Integer score;
    private String brand;
    private String city;
    private String starName;
    private String business;
    private String longitude;
    private String latitude;
    private String pic;
}
