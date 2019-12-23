package com.qianfeng.v17cart.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qianfeng.api.ICartService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author pangzhenyu
 * @Date 2019/11/16
 */
@Component
public class SSOHandler {

    @Reference
    private ICartService cartService;

    @RabbitListener(queues = "sso-cart-queue")
    @RabbitHandler
    public void process(Map<String,String> map){
        String nologinKey = map.get("nologinKey");
        String loginKey = map.get("loginKey");
        cartService.merget(nologinKey,loginKey);
    }
}
