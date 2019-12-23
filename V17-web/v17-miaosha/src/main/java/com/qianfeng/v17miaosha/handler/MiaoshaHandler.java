package com.qianfeng.v17miaosha.handler;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author pangzhenyu
 * @Date 2019/11/23
 */
@Component
public class MiaoshaHandler {

    /*@RabbitListener(queues = "limit-miaosha-queue")
    @RabbitHandler
    public void limitHandler(Integer sum){
        LimitResult.createSum = sum;
        System.out.println(LimitResult.createSum);
    }*/
    @Autowired
    private RateLimiter rateLimiter;

    @RabbitListener(queues = "limit-miaosha-queue")
    @RabbitHandler
    public void limitHandler(Double sum){
        rateLimiter.setRate(sum);
    }
}
