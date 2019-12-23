package com.qianfeng.v17miaosha.service.impl;

import com.qianfeng.v17miaosha.entity.TMiaoshaProduct;
import com.qianfeng.v17miaosha.exception.MiaoshaException;
import com.qianfeng.v17miaosha.mapper.TMiaoshaProductMapper;
import com.qianfeng.v17miaosha.pojo.ResultBean;
import com.qianfeng.v17miaosha.service.IMiaoshaService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author pangzhenyu
 * @Date 2019/11/20
 */
@Service
public class MiaoshaServiceImpl implements IMiaoshaService {

    @Autowired
    private TMiaoshaProductMapper miaoshaProductMapper;

    private int count = 0;

    @Resource(name = "MyRedisTemplate")
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    @Cacheable(value = "product",key = "#id")
    public TMiaoshaProduct getById(Long id) {
        /*TMiaoshaProduct miaoshaProduct = (TMiaoshaProduct) redisTemplate.opsForValue().get("product-" + id);
        if(miaoshaProduct == null){
            miaoshaProduct = miaoshaProductMapper.selectByPrimaryKey(id);
            redisTemplate.opsForValue().set("product-"+id,miaoshaProduct);
        }
        return miaoshaProduct;*/
        return miaoshaProductMapper.selectByPrimaryKey(id);
    }

    /*@Override
    @Transactional
    public ResultBean kill(Long userId, Long productId) {
        TMiaoshaProduct product = miaoshaProductMapper.selectByPrimaryKey(productId);
        if(product.getCount()>0){
            product.setCount(product.getCount()-1);
            product.setUpdateTime(new Date());
            miaoshaProductMapper.updateByPrimaryKeySelective(product);
            System.out.println("抢购成功--"+(++count));
            return new ResultBean(200,"抢购成功");
        }
        return new ResultBean(404,"抢购失败");
    }*/
    @Override
    @Transactional
    public ResultBean kill(Long userId, Long id, String path) {
        //判断path是否合法
        String pathKey = new StringBuilder("miaosha-userId:").
                append(userId).append("-productId:").append(id).toString();
        if (!redisTemplate.hasKey(pathKey)) {
            throw new MiaoshaException("当前地址不合法");
        }
        //保证path一次性
        redisTemplate.delete(pathKey);

        //获取当前秒杀的商品
        //TMiaoshaProduct product = miaoshaProductMapper.selectByPrimaryKey(id);
        String keyInfo = new StringBuilder("miaosha-productInfo-").append(id).toString();
        TMiaoshaProduct product = (TMiaoshaProduct) redisTemplate.opsForValue().get(keyInfo);
        //判断活动是否开始
        if("0".equals(product.getMiaoshaStatus())){
            throw new MiaoshaException("当前活动尚未开启");
        }
        if("2".equals(product.getMiaoshaStatus())){
            throw new MiaoshaException("当前活动已结束");
        }

        String key = new StringBuilder("miaosha-user-").append(id).toString();
        Long add = redisTemplate.opsForSet().add(key, userId);
        if(add == 0){
            throw new MiaoshaException("无法重复抢购");
        }

        String killKey = new StringBuilder("miaosha-product-").append(id).toString();
        Long productId = (Long) redisTemplate.opsForList().leftPop(killKey);
        if(productId == null){
            redisTemplate.opsForSet().remove(key,userId);
            throw new MiaoshaException("当前商品已抢购一空");
        }

        //抢购成功，异步生成订单,userId,productId,count,orderNo
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmssSSSS");
        String orderNo = new StringBuilder(format.format(System.currentTimeMillis())).append(userId).toString();
        Map<String,Object> map = new HashMap<>();
        map.put("userId",userId);
        map.put("productId",id);
        map.put("count",1);
        map.put("orderNo",orderNo);
        map.put("price",product.getSalePrice());
        rabbitTemplate.convertAndSend("order-exchange","create.orderNo",map);
        return new ResultBean(200,orderNo);
    }

    @Override
    public ResultBean getPath(Long userId, Long id) {
        String keyInfo = new StringBuilder("miaosha-productInfo-").append(id).toString();
        TMiaoshaProduct product = (TMiaoshaProduct) redisTemplate.opsForValue().get(keyInfo);
        //判断活动是否开始
        if("0".equals(product.getMiaoshaStatus())){
            throw new MiaoshaException("当前活动尚未开启");
        }
        if("2".equals(product.getMiaoshaStatus())){
            throw new MiaoshaException("当前活动已结束");
        }
        //活动开启，先发起一次请求获取Path
        //动态生成path存到redis，设置有效时间
        String key = new StringBuilder("miaosha-userId:").
                append(userId).append("-productId:").append(id).toString();

        String path = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(key,path,5, TimeUnit.MINUTES);
        return new ResultBean(200,path);
    }
}
