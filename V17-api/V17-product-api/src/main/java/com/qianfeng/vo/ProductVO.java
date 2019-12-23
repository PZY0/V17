package com.qianfeng.vo;

import com.qianfeng.entity.TProduct;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author pangzhenyu
 * @Date 2019/10/29
 */
@Data
public class ProductVO implements Serializable {
    private TProduct product;
    private String productdesc;
}
