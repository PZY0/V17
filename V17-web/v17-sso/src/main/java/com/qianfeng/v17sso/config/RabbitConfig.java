package com.qianfeng.v17sso.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author pangzhenyu
 * @Date 2019/11/16
 */
@Configuration
public class RabbitConfig {

    @Bean
    public TopicExchange initSMSExchange(){
        TopicExchange topicExchange = new TopicExchange("sso-exchange",true,false);
        return topicExchange;
    }
}
