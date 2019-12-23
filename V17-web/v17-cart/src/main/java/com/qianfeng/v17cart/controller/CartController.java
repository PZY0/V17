package com.qianfeng.v17cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qianfeng.api.ICartService;
import com.qianfeng.api.IUserService;
import com.qianfeng.result.ResultBean;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @Author pangzhenyu
 * @Date 2019/11/15
 */
@RestController
@RequestMapping("cart")
public class CartController {

    @Reference
    private ICartService cartService;

    @Reference
    private IUserService userService;

    @GetMapping("add/{id}/{count}")
    public ResultBean add(
            @CookieValue(name = "cart-token",required = false)String cartToken,
            @PathVariable Long id, @PathVariable Integer count,
            HttpServletResponse response, HttpServletRequest request){
        //用户名
        String userToken = (String) request.getAttribute("user");
        //已登陆
        if(userToken != null){
            return cartService.add(userToken,id,count);
        }
        //未登录
        if(cartToken == null){
            cartToken = UUID.randomUUID().toString();
        }
        reflushcart(cartToken, response);

        return cartService.add(cartToken,id,count);
    }

    @GetMapping("query")
    public ResultBean query(
            @CookieValue(name = "cart-token",required = false)String token,
            HttpServletResponse response,HttpServletRequest request){
        //已登陆
        String userToken = (String) request.getAttribute("user");
        if(userToken != null){
            return cartService.query(userToken);
        }

        if(token != null){
            reflushcart(token,response);
            return cartService.query(token);
        }
        return new ResultBean(404,null);
    }

    @GetMapping("update/{id}/{count}")
    public ResultBean update(
            @CookieValue(name = "cart-token",required = false)String token,
            @PathVariable Long id, @PathVariable Integer count,
            HttpServletResponse response,HttpServletRequest request){
        //已登陆
        String userToken = (String) request.getAttribute("user");
        if(userToken != null){
            return cartService.update(userToken,id,count);
        }

        if(token != null){
            reflushcart(token,response);
            return cartService.update(token,id,count);
        }
        return new ResultBean(404,false);
    }

    @GetMapping("del/{id}")
    public ResultBean del(
            @CookieValue(name = "cart-token",required = false)String token,
            @PathVariable Long id,HttpServletRequest request){
        //已登陆
        String userToken = (String) request.getAttribute("user");
        if(userToken != null){
            return cartService.del(userToken,id);
        }

        if(token != null){
            return cartService.del(token,id);
        }
        return new ResultBean(404,null);
    }

    private void reflushcart(@CookieValue(name = "cart-token", required = false) String token, HttpServletResponse response) {
        Cookie cookie = new Cookie("cart-token",token);
        cookie.setMaxAge(15*24*60*60);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
