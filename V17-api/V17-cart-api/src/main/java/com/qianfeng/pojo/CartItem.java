package com.qianfeng.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author pangzhenyu
 * @Date 2019/11/14
 * 存储在Redis
 */
@Data
public class CartItem implements Serializable ,Comparable<CartItem>{

    private Long productId;

    private Integer count;

    private Date updateTime;

    public int compareTo(CartItem o) {
        long result = o.getUpdateTime().getTime() - this.getUpdateTime().getTime();
        if(result == 0){
            return -1;
        }
        return (int) result;
    }
}
