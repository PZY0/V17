package com.qianfeng.v17productservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qianfeng.api.IProductService;
import com.qianfeng.base.BaseServiceImpl;
import com.qianfeng.base.IBaseDao;
import com.qianfeng.entity.TProduct;
import com.qianfeng.entity.TProductDesc;
import com.qianfeng.mapper.TProductDescMapper;
import com.qianfeng.mapper.TProductMapper;
import com.qianfeng.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author pangzhenyu
 * @Date 2019/10/28
 */
@Service
public class ProductServiceImpl extends BaseServiceImpl<TProduct> implements IProductService{

    @Autowired
    private TProductMapper productMapper;

    @Autowired
    private TProductDescMapper productDescMapper;

    @Override
    public IBaseDao<TProduct> getBaseDao() {
        return productMapper;
    }

    /*public PageInfo<TProduct> pageList(Integer pageIndex, Integer pageSize) {
        PageHelper.startPage(pageIndex,pageSize);
        List<TProduct> list = getBaseDao().selectAll();
        return new PageInfo<TProduct>(list,3);
    }*/

    @Override
    @Transactional
    public Long add(ProductVO productVO) {
        productMapper.insertSelective(productVO.getProduct());
        TProductDesc productDesc = new TProductDesc();
        productDesc.setProductDesc(productVO.getProductdesc());
        productDesc.setProductId(productVO.getProduct().getId());
        productDescMapper.insertSelective(productDesc);
        return productVO.getProduct().getId();
    }

    @Override
    public ProductVO selectById(Long id) {
        TProduct product = productMapper.selectByPrimaryKey(id);
        TProductDesc productDesc = productDescMapper.selectByProId(id);
        //TProductDesc productDesc = productDescMapper.selectByPrimaryKey(id);
        ProductVO productVO = new ProductVO();
        productVO.setProduct(product);
        productVO.setProductdesc(productDesc.getProductDesc());
        return productVO;
    }

    @Override
    public int batchDel(List<Long> ids) {
        return productMapper.batchDel(ids);
    }

    @Override
    public void update(ProductVO productVO) {
        productMapper.updateByPrimaryKeySelective(productVO.getProduct());
        TProductDesc productDesc = productDescMapper.selectByProId(productVO.getProduct().getId());
        productDesc.setProductDesc(productVO.getProductdesc());
        productDescMapper.updateByPrimaryKeySelective(productDesc);
    }

    /*@Override
    public List<TProduct> batchQuery(List<Long> ids) {
        return productMapper.batchQuery(ids);
    }*/
}
