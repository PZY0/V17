package com.qianfeng.routing;

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
    private static final String EXCHANGE_NAME = "direct_exchange";
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
        channel.exchangeDeclare(EXCHANGE_NAME,"direct");
        //发送消息到交换机
        String message = "华为手机";
        channel.basicPublish(EXCHANGE_NAME,"huawei",null,message.getBytes());
        String message2 = "苹果手机";
        channel.basicPublish(EXCHANGE_NAME,"iphone",null,message2.getBytes());

        System.out.println("消息发送成功");
    }
}
