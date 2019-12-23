package com.qianfeng.api;

import com.qianfeng.result.ResultBean;

/**
 * @Author pangzhenyu
 * @Date 2019/11/14
 */
public interface ICartService {

    public ResultBean add(String stoken,Long productId,Integer count);

    public ResultBean del(String stoken,Long productId);

    public ResultBean update(String stoken,Long productId,Integer count);

    public ResultBean query(String stoken);

    public ResultBean merget(String nologinKey, String loginKey);
}
