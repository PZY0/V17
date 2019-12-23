package com.qianfeng.v17miaosha.interceptor;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @Author pangzhenyu
 * @Date 2019/11/23
 */
@Component
@Slf4j
public class LimitInterceptor implements HandlerInterceptor {

    //private RateLimiter rateLimiter = RateLimiter.create(LimitResult.createSum);
    @Autowired
    private RateLimiter rateLimiter;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean tryAcquire = rateLimiter.tryAcquire(500, TimeUnit.MILLISECONDS);
        if (!tryAcquire) {
            log.info("操作过于频繁，请稍后再试");
            return false;
        }
        //System.out.println("令牌数量"+LimitResult.createSum);
        System.out.println("令牌数量"+ rateLimiter.getRate());
        log.info("获取令牌");
        return true;
    }
}
