package com.qianfeng.pojo;

import com.qianfeng.entity.TProduct;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author pangzhenyu
 * @Date 2019/11/14
 * 视图展示
 */
@Data
public class CartItemVO implements Serializable {

    private TProduct product;

    private Integer count;

    private Date updateTime;
}
