package com.qianfeng.v17limit.controller;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author pangzhenyu
 * @Date 2019/11/25
 */
@Controller
@RequestMapping("limit")
public class LimitConfigCongtroller {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("config")
    @ResponseBody
    public String config(Double sum){
        rabbitTemplate.setConfirmCallback(confirmCallback);
        CorrelationData correlationData = new CorrelationData("666");
        rabbitTemplate.convertAndSend("order-exchange","limitNum",sum,correlationData);
        return "success";
    }

    //回调函数: confirm确认
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {

            System.err.println("ack: " + ack);
            if(ack){
                System.out.println("说明消息正确送达MQ服务器");
                System.out.println("correlationData: " + correlationData.getId());
            }
        }
    };
}
