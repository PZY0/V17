package com.qianfeng.v17miaosha.service;

import com.qianfeng.v17miaosha.entity.TMiaoshaProduct;
import com.qianfeng.v17miaosha.pojo.ResultBean;

/**
 * @Author pangzhenyu
 * @Date 2019/11/20
 */
public interface IMiaoshaService {
    TMiaoshaProduct getById(Long id);

    ResultBean kill(Long userId, Long id, String path);

    ResultBean getPath(Long userId, Long id);
}
