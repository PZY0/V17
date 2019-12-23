package com.qianfeng.v17miaosha.test;

import java.util.Arrays;
import java.util.Scanner;

/**
 * @Author pangzhenyu
 * @Date 2019/12/18
 */
public class MyTest {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String next = sc.next();
        System.out.println(ASCIISort(next));
    }

    public static String ASCIISort(String str) {
        char[] test = new char[str.length()];
        StringBuilder sb = new StringBuilder();
        /*while (true) {
            String a = str;//直接读取这行当中的字符串。
            for (int i = 0; i < str.length(); i++) {
                //字符串处理每次读取一位。
                test[i] = a.charAt(i);
            }
            Arrays.sort(test);
            for (int i = 0; i < test.length; i++) {
                sb.append(test[i]);
            }
            String trim = sb.toString().trim();
            return trim;
        }*/
        char[] chars = str.toCharArray();
        Arrays.sort(chars);
        for (int i = 0; i < chars.length; i++) {
            sb.append(chars[i]);
        }
        return sb.toString();
    }
}
