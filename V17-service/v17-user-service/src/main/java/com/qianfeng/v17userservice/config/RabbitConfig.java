package com.qianfeng.v17userservice.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author pangzhenyu
 * @Date 2019/11/12
 */
@Configuration
public class RabbitConfig {

    //声明交换机
    @Bean
    public TopicExchange initSMSExchange(){
        TopicExchange topicExchange = new TopicExchange("sms_exchange",true,false);
        return topicExchange;
    }


}
