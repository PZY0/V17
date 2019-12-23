package com.qianfeng.v17center.config;

import com.qianfeng.MQConstant.MQConstant;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author pangzhenyu
 * @Date 2019/11/7
 */
@Configuration
public class RabbitConfig {
    //声明交换机
    @Bean
    public TopicExchange initProductExchange(){
        TopicExchange productExchange = new TopicExchange(MQConstant.EXCHANGE.CENTER_PRODUCT_EXCHANGE,true,false);
        return productExchange;
    }
}
