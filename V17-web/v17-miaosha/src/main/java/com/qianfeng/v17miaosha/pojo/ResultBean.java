package com.qianfeng.v17miaosha.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author pangzhenyu
 * @Date 2019/11/20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultBean<T> {
    //返回的状态码
    private Integer statusCode;
    //返回的数据
    private T data;
}
