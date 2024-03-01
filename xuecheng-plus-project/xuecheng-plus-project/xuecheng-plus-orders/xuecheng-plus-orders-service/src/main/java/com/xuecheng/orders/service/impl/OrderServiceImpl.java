package com.xuecheng.orders.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.exception.ServiceException;
import com.xuecheng.base.utils.IdWorkerUtils;
import com.xuecheng.base.utils.QRCodeUtil;
import com.xuecheng.orders.config.AlipayConfig;
import com.xuecheng.orders.mapper.XcOrdersGoodsMapper;
import com.xuecheng.orders.mapper.XcOrdersMapper;
import com.xuecheng.orders.mapper.XcPayRecordMapper;
import com.xuecheng.orders.model.dto.AddOrderDto;
import com.xuecheng.orders.model.dto.PayStatusDto;
import com.xuecheng.orders.model.po.XcOrders;
import com.xuecheng.orders.model.po.XcOrdersGoods;
import com.xuecheng.orders.model.po.XcPayRecord;
import com.xuecheng.orders.model.vo.PayRecordVO;
import com.xuecheng.orders.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.springframework.aop.framework.AopContext.currentProxy;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private XcOrdersMapper ordersMapper;
    @Autowired
    private XcOrdersGoodsMapper ordersGoodsMapper;
    @Autowired
    private XcPayRecordMapper payRecordMapper;

    @Autowired
    private OrderService currentProxy;

    @Value("${pay.qrcodeurl}")
    private String qrcodeurl;

    @Value("${pay.alipay.APP_ID}")
    private String APP_ID;
    @Value("${pay.alipay.APP_PRIVATE_KEY}")
    private String APP_PRIVATE_KEY;
    @Value("${pay.alipay.ALIPAY_PUBLIC_KEY}")
    private String ALIPAY_PUBLIC_KEY;


    /**
     * 、
     * 创建订单
     *
     * @param userId      用户id
     * @param addOrderDto 订单信息
     * @return
     */
    @Transactional
    @Override
    public PayRecordVO createOrder(String userId, AddOrderDto addOrderDto) {
        //创建商品订单
        XcOrders orders = saveXcOrders(userId, addOrderDto);
        if (orders == null) {
            throw new ServiceException("订单创建失败");
        }
        if (orders.getStatus().equals("600002")) {
            throw new ServiceException("订单已支付");
        }
        //生成支付记录
        XcPayRecord payRecord = createPayRecord(orders);
        //生成二维码
        String qrCode = null;
        try {
            //url 要可以被模拟器访问到，url 为下单接口(稍后定义)
            String url = String.format(qrcodeurl, payRecord.getPayNo());
            qrCode = new QRCodeUtil().createQRCode(url, 200, 200);
        } catch (IOException e) {
            throw new ServiceException("生成二维码出错");
        }
        PayRecordVO payRecordDto = new PayRecordVO();
        BeanUtils.copyProperties(payRecord, payRecordDto);
        payRecordDto.setQrcode(qrCode);
        return payRecordDto;
    }

    @Override
    public XcPayRecord getPayRecordByPayno(String payNo) {
        XcPayRecord xcPayRecord = payRecordMapper.selectOne(
                new LambdaQueryWrapper<XcPayRecord>().eq(XcPayRecord::getPayNo, payNo));
        return xcPayRecord;
    }

    @Override
    public PayRecordVO queryPayResult(String payNo) {
        XcPayRecord payRecord = getPayRecordByPayno(payNo);
        if (payRecord == null) {
            throw new ServiceException("请重新点击支付获取二维码");
        }
        //支付状态
        String status = payRecord.getStatus();
        //如果支付成功直接返回
        if ("601002".equals(status)) {
            PayRecordVO payRecordVO = new PayRecordVO();
            BeanUtils.copyProperties(payRecord, payRecordVO);
            return payRecordVO;
        }
        //从支付宝查询支付结果
        PayStatusDto payStatusDto = queryPayResultFromAlipay(payNo);
        //保存支付结果
        currentProxy.saveAliPayStatus(payStatusDto);
        //重新查询支付记录
        payRecord = getPayRecordByPayno(payNo);
        PayRecordVO payRecordVO = new PayRecordVO();
        BeanUtils.copyProperties(payRecord, payRecordVO);
        return payRecordVO;
    }

    /**
     * 请求支付宝查询支付结果
     *
     * @param payNo 支付交易号
     * @return 支付结果
     */
    public PayStatusDto queryPayResultFromAlipay(String payNo) {

        //========请求支付宝查询支付结果=============
        AlipayClient alipayClient = new DefaultAlipayClient(
                AlipayConfig.URL, APP_ID, APP_PRIVATE_KEY, "json",
                AlipayConfig.CHARSET, ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE); //获得初始化的 AlipayClient
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", payNo);
        request.setBizContent(bizContent.toString());
        AlipayTradeQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
            if (!response.isSuccess()) {
                throw new ServiceException("请求支付查询查询失败");
            }
        } catch (AlipayApiException e) {
            log.error("请求支付宝查询支付结果异常:{}", e.toString(), e);
            throw new ServiceException("请求支付查询查询失败");
        }
        //获取支付结果
        String resultJson = response.getBody();
        //转 map
        Map resultMap = JSON.parseObject(resultJson, Map.class);
        Map alipay_trade_query_response = (Map) resultMap.get("alipay_trade_query_response");
        //支付结果
        // 支付状态
        String trade_status = (String) alipay_trade_query_response.get("trade_status");
        // 支付总金额
        String total_amount = (String) alipay_trade_query_response.get("total_amount");
        // 交易流水号
        String trade_no = (String) alipay_trade_query_response.get("trade_no");
        //保存支付结果
        PayStatusDto payStatusDto = new PayStatusDto();
        payStatusDto.setOut_trade_no(payNo);
        payStatusDto.setTrade_status(trade_status);
        payStatusDto.setApp_id(APP_ID);
        payStatusDto.setTrade_no(trade_no);
        payStatusDto.setTotal_amount(total_amount);
        return payStatusDto;
    }

    /**
     * 保存支付宝支付结果到数据库
     *
     * @param payStatusDto 支付结果信息
     */
    public void saveAliPayStatus(PayStatusDto payStatusDto) {
        //支付流水号
        String payNo = payStatusDto.getOut_trade_no();
        XcPayRecord payRecord = getPayRecordByPayno(payNo);
        if (payRecord == null) {
            throw new ServiceException("支付记录找不到");
        }
        //支付结果
        String trade_status = payStatusDto.getTrade_status();
        log.debug("收到支付结果:{},支付记录:{}}", payStatusDto.toString(), payRecord.toString());
        if (trade_status.equals("TRADE_SUCCESS")) {
            //支付金额变为分
            Float totalPrice = payRecord.getTotalPrice() * 100; // 数据库
            Float total_amount = Float.parseFloat(payStatusDto.getTotal_amount()) * 100; // 支付宝
            //校验是否一致
            if (!payStatusDto.getApp_id().equals(APP_ID) ||
                    totalPrice.intValue() != total_amount.intValue()) {
                //校验失败
                log.info("校验支付结果失败,支付记 录:{},APP_ID:{},totalPrice:{}", payRecord.toString(), payStatusDto.getApp_id(), total_amount.intValue());
                throw new ServiceException("校验支付结果失败");
            }
            log.debug("更新支付结果,支付交易流水号:{},支付结果:{}", payNo, trade_status);
            XcPayRecord payRecord_u = new XcPayRecord();
            payRecord_u.setStatus("601002");//支付成功
            payRecord_u.setOutPayChannel("Alipay");
            payRecord_u.setOutPayNo(payStatusDto.getTrade_no());//支付宝交易号
            payRecord_u.setPaySuccessTime(LocalDateTime.now());//成功时间
            int update1 = payRecordMapper.update(payRecord_u,
                    new LambdaQueryWrapper<XcPayRecord>().eq(XcPayRecord::getPayNo, payNo));
            if (update1 > 0) {
                log.info("更新支付记录状态成功:{}", payRecord_u.toString());
            } else {
                log.info("更新支付记录状态失败:{}", payRecord_u.toString());
                throw new ServiceException("更新支付记录状态失败");
            }
            //关联的订单号
            Long orderId = payRecord.getOrderId();
            XcOrders orders = ordersMapper.selectById(orderId);
            if (orders == null) {
                log.info("根据支付记录[{}}]找不到订单", payRecord_u.toString());
                throw new ServiceException("根据支付记录找不到订单");
            }
            XcOrders order_u = new XcOrders();
            order_u.setStatus("600002");//支付成功
            int update = ordersMapper.update(order_u, new
                    LambdaQueryWrapper<XcOrders>().eq(XcOrders::getId, orderId));
            if (update > 0) {
                log.info("更新订单表状态成功,订单号:{}", orderId);
            } else {
                log.info("更新订单表状态失败,订单号:{}", orderId);
                throw new ServiceException("更新订单表状态失败");
            }
        }
    }


    // 保存订单信息
    @Transactional
    public XcOrders saveXcOrders(String userId, AddOrderDto addOrderDto) {
        //幂等性处理 选课记录表id
        XcOrders order = getOrderByBusinessId(addOrderDto.getOutBusinessId());
        if (order != null) {
            return order;
        }
        order = new XcOrders();
        //生成订单号
        long orderId = IdWorkerUtils.getInstance().nextId();
        order.setId(orderId);
        order.setTotalPrice(addOrderDto.getTotalPrice());
        order.setCreateDate(LocalDateTime.now());
        order.setStatus("600001");//未支付
        order.setUserId(userId);
        order.setOrderType(addOrderDto.getOrderType());
        order.setOrderName(addOrderDto.getOrderName());
        order.setOrderDetail(addOrderDto.getOrderDetail());
        order.setOrderDescrip(addOrderDto.getOrderDescrip());
        order.setOutBusinessId(addOrderDto.getOutBusinessId());//选课记录 id
        // 插入订单主表
        ordersMapper.insert(order);
        String orderDetailJson = addOrderDto.getOrderDetail();
        List<XcOrdersGoods> xcOrdersGoodsList = JSON.parseArray(orderDetailJson, XcOrdersGoods.class);
        xcOrdersGoodsList.forEach(goods -> {
            XcOrdersGoods xcOrdersGoods = new XcOrdersGoods();
            BeanUtils.copyProperties(goods, xcOrdersGoods);
            xcOrdersGoods.setOrderId(orderId);//订单号
            // 插入订单详情表，子表
            ordersGoodsMapper.insert(xcOrdersGoods);
        });
        return order;
    }

    // 创建支付交易记录
    public XcPayRecord createPayRecord(XcOrders orders) {
        if (orders == null) {
            throw new ServiceException("订单不存在");
        }
        if (orders.getStatus().equals("600002")) {
            throw new ServiceException("订单已支付");
        }
        XcPayRecord payRecord = new XcPayRecord();
        //生成支付交易流水号（雪花算法）
        long payNo = IdWorkerUtils.getInstance().nextId();
        payRecord.setPayNo(payNo);
        payRecord.setOrderId(orders.getId());//商品订单号
        payRecord.setOrderName(orders.getOrderName());
        payRecord.setTotalPrice(orders.getTotalPrice());
        payRecord.setCurrency("CNY");
        payRecord.setCreateDate(LocalDateTime.now());
        payRecord.setStatus("601001");//未支付
        payRecord.setUserId(orders.getUserId());
        payRecordMapper.insert(payRecord);
        return payRecord;
    }


    //根据业务 id 查询订单
    public XcOrders getOrderByBusinessId(String businessId) {
        XcOrders orders = ordersMapper.selectOne(
                new LambdaQueryWrapper<XcOrders>().eq(XcOrders::getOutBusinessId, businessId));
        return orders;
    }

}
