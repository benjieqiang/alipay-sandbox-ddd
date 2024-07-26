package com.ben.infrastructure.persistent.dao;

import com.ben.infrastructure.persistent.po.PayOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IOrderDao {

    void insert(PayOrder order);

    PayOrder queryUnPayOrder(PayOrder order);

    void updateOrderPayInfo(PayOrder order);

    void updateOrderPaySuccess(PayOrder order);

}
