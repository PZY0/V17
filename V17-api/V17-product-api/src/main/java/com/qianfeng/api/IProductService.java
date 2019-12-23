package com.qianfeng.api;

import com.qianfeng.base.IBaseService;
import com.qianfeng.entity.TProduct;
import com.qianfeng.vo.ProductVO;

import java.util.List;

/**
 * @Author pangzhenyu
 * @Date 2019/10/28
 */
public interface IProductService extends IBaseService<TProduct> {
    /*PageInfo<TProduct> pageList(Integer pageIndex, Integer pageSize);*/

    Long add(ProductVO productVO);

    ProductVO selectById(Long id);

    int batchDel(List<Long> ids);

    void update(ProductVO productVO);

    //List<TProduct> batchQuery(List<Long> ids);
}
