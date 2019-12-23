package com.qianfeng.v17miaosha;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class V17MiaoshaApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void contextLoads() {
        Integer sum = 5;
        rabbitTemplate.convertAndSend("order-exchange","limit-miaosha-queue",sum);
    }

    @Test
    void ttlTest(){
        rabbitTemplate.convertAndSend("","ttl-queue","1");
    }
}
