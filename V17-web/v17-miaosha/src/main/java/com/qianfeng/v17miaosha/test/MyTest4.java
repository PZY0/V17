package com.qianfeng.v17miaosha.test;

import java.util.Scanner;

/**
 * @Author pangzhenyu
 * @Date 2020/1/6
 */
public class MyTest4 {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String s = sc.next();
        String result = method(s);
        System.out.println(result);
    }

    private static String method(String s) {
        StringBuilder sb = new StringBuilder();
        int sum = 1;
        char c1 = s.charAt(0);
        for (int i = 1; i < s.length(); i++) {
            char c2 = s.charAt(i);
            if(c1 == c2){
                sum++;
                continue;
            }

            if(sum > 1){
                sb.append(c1).append(sum);
            }else{
                sb.append(c1);
            }

            c1 = c2;
            sum = 1;
        }
        if(sum > 1){
            sb.append(c1).append(sum);
        }else{
            sb.append(c1);
        }
        return sb.toString();
    }
}
