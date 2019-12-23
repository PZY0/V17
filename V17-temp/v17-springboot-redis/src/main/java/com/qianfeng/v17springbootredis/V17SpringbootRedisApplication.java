package com.qianfeng.v17springbootredis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class V17SpringbootRedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(V17SpringbootRedisApplication.class, args);
    }

}
