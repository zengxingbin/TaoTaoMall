package com.taotao.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;
/**
 * 订单处理Service
 * @author Administrator
 *
 */
@Service
public class OrderServiceImpl implements OrderService {
    //注入jedisClient
    @Autowired
    private JedisClient jedisClient;
    @Value("${ORDER_GEN_KEY}")
    private String ORDER_GEN_KEY;
    @Value("${ORDER_GEN_KEY_BEGIN_VALUE}")
    private String ORDER_GEN_KEY_BEGIN_VALUE;
    @Value("${ORDERITEM_GEN_KEY}")
    private String ORDERITEM_GEN_KEY;
    //注入mapper
    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;
    @Autowired
    private TbOrderShippingMapper TbOrderShippingMapper;
    @Autowired
    private TbOrderMapper tbOrderMapper;
    @Override
    public TaotaoResult createOrder(OrderInfo orderInfo) {
        //生成订单号,由redis的incr方法来生成
        if(!jedisClient.exists(ORDER_GEN_KEY)) {
            //一开始key不存在，创建key并赋予初始值
            jedisClient.set(ORDER_GEN_KEY, ORDER_GEN_KEY_BEGIN_VALUE);
        }
        String orderId = jedisClient.incr(ORDER_GEN_KEY).toString();
        //设置订单号
        orderInfo.setOrderId(orderId);
        //设置邮费，免邮
        orderInfo.setPostFee("0");
        //设置订单状态
        orderInfo.setStatus(1);//1表示未付款
        //设置订单生成时间和更新时间
        orderInfo.setCreateTime(new Date());
        orderInfo.setUpdateTime(new Date());
        //获取订单明细列表，插入到数据库中
        List<TbOrderItem> orderItems = orderInfo.getOrderItems();
        for(TbOrderItem tbOrderItem : orderItems) {
            //补全pojo属性,设置订单明细表Id
            String oid = jedisClient.incr(ORDERITEM_GEN_KEY).toString();
            tbOrderItem.setId(oid);
            //设置订单号
            tbOrderItem.setOrderId(orderId);
            //插入到数据库中
            tbOrderItemMapper.insert(tbOrderItem);
        }
        //获取订单物流配送信息对象
        TbOrderShipping orderShipping = orderInfo.getOrderShipping();
        //补全属性
        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(new Date());
        orderShipping.setUpdated(new Date());
        //插入到数据库中
        TbOrderShippingMapper.insert(orderShipping);
        //将订单表插入到数据库中
        tbOrderMapper.insert(orderInfo);
        //返回TaoTaoResult，并将订单号返回
        return TaotaoResult.ok(orderId);
    }


}
