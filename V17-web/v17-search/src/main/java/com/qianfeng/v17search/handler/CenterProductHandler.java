package com.qianfeng.v17search.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qianfeng.MQConstant.MQConstant;
import com.qianfeng.api.ISearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Author pangzhenyu
 * @Date 2019/11/7
 */
@Component
@RabbitListener(queues = MQConstant.QUEUE.CENTER_PRODUCT_EXCHANGE_SEARCH)
@Slf4j
public class CenterProductHandler {

    @Reference
    private ISearchService searchService;

    @RabbitHandler
    public void process(Long id){
        log.info("同步id{}",id);
        searchService.synById(id);
    }

    @RabbitHandler
    public void process(String del){
        //log.info("同步id{}",id);
        //searchService.synById(id);
        System.out.println("+++++++++++>"+Long.parseLong(del));
        searchService.delById(Long.parseLong(del));
    }
}
