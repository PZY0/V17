package com.qianfeng.v17order.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author pangzhenyu
 * @Date 2019/11/18
 */
@Controller
@RequestMapping("order")
public class OrderController {

    @RequestMapping("confirm")
    public String confirm(){

        return "confirm";
    }
}
