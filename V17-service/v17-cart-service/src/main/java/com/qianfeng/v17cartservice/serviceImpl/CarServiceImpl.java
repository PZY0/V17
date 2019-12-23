package com.qianfeng.v17cartservice.serviceImpl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.qianfeng.api.ICartService;
import com.qianfeng.api.IProductService;
import com.qianfeng.pojo.CartItem;
import com.qianfeng.result.ResultBean;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author pangzhenyu
 * @Date 2019/11/14
 */
@Service
public class CarServiceImpl implements ICartService {

    @Resource(name = "MyRedisTemplate")
    private RedisTemplate<String,Object> redisTemplate;

    @Reference
    private IProductService productService;

    @Override
    public ResultBean add(String stoken, Long productId, Integer count) {
       StringBuilder key = new StringBuilder("user-cart-").append(stoken);
       //判断该商品是否存在
       if(redisTemplate.opsForHash().hasKey(key.toString(),productId)){
           CartItem cartItem = (CartItem) redisTemplate.opsForHash().get(key.toString(),productId);
           cartItem.setCount(cartItem.getCount()+count);
           cartItem.setUpdateTime(new Date());
           redisTemplate.opsForHash().put(key.toString(),productId,cartItem);
           //设置有效期
           redisTemplate.expire(key.toString(),15, TimeUnit.DAYS);
           return new ResultBean(200,true);
       }
       //商品不存在则创建
       CartItem cartItem = new CartItem();
       cartItem.setProductId(productId);
       cartItem.setCount(count);
       cartItem.setUpdateTime(new Date());
       redisTemplate.opsForHash().put(key.toString(),productId,cartItem);
       //设置有效期
       redisTemplate.expire(key.toString(),15, TimeUnit.DAYS);
       return new ResultBean(200,true);
    }

    @Override
    public ResultBean del(String stoken, Long productId) {
        StringBuilder key = new StringBuilder("user-cart-").append(stoken);
        Long delete = redisTemplate.opsForHash().delete(key.toString(), productId);
        if(delete>0){
            return new ResultBean(200,true);
        }
        return new ResultBean(500,false);
    }

    @Override
    public ResultBean update(String stoken, Long productId, Integer count) {
        StringBuilder key = new StringBuilder("user-cart-").append(stoken);
        CartItem cartItem = (CartItem) redisTemplate.opsForHash().get(key.toString(), productId);
        if(cartItem != null){
            cartItem.setCount(count);
            cartItem.setUpdateTime(new Date());
            redisTemplate.opsForHash().put(key.toString(),productId,cartItem);
            redisTemplate.expire(key.toString(),15,TimeUnit.MINUTES);
            return new ResultBean(200,true);
        }
        return  new ResultBean(404,false);
    }

    @Override
    public ResultBean query(String stoken) {
        StringBuilder key = new StringBuilder("user-cart-").append(stoken);
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key.toString());
        /*List<Long> ids = new ArrayList<>(entries.size());
        for(Object id : entries.keySet()){
            ids.add((Long) id);
        }
        List<TProduct> products = productService.batchQuery(ids);
        System.out.println("--------->"+products.size());
        for (TProduct product : products) {
            System.out.println();
        }*/
        //entries不会为空，无数据会返回大括号
        if(entries.size() > 0){
            System.out.println("entries-->"+entries);
            /*List<CartItem> list = new ArrayList<>(entries.size());
            for(Object value : entries.values()){
                list.add((CartItem) value);
            }*/
            Collection<Object> values = entries.values();
            TreeSet<CartItem> treeSet = new TreeSet<>();
            for (Object value : values) {
                treeSet.add((CartItem) value);
            }
            //treeSet转list
            List<CartItem> list = new ArrayList<>(treeSet);
            return new ResultBean(200,list);
        }
        return new ResultBean(404,null);
    }

    @Override
    public ResultBean merget(String nologin, String login) {
        String nologinKey = new StringBuilder("user-cart-").append(nologin).toString();
        String loginKey = new StringBuilder("user-cart-").append(login).toString();
        //判断未登录购物车是否存在
        Map<Object, Object> nologinCart = redisTemplate.opsForHash().entries(nologinKey);
        if(nologinCart.size()==0){
            return new ResultBean(200,"不用合并");
        }
        //判断登录购物车是否存在,到这一步，则未登录购物车存在
        Map<Object, Object> loginCart = redisTemplate.opsForHash().entries(loginKey);
        if(loginCart.size()==0){
            redisTemplate.opsForHash().putAll(loginKey,nologinCart);
            redisTemplate.delete(nologinKey);
            return new ResultBean(200,"合并成功");
        }
        //登录购物车存在
        Set<Map.Entry<Object, Object>> entries = nologinCart.entrySet();
        for (Map.Entry<Object, Object> entry : entries) {
            //entry.getKey()      productId
            //entry.getValue()    CartItem
            //判断登录购物车是否存在未登录购物车的商品
            CartItem cartItem = (CartItem) redisTemplate.opsForHash().get(loginKey, entry.getKey());
            if(cartItem != null){
                CartItem nologinCartItem = (CartItem) entry.getValue();
                cartItem.setCount(cartItem.getCount()+nologinCartItem.getCount());
                cartItem.setUpdateTime(new Date());
                redisTemplate.opsForHash().put(loginKey,cartItem.getProductId(),cartItem);
            }else{
                redisTemplate.opsForHash().put(loginKey,entry.getKey(),entry.getValue());
            }
        }
        //redisTemplate.expire(loginKey,15,TimeUnit.DAYS);
        redisTemplate.delete(nologinKey);
        return new ResultBean(200,"合并成功");
    }
}
