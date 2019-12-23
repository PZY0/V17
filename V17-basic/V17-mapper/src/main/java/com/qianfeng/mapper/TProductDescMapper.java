package com.qianfeng.mapper;

import com.qianfeng.base.IBaseDao;
import com.qianfeng.entity.TProductDesc;

public interface TProductDescMapper extends IBaseDao<TProductDesc> {

    TProductDesc selectByProId(Long id);
}