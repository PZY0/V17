package com.qianfeng.v17miaosha.test;

import java.util.Scanner;

/**
 * @Author pangzhenyu
 * @Date 2020/1/1
 */
public class MyTest3 {
    public static void main(String[] args){
        Scanner s = new Scanner(System.in);
        System.out.print("请输入排成一圈的人数：");
        int n = s.nextInt();
        int result = method(n);
        System.out.println("原排在第" + result + "位的人留下了。");
    }

    private static int method(int n) {
        boolean[] arr = new boolean[n];

        for (int i = 0; i < arr.length; i++) {
            arr[i] = true;
        }

        int leftCount = n;
        int countNum = 0;
        int index = 0;
        int result = 0;
        while (leftCount > 1) {
            if (arr[index] == true) {
                countNum++;
                if (countNum == 3) {
                    countNum = 0;
                    arr[index] = false;
                    leftCount--;
                }
            }
            index++;
            if (index == n) {
                index = 0;
            }
        }
        for (int i = 0; i < n; i++) {
            if (arr[i] == true) {
                result = i+1;
                break;
            }
        }
        return result;
    }
}
