package com.qianfeng.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author pangzhenyu
 * @Date 2019/11/6
 */
public class Product {
    private static final String EXCHANGE_NAME = "topic_exchange";
    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("106.13.216.239");
        connectionFactory.setVirtualHost("/zs");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("zs");
        connectionFactory.setPassword("123");
        //得到连接
        Connection connection = connectionFactory.newConnection();
        //基于这个连接得到通道channal
        Channel channel = connection.createChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"topic");
        //发送消息到交换机
        String message = "华为p手机";
        channel.basicPublish(EXCHANGE_NAME,"huawei.p",null,message.getBytes());
        String message2 = "华为m手机";
        channel.basicPublish(EXCHANGE_NAME,"huawei.m",null,message2.getBytes());
        String message3 = "华为mate手机";
        channel.basicPublish(EXCHANGE_NAME,"huawei.p.mate",null,message3.getBytes());
        System.out.println("消息发送成功");
    }
}
