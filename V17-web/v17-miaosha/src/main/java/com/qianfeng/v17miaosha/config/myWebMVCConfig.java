package com.qianfeng.v17miaosha.config;

import com.qianfeng.v17miaosha.interceptor.LimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author pangzhenyu
 * @Date 2019/11/23
 */
@Configuration
public class myWebMVCConfig implements WebMvcConfigurer {

    @Autowired
    private LimitInterceptor limitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(limitInterceptor).addPathPatterns("/**");
    }
}
