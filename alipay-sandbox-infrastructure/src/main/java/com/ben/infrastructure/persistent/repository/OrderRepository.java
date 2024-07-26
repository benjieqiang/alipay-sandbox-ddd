package com.ben.infrastructure.persistent.repository;


import com.ben.domain.alipay.model.aggregate.CreateOrderAggregate;
import com.ben.domain.alipay.model.entity.OrderEntity;
import com.ben.domain.alipay.model.entity.PayOrderEntity;
import com.ben.domain.alipay.model.entity.ProductEntity;
import com.ben.domain.alipay.model.entity.ShopCartEntity;
import com.ben.domain.alipay.model.valobj.OrderStatusVO;
import com.ben.domain.alipay.repository.IOrderRepository;
import com.ben.infrastructure.persistent.dao.IOrderDao;
import com.ben.infrastructure.persistent.po.PayOrder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * 订单仓储实现
 *
 * @author 小傅哥
 */
@Repository
public class OrderRepository implements IOrderRepository {

    @Resource
    private IOrderDao orderDao;

    @Override
    public OrderEntity queryUnPayOrder(ShopCartEntity shopCartEntity) {
        // 1. 封装参数
        PayOrder orderReq = new PayOrder();
        orderReq.setUserId(shopCartEntity.getUserId());
        orderReq.setProductId(shopCartEntity.getProductId());
        // 2. 查询到订单
        PayOrder order = orderDao.queryUnPayOrder(orderReq);
        if (null == order) return null;
        // 3. 返回结果
        return OrderEntity.builder()
                .productId(order.getProductId())
                .productName(order.getProductName())
                .orderId(order.getOrderId())
                .orderStatus(OrderStatusVO.valueOf(order.getStatus()))
                .orderTime(order.getOrderTime())
                .totalAmount(order.getTotalAmount())
                .payUrl(order.getPayUrl())
                .build();
    }

    @Override
    public ProductEntity queryProductByProductId(String productId) {
        // 实际场景中会从数据库查询
        return ProductEntity.builder()
                .productId(productId)
                .productName("测试商品")
                .productDesc("这是一个测试商品")
                .price(new BigDecimal("23.23"))
                .build();
    }

    @Override
    public void doSaveOrder(CreateOrderAggregate orderAggregate) {
        String userId = orderAggregate.getUserId();
        ProductEntity productEntity = orderAggregate.getProductEntity();
        OrderEntity orderEntity = orderAggregate.getOrderEntity();

        PayOrder order = new PayOrder();
        order.setUserId(userId);
        order.setProductId(productEntity.getProductId());
        order.setProductName(productEntity.getProductName());
        order.setOrderId(orderEntity.getOrderId());
        order.setOrderTime(orderEntity.getOrderTime());
        order.setTotalAmount(productEntity.getPrice());
        order.setStatus(orderEntity.getOrderStatus().getCode());

        orderDao.insert(order);
    }

    @Override
    public void updateOrderPayInfo(PayOrderEntity payOrderEntity) {
        PayOrder order = new PayOrder();
        order.setUserId(payOrderEntity.getUserId());
        order.setOrderId(payOrderEntity.getOrderId());
        order.setPayUrl(payOrderEntity.getPayUrl());
        order.setStatus(payOrderEntity.getOrderStatus().getCode());
        orderDao.updateOrderPayInfo(order);
    }

    @Override
    public void updateOrderPaySuccess(String orderId) {
        PayOrder order = new PayOrder();
        order.setOrderId(orderId);
        order.setStatus(OrderStatusVO.PAY_SUCCESS.getCode());
        orderDao.updateOrderPaySuccess(order);
    }

}
