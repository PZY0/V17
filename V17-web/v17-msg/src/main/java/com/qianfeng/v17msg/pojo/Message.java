package com.qianfeng.v17msg.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author pangzhenyu
 * @Date 2019/11/27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message<T> {

    private String msgType;

    private T data;
}
