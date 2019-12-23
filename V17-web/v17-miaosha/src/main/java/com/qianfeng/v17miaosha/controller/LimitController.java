package com.qianfeng.v17miaosha.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.qianfeng.v17miaosha.pojo.ResultBean;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.TimeUnit;

/**
 * @Author pangzhenyu
 * @Date 2019/11/23
 */
@Controller
@RequestMapping("limit")
public class LimitController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //每秒生成1个令牌
    private RateLimiter rateLimiter = RateLimiter.create(1);

    @RequestMapping("miaosha")
    @ResponseBody
    public String Limit(){
        //客户端从令牌桶获取令牌，如果500ms没有获取到令牌，则降级处理
        boolean tryAcquire = rateLimiter.tryAcquire(500, TimeUnit.MILLISECONDS);
        if(!tryAcquire){
            //降级处理
            System.out.println("系统繁忙");
            return "系统繁忙";
        }
        //获取到令牌，处理正常逻辑
        System.out.println("处理成功");
        return "处理成功";
    }

    @GetMapping("go")
    public String go(){
        return "limit";
    }

    @RequestMapping("sum")
    @ResponseBody
    public ResultBean limit(Integer sum){
        rabbitTemplate.convertAndSend("order-exchange","limitNum",sum);
        return new ResultBean(200,"successs");
    }

    public static void main(String[] args){
        RateLimiter rateLimiter = RateLimiter.create(10);
        System.out.println(rateLimiter.acquire(20));
        System.out.println(rateLimiter.acquire(30));
        System.out.println(rateLimiter.acquire(1));
        System.out.println(rateLimiter.acquire(1));
    }
}
