package com.ben.domain.alipay.service;

import com.ben.domain.alipay.model.entity.PayOrderEntity;
import com.ben.domain.alipay.model.entity.ShopCartEntity;

/**
 * @InterfaceName: IOrderService
 * @Description: 订单服务
 * @Author: benjieqiang
 * @LastChangeDate: 2024/7/26 2:22 PM
 * @Version: v1.0
 */
public interface IOrderService {

    /**
     * 通过购物车实体对象，创建支付单实体（用于支付）—— 所有的订单下单都从购物车开始触发
     *
     * @param shopCartEntity 购物车实体
     * @return 支付单实体
     */
    PayOrderEntity createOrder(ShopCartEntity shopCartEntity) throws Exception;

    /**
     * 更新订单状态
     * @param orderId 订单ID
     */
    void updateOrderPaySuccess(String orderId);

}

