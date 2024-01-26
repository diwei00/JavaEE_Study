package com.itheima.mp.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 吴浩
 * @since 2024-01-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("address")
@ApiModel(value="Address对象", description="")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户ID")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "省")
    @TableField("province")
    private String province;

    @ApiModelProperty(value = "市")
    @TableField("city")
    private String city;

    @ApiModelProperty(value = "县/区")
    @TableField("town")
    private String town;

    @ApiModelProperty(value = "手机")
    @TableField("mobile")
    private String mobile;

    @ApiModelProperty(value = "详细地址")
    @TableField("street")
    private String street;

    @ApiModelProperty(value = "联系人")
    @TableField("contact")
    private String contact;

    @ApiModelProperty(value = "是否是默认 1默认 0否")
    @TableField("is_default")
    private Boolean isDefault;

    @ApiModelProperty(value = "备注")
    @TableField("notes")
    private String notes;

    @ApiModelProperty(value = "逻辑删除")
    @TableField("deleted")
    private Boolean deleted;


}
