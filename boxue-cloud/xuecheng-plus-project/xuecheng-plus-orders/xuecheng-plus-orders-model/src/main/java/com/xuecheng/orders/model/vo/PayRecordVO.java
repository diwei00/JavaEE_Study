package com.xuecheng.orders.model.vo;

import com.xuecheng.orders.model.po.XcPayRecord;
import lombok.*;

/**
 * 支付记录dto
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class PayRecordVO extends XcPayRecord {

    //二维码
    private String qrcode;

}
