package com.ben.domain.alipay.model.entity;

import com.ben.domain.alipay.model.valobj.OrderStatusVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: benjieqiang
 * @CreateTime: 2024-07-26  14:24
 * @Description: 订单实体
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity {

    /**
     * 商品ID
     */
    private String productId;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 订单编号
     */
    private String orderId;
    /**
     * 下单时间
     */
    private Date orderTime;
    /**
     * 订单状态；create-创建完成、pay_wait-等待支付、pay_success-支付成功、deal_done-交易完成、close-订单关单
     */
    private OrderStatusVO orderStatus;
    /**
     * 订单金额
     */
    private BigDecimal totalAmount;
    /**
     * 支付信息，支付宝返回的form表单
     */
    private String payUrl;
}
