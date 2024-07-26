package com.ben.domain.alipay.model.aggregate;


import com.ben.domain.alipay.model.entity.OrderEntity;
import com.ben.domain.alipay.model.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderAggregate {

    /** 用户ID */
    private String userId;
    /** 用户ID */
    private ProductEntity productEntity;

    private OrderEntity orderEntity;

}
