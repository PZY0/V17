package com.qianfeng.v17order.interceptor;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qianfeng.api.IUserService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author pangzhenyu
 * @Date 2019/11/18
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Reference
    private IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for (Cookie cookie : cookies) {
                if("user-token".equals(cookie.getName())){
                    ResultBean resultBean = userService.checkIsLogin(cookie.getValue());
                    if(200==resultBean.getStatusCode()){
                        request.setAttribute("user",resultBean.getData());
                        return true;
                    }
                }
            }
        }
        StringBuffer url = request.getRequestURL();
        response.sendRedirect("http://localhost:8095?referer="+url.toString());
        return false;*/
        return true;
    }
}
