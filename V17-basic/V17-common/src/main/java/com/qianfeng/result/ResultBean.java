package com.qianfeng.result;

import java.io.Serializable;

/**
 * @Author pangzhenyu
 * @Date 2019/10/30
 */
public class ResultBean<T> implements Serializable {
    //返回的状态码
    private Integer statusCode;
    //返回的数据
    private T data;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResultBean(Integer statusCode, T data) {
        this.statusCode = statusCode;
        this.data = data;
    }

    public ResultBean(){}
}
