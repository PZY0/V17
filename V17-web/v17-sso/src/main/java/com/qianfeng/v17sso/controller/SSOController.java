package com.qianfeng.v17sso.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianfeng.api.IUserService;
import com.qianfeng.entity.TUser;
import com.qianfeng.result.ResultBean;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author pangzhenyu
 * @Date 2019/11/13
 */
@Controller
@RequestMapping("sso")
public class SSOController {

    @Reference
    private IUserService userService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //同步登录方式
    @PostMapping("checklogin")
    @ResponseBody
    public ResultBean checklogin(TUser user){
        return userService.checkByIdentification(user);
    }

    //同步登录方式
    @PostMapping("checklogin4PC")
    @ResponseBody
    public String checklogin4PC(TUser user, HttpServletResponse response,
                                @CookieValue(name = "cart-token",required = false)String cartToken){
        ResultBean resultBean = userService.checkByIdentification(user);
        if (200==resultBean.getStatusCode()){
            //request.getSession().setAttribute("user",user.getUsername());
            Map<String,String> datas = (Map<String, String>) resultBean.getData();
            String jwtToken = datas.get("jwtToken");
            Cookie cookie = new Cookie("user-token", jwtToken);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            Map<String,String> map = new HashMap<>();
            map.put("nologinKey",cartToken);
            map.put("loginKey",datas.get("username"));
            rabbitTemplate.convertAndSend("sso-exchange","user.login",map);
            //return "redirect:http://localhost:8091";
            return "登录成功";
        }
        return "index";
    }

    //检验是否登录
    @GetMapping("checkIsLogin")
    @CrossOrigin(origins = "*",allowCredentials = "true")
    @ResponseBody
    public ResultBean checkIsLogin(HttpServletRequest request,
                                   @CookieValue(name = "user-token",required = false)String uuid){
        /*String username = (String) request.getSession().getAttribute("user");
        if (username!=null){
            return new ResultBean(200,"已登录");
        }*/
        /*Cookie[] cookies = request.getCookies();
        if (cookies!=null && cookies.length>0){
            for (Cookie cookie : cookies) {
                if("user-token".equals(cookie.getName())){
                    return userService.checkIsLogin(cookie.getValue());
                }
            }
        }*/
        if (uuid!=null){
            return userService.checkIsLogin(uuid);
        }
        return new ResultBean(404,null);
    }

    //注销
    /*@PostMapping("loginOut")
    @ResponseBody
    public ResultBean loginOut(HttpServletRequest request){
        request.getSession().removeAttribute("user");
        return new ResultBean(200,"注销成功");
    }*/
    @GetMapping("loginOut")
    @ResponseBody
    public ResultBean loginOut(@CookieValue(name = "user-token",required = false)String uuid,
                               HttpServletResponse response){
        if (uuid!=null){
            Cookie cookie = new Cookie("user-token",uuid);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        return new ResultBean(200,"注销成功");
    }

    @GetMapping("checkIsLoginJsonp")
    @ResponseBody
    public String checkIsLoginJsonp(
            @CookieValue(name = "user-token",required = false)String uuid,
            String callback) throws JsonProcessingException {
        ResultBean resultBean = null;
        if(uuid != null){
            resultBean = userService.checkIsLogin(uuid);
        }else{
            //3.返回找不到的结果
            resultBean = new ResultBean(404, null);
        }
        //将对象转换为json
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(resultBean);
        //回调客户端的方法
        return callback+"("+json+")";//deal(json) padding
    }
}
