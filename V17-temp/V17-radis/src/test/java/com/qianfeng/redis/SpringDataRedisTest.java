package com.qianfeng.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

/**
 * @Author pangzhenyu
 * @Date 2019/11/8
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:redis.xml")
public class SpringDataRedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void stringTest(){
        /*redisTemplate.opsForValue().set("d","ddd");
        System.out.println(redisTemplate.opsForValue().get("d"));*/
        redisTemplate.opsForHash().put("hash","a1","b1");
        System.out.println(redisTemplate.opsForHash().get("hash", "a1"));
    }

    @Test
    public void incrTest(){
        //redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().increment("a",334);
        //redisTemplate.opsForValue().increment("a",-334);
        System.out.println(redisTemplate.opsForValue().get("a"));
    }

    @Test
    public void batch(){
        /*long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            redisTemplate.opsForValue().set("k"+i,"v"+i);
        }
        long end = System.currentTimeMillis();
        System.out.println(end-start);*///20187
        long start = System.currentTimeMillis();
        redisTemplate.executePipelined(new SessionCallback<Object>() {
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                for (int i = 0; i < 1000; i++) {
                    redisOperations.opsForValue().set("k"+i,"v"+i);//281
                }
                return null;
            }
        });
        long end = System.currentTimeMillis();
        System.out.println(end-start);
    }

    @Test
    public void ttlTest(){
        redisTemplate.opsForValue().set("k1","v1");
        redisTemplate.expire("k1",1, TimeUnit.MINUTES);
        Long expire = redisTemplate.getExpire("k1");
        System.out.println(expire);
    }
}
