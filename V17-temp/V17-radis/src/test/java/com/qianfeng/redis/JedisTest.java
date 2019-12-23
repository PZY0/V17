package com.qianfeng.redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @Author pangzhenyu
 * @Date 2019/11/8
 */
public class JedisTest {

    @Test
    public void jedisTest(){
        Jedis jedis = new Jedis("106.13.216.239",6379);
        jedis.auth("123");
        /*jedis.set("a","666");
        System.out.println(jedis.get("a"));*/
        for (int i = 0; i < 10; i++) {
            jedis.incrBy("b",100);
        }
        System.out.println(jedis.get("b"));
    }
}
