package com.qianfeng.v17productservice;

import com.github.pagehelper.PageInfo;
import com.qianfeng.api.IProductService;
import com.qianfeng.api.IProductTypeService;
import com.qianfeng.entity.TProduct;
import com.qianfeng.entity.TProductType;
import com.qianfeng.vo.ProductVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class V17ProductServiceApplicationTests {

    @Autowired
    private IProductService productService;

    @Autowired
    private IProductTypeService productTypeService;

    @Test
    void contextLoads() {
        TProduct product = productService.selectByPrimaryKey(1L);
        System.out.println(product.getCreateTime());
    }

    @Test
    void pageList() {
        PageInfo<TProduct> page = productService.pageList(1, 1);
        for (TProduct product : page.getList()) {
            System.out.println(product.getName());
        }
    }

    @Test
    void add() {
        TProduct product = new TProduct();
        product.setName("格力手机");
        product.setPrice(3999L);
        product.setSalePoint("好用");
        product.setSalePrice(3666L);
        product.setImages("123");
        product.setTypeId(1);
        product.setTypeName("电子数码");
        ProductVO vo = new ProductVO();
        vo.setProduct(product);
        vo.setProductdesc("怎么摔都没问题");

        System.out.println(productService.add(vo));
    }

    @Test
    void typeList(){
        List<TProductType> productTypes = productTypeService.selectAll();
        for (TProductType productType : productTypes) {
            System.out.println(productType.getName());
        }
    }

}
