package com.qianfeng.util;

import java.util.Random;

/**
 * @Author pangzhenyu
 * @Date 2019/11/12
 */
public class CodeUtils {

    //生成验证码
    public static String generateCode(int len){
        int min = Double.valueOf(Math.pow(10, len - 1)).intValue();
        int num = new Random().nextInt(
                Double.valueOf(Math.pow(10, len + 1)).intValue() - 1) + min;
        return String.valueOf(num).substring(0,len);
    }
}
