package com.qianfeng.mapper;

import com.qianfeng.base.IBaseDao;
import com.qianfeng.entity.TProduct;
import com.qianfeng.vo.ProductPartVO;

import java.util.List;

public interface TProductMapper extends IBaseDao<TProduct> {

    int batchDel(List<Long> ids);

    List<ProductPartVO> selectPart();

    ProductPartVO selectPartById(Long id);

    //List<TProduct> batchQuery(List<Long> ids);
}