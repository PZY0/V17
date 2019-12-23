package com.qianfeng.v17miaosha.controller;

import com.qianfeng.v17miaosha.pojo.ResultBean;
import com.qianfeng.v17miaosha.service.IRemindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author pangzhenyu
 * @Date 2019/11/27
 */
@Controller
@RequestMapping("remind")
public class RemindController {

    @Autowired
    private IRemindService remindService;

    @RequestMapping("add/{sekillId}/{userId}")
    public ResultBean add(@PathVariable Long sekillId,@PathVariable Long userId){

        return null;
    }
}
