package com.qianfeng.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class TProduct implements Serializable {
    private Long id;

    private String name;

    private Long price;

    private Long salePrice;

    private String images;

    private String salePoint;

    private Date createTime;

    private Date updateTime;

    private Integer typeId;

    private String typeName;

    private Byte flag;


}