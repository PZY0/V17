package com.qianfeng.exchange;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author pangzhenyu
 * @Date 2019/11/6
 */
public class Myconsumer2 {
    private static final String EXCHANGE_NAME = "fanout_exchange";
    private static final String QUEUE_NAME = "fanout_exchange-queue2";
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
        final Channel channel = connection.createChannel();
        //限流，表示最多放行一个消息，只有处理完毕后，在放第二个
        channel.basicQos(1);
        //声明队列并绑定交换机
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"");
        //创建消费者对象
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                super.handleDelivery(consumerTag, envelope, properties, body);
                System.out.println("消费者2：接受到的消息是:"+new String(body));
                //手工确认模式，告知MQ服务器，消息已被处理
                //参数2表示是否批量确认
                //为true，则批量确认确认，比如envelope.getDeliveryTag()=10，会将1-10的消息都确认
                //为false，则只确认10
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };
        //消费者去监听队列
        //参数2：autoAck=true，自动确认模式
        //autoAck=false。手动确认模式，开发使用
        channel.basicConsume(QUEUE_NAME,false,consumer);
    }
}
