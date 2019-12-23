package com.qianfeng.api;

import com.qianfeng.pojo.MessageResponse;

/**
 * @Author pangzhenyu
 * @Date 2019/11/12
 */
public interface ISMS {

    public MessageResponse sendSMS(String phone,String code);
}
