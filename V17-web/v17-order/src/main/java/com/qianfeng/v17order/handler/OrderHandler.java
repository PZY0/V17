package com.qianfeng.v17order.handler;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @Author pangzhenyu
 * @Date 2019/11/21
 */
@Component
public class OrderHandler {

    @RabbitListener(queues = "order-miaosha-queue")
    @RabbitHandler
    public void createOrder(Map<String,Object> map, Channel channel, Message message){
        System.out.println(map.get("orderNo"));
        try {
            Thread.sleep(3000);
            //处理完业务逻辑，手工确认消息已经消费
            long deliveryTag = message.getMessageProperties().getDeliveryTag();
            channel.basicAck(deliveryTag,false);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
