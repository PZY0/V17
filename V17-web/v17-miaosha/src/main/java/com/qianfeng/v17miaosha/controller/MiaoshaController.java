package com.qianfeng.v17miaosha.controller;

import com.qianfeng.v17miaosha.entity.TMiaoshaProduct;
import com.qianfeng.v17miaosha.exception.MiaoshaException;
import com.qianfeng.v17miaosha.pojo.ResultBean;
import com.qianfeng.v17miaosha.service.IMiaoshaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author pangzhenyu
 * @Date 2019/11/20
 */
@Controller
@RequestMapping("miaosha")
public class MiaoshaController {

    @Autowired
    private IMiaoshaService miaoshaService;

    @GetMapping("get")
    public String getById(Long id, ModelMap map){
        TMiaoshaProduct product = miaoshaService.getById(id);
        map.put("product",product);
        return "kill";
    }

    //先发起一次请求获取path，前提用户已登陆，用户信息由服务端获取，而不是客户端
    @GetMapping("path")
    @ResponseBody
    public ResultBean getPath(Long userId,Long id){
        try {
            return miaoshaService.getPath(userId,id);
        }catch (MiaoshaException e){
            return new ResultBean(404,e.getMessage());
        }
    }

    //发起第二次请求抢购商品，验证path是否合法
    @GetMapping("kill/{path}")
    @ResponseBody
    public ResultBean kill(Long userId, @PathVariable String path){
        //Long userId = 1L;
        Long id = 1L;
        try {
            return miaoshaService.kill(userId,id,path);
        }catch (MiaoshaException e){
            return new ResultBean(404,e.getMessage());
        }
    }

    /*@GetMapping("kill")
    @ResponseBody
    public ResultBean kill(Long userId){
        //Long userId = 1L;
        Long id = 1L;
        try {
            return miaoshaService.kill(userId,id);
        }catch (MiaoshaException e){
            return new ResultBean(404,e.getMessage());
        }
    }*/
    @RequestMapping("msg")
    public String msg(){
        return "message";
    }
}
