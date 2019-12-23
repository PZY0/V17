package com.qianfeng.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
@Data
//@AllArgsConstructor
public class TProductType implements Serializable {
    private Integer id;

    private Integer pid;

    private String name;

    //private Boolean flag;
    private Integer flag;

}