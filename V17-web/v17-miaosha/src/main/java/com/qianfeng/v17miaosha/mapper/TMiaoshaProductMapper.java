package com.qianfeng.v17miaosha.mapper;

import com.qianfeng.v17miaosha.entity.TMiaoshaProduct;

import java.util.List;

public interface TMiaoshaProductMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TMiaoshaProduct record);

    int insertSelective(TMiaoshaProduct record);

    TMiaoshaProduct selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TMiaoshaProduct record);

    int updateByPrimaryKey(TMiaoshaProduct record);

    List<TMiaoshaProduct> getStartMiaoshaProduct();

    List<TMiaoshaProduct> getEndMiaoshaProduct();
}