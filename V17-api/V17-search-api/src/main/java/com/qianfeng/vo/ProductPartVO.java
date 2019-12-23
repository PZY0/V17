package com.qianfeng.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author pangzhenyu
 * @Date 2019/11/3
 */
@Data
//@AllArgsConstructor
public class ProductPartVO implements Serializable {
    private Long id;

    private String name;

    private Long price;

    private String images;

    private String salePoint;
}
