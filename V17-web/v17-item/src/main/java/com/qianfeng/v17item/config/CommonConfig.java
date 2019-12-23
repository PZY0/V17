package com.qianfeng.v17item.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author pangzhenyu
 * @Date 2019/11/5
 */
@Configuration
public class CommonConfig {

    @Bean
    public ThreadPoolExecutor initThreadPoolExecutor(){
        //查询电脑cpu核数
        int i = Runtime.getRuntime().availableProcessors();
        //自定义线程池
        ThreadPoolExecutor pool = new ThreadPoolExecutor(i, i * 2, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100));
        return pool;
    }
}
