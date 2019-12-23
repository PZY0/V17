package com.qianfeng.v17miaosha;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching//开启声明式缓存
@EnableScheduling
@SpringBootApplication
@MapperScan("com.qianfeng.v17miaosha.mapper")
public class V17MiaoshaApplication {

    public static void main(String[] args) {
        SpringApplication.run(V17MiaoshaApplication.class, args);
    }

}
