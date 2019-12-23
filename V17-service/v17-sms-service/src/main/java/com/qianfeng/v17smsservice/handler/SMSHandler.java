package com.qianfeng.v17smsservice.handler;

import com.qianfeng.api.ISMS;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author pangzhenyu
 * @Date 2019/11/7
 */
@Component
public class SMSHandler {

    @Autowired
    private ISMS isms;

    @RabbitListener(queues = "sms-code-queue")
    @RabbitHandler
    public void process(Map<String,String> map){
        isms.sendSMS(map.get("identification"),map.get("code"));
    }
}
