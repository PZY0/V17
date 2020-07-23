package com.qianfeng.v17miaosha.test;

import java.util.Scanner;

/**
 * @Author pangzhenyu
 * @Date 2020/1/1
 */
public class MyTest2 {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String s = sc.next();
        String zero = s.replaceAll("zero", "0");
        String one = zero.replaceAll("one", "1");
        String two = one.replaceAll("two", "2");
        String three = two.replaceAll("three", "3");
        String four = three.replaceAll("four", "4");
        String five = four.replaceAll("five", "5");
        String six = five.replaceAll("six", "6");
        String seven = six.replaceAll("seven", "7");
        String eight = seven.replaceAll("eight", "8");
        String nine = eight.replaceAll("nine", "9");
        System.out.println(nine);
    }
}
