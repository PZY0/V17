package com.qianfeng.v17register.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qianfeng.api.IUserService;
import com.qianfeng.entity.TUser;
import com.qianfeng.result.ResultBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Author pangzhenyu
 * @Date 2019/11/11
 */
@Controller
@RequestMapping("register")
public class UserController {

    @Reference
    private IUserService userService;

    @GetMapping("checkUserNameIsExists/{username}")
    @ResponseBody
    public ResultBean checkUserNameIsExists(@PathVariable String username){
        return null;
    }

    @GetMapping("checkPhoneIsExists/{phone}")
    @ResponseBody
    public ResultBean checkPhoneIsExists(@PathVariable String phone){
        return null;
    }

    @GetMapping("checkEmailIsExists/{email}")
    @ResponseBody
    public ResultBean checkEmailIsExists(@PathVariable String email){
        return null;
    }

    @PostMapping("generateCode/{identification}")
    @ResponseBody
    public ResultBean generateCode(@PathVariable String identification){
        return userService.generateCode(identification);
    }

    //异步方式
    @PostMapping("register")
    @ResponseBody
    public ResultBean register(TUser user){
        return userService.register(user);
    }

    //同步方式
    /*@PostMapping("register")
    public String register4PC(TUser user){
        return null;
    }*/

    @GetMapping("activating")
    public String activating(String token){
        return null;
    }
}
