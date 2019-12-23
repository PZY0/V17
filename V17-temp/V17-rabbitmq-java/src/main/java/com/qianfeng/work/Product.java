package com.qianfeng.work;

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
    private static final String QUEUE_NAME = "work-queue";
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
        //声明队列
        //参数1：队列名称，如果该队列不存在，则创建，
        //参数2：是否为持久队列，true的话，重启服务器后队列可以保留下来
        //参数3：是否是独占队列，true的话，该队列仅限该连接使用
        //参数4：是否为自动删除队列，true的话，服务器不在使用后自动删除队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        for (int i = 0; i < 20; i++) {
            //发送消息到队列
            String message = "第"+i+"个生产者";
            //参数1：交换机，不写会使用默认的，参数2：如果没有交换机就写队列
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        }

        System.out.println("消息发送成功");
    }
}
