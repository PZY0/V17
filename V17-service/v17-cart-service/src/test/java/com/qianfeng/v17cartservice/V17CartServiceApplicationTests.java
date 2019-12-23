package com.qianfeng.v17cartservice;

import com.qianfeng.api.ICartService;
import com.qianfeng.pojo.CartItem;
import com.qianfeng.result.ResultBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class V17CartServiceApplicationTests {

    @Autowired
    private ICartService cartService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Test
    void addTest() {
        String stoken = "aaa";
        ResultBean add = cartService.add(stoken, 2L, 3);
        CartItem cartItem = (CartItem) redisTemplate.opsForHash().get("user-cart-aaa", 1L);
        System.out.println(cartItem);
        System.out.println(add.getData());
    }

    @Test
    void delTest() {
        ResultBean del = cartService.del("aaa", 1L);
        System.out.println(del.getData());
    }

    @Test
    void update(){
        ResultBean update = cartService.update("aaa", 1L, 6);
        System.out.println(update.getData());
        CartItem cartItem = (CartItem) redisTemplate.opsForHash().get("user-cart-aaa", 1L);
        System.out.println(cartItem);
    }

    @Test
    void query(){
        ResultBean query = cartService.query("aaa");
        System.out.println(query.getData());
    }
}
