package com.ben.config;

import com.ben.trigger.listener.OrderPaySuccessListener;
import com.google.common.eventbus.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 使用Guava作为监听消息，实际业务场景可以基于MQ消息
 * @create 2023-12-13 21:10
 */
@Configuration
public class GuavaConfig {

    @Bean
    public EventBus eventBusListener(OrderPaySuccessListener listener) {
        EventBus eventBus = new EventBus();
        eventBus.register(listener);
        return eventBus;
    }

}
