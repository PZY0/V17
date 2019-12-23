package com.qianfeng.v17productservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.qianfeng.api.IProductTypeService;
import com.qianfeng.base.BaseServiceImpl;
import com.qianfeng.base.IBaseDao;
import com.qianfeng.entity.TProductType;
import com.qianfeng.mapper.TProductTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * @Author pangzhenyu
 * @Date 2019/11/1
 */
@Service
public class ProductTypeServiceImpl extends BaseServiceImpl<TProductType> implements IProductTypeService {

    @Autowired
    private TProductTypeMapper productTypeMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public IBaseDao<TProductType> getBaseDao() {
        return productTypeMapper;
    }

    @Override
    public List<TProductType> selectAll() {
        if(!redisTemplate.hasKey("producttype")){
            List<TProductType> productTypes = productTypeMapper.selectAll();
            Gson gson = new Gson();
            String json = gson.toJson(productTypes);
            redisTemplate.opsForValue().set("producttype",json);
            return productTypes;
        }
        Object obj = redisTemplate.opsForValue().get("producttype");
        Gson gson = new Gson();
        List<TProductType> list= gson.fromJson(obj.toString(), new TypeToken<List<TProductType>>() {}.getType());
        return list;
    }
}
