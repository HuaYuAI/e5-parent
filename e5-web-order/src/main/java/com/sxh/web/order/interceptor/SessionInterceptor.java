package com.sxh.web.order.interceptor;

import com.sxh.common.utils.Constant;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by HuaYu on 2018/2/25.
 */
public class SessionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //本次要访问的请求地址
        String destpath=request.getRequestURL().toString();
        Object user=request.getSession().getAttribute(Constant.USER);

        if(user==null){
            //如果用户为空，则返回登录页面，并且登录成功后，回到当前页
            response.sendRedirect(Constant.SSO_LOGIN_URL+"?redirect="+destpath);
            return false;
        }

        //如果存在则说明登录了，直接放行。
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
