package com.qianfeng.v17miaosha.task;

import com.qianfeng.v17miaosha.entity.TMiaoshaProduct;
import com.qianfeng.v17miaosha.mapper.TMiaoshaProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author pangzhenyu
 * @Date 2019/11/20
 */
@Component
public class MiaoshaTask {

    @Autowired
    private TMiaoshaProductMapper miaoshaProductMapper;

    @Resource(name = "MyRedisTemplate")
    private RedisTemplate<String,Object> redisTemplate;

    @Resource(name = "MyLongRedisTemplate")
    private RedisTemplate<String,Long> longRedisTemplate;

    @Scheduled(cron = "0/5 * * * * ?")
    @Transactional
    public void task(){
        //查看当前可以开启秒杀活动的商品
        List<TMiaoshaProduct> list = miaoshaProductMapper.getStartMiaoshaProduct();
        if(list != null && !list.isEmpty()){
            for (TMiaoshaProduct product : list) {
                StringBuilder sb = new StringBuilder("miaosha-product-").append(product.getProductId());
                /*for (Integer i = 0; i < product.getCount(); i++) {
                    redisTemplate.opsForList().leftPush(sb.toString(),product.getProductId());
                }*/
                //流水线方式
                redisTemplate.executePipelined(new SessionCallback<Object>() {
                    @Override
                    public Object execute(RedisOperations redisOperations) throws DataAccessException {
                        for (Integer i = 0; i < product.getCount(); i++) {
                            redisTemplate.opsForList().leftPush(sb.toString(),product.getProductId());
                        }
                        return null;
                    }
                });
//                  方式2
//                Collection<Long> ids = new ArrayList<>(product.getCount());
//                for (Integer i = 0; i < product.getCount(); i++) {
//                    ids.add(product.getProductId());
//                }
//                longRedisTemplate.opsForList().leftPushAll(sb.toString(), ids);
                product.setMiaoshaStatus("1");
                miaoshaProductMapper.updateByPrimaryKeySelective(product);
                String key = new StringBuilder("miaosha-productInfo-").append(product.getId()).toString();
                redisTemplate.opsForValue().set(key,product);
            }
            System.out.println("初始化完毕");
        }
    }

    @Scheduled(cron = "0/5 * * * * ?")
    public void task2(){
        //查看当前可以开启秒杀活动的商品
        List<TMiaoshaProduct> list = miaoshaProductMapper.getEndMiaoshaProduct();
        if(list != null && !list.isEmpty()){
            for (TMiaoshaProduct product : list) {
                String killKey = new StringBuilder("miaosha-product-").append(product.getId()).toString();
                redisTemplate.delete(killKey);
                String killInfoKey = new StringBuilder("miaosha-productInfo-").append(product.getId()).toString();
                redisTemplate.delete(killInfoKey);

                product.setMiaoshaStatus("2");
                miaoshaProductMapper.updateByPrimaryKeySelective(product);
            }
            System.out.println("清理完毕");
        }
    }
}
