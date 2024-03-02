package com.xuecheng.orders.service;

import com.xuecheng.messagesdk.model.po.MqMessage;
import com.xuecheng.orders.model.dto.AddOrderDto;
import com.xuecheng.orders.model.dto.PayStatusDto;
import com.xuecheng.orders.model.po.XcPayRecord;
import com.xuecheng.orders.model.vo.PayRecordVO;

public interface OrderService {

    /**
     *  创建商品订单
     * @param addOrderDto 订单信息
     * @return PayRecordVO 支付交易记录(包括二维码)
     */
    PayRecordVO createOrder(String userId, AddOrderDto addOrderDto);

    /**
     *  查询支付交易记录
     * @param payNo 交易记录号
     */
    XcPayRecord getPayRecordByPayno(String payNo);

    /**
     * 请求支付宝查询支付结果
     * @param payNo 支付记录 id
     * @return 支付记录信息
     */
    PayRecordVO queryPayResult(String payNo);

    /**
     * 保存支付状态
     * @param payStatusDto
     */
    void saveAliPayStatus(PayStatusDto payStatusDto) ;


    /**
     * 发送通知结果
     * @param message
     */
    void notifyPayResult(MqMessage message);


}
