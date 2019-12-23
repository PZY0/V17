package com.qianfeng.v17smsservice.config;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author pangzhenyu
 * @Date 2019/11/12
 */
@Configuration
public class RabbitConfig {
    //声明队列
    @Bean
    public Queue initProductSearchQueue(){
        Queue queue = new Queue("sms-code-queue",true,false,false);
        return queue;

    }
    //声明交换机
    @Bean
    public TopicExchange initProductExchange(){
        TopicExchange productExchange = new TopicExchange("sms_exchange",true,false);
        return productExchange;
    }
    //队列绑定交换机
    @Bean
    public Binding bindProductQueueExchange(Queue initProductSearchQueue,TopicExchange initProductExchange){
        return BindingBuilder.bind(initProductSearchQueue).to(initProductExchange).with("sms.code");
    }
}
