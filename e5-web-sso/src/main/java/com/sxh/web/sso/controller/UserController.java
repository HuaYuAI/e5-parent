package com.sxh.web.sso.controller;

import com.sxh.common.utils.E3Result;
import com.sxh.service.user.api.UserService;
import com.sxh.service.user.pojo.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by HuaYu on 2018/2/23.
 */
@Controller
@RequestMapping("user")
public class UserController {

    //dubbo暴露的服务
    @Autowired
    UserService userService;

    /**
     *注册前，检验用户是否存在
     * @return
     */
    @RequestMapping(value = "/check/{param}/{type}",method = RequestMethod.GET)
    @ResponseBody
    public E3Result check(@PathVariable("param") String param, @PathVariable("type") Integer type){
        //通过userService，查询数据库，验证用户是否已经注册过。（用户名，手机号，email都不能相同，方可注册成功）
        return userService.check(param, type);
    }
    /**
     * 用户注册接口，遵循接口文档开发
     * @return
     */
    @RequestMapping("register")
    @ResponseBody
    public E3Result register(TbUser user){
        return userService.register(user);
    }

    /**
     * 用户登录接口
     * @return
     */
    @RequestMapping("login")
    @ResponseBody
    public E3Result login(String username, String password, HttpServletRequest request){
        E3Result result = userService.login(username, password);
        //登录成功，将用户信息放session中，实现分布式session共享
        if(result!=null){
            TbUser user = (TbUser) result.getData();
            request.getSession().setAttribute("user",user);
            return E3Result.ok();
        }
        return  E3Result.build(222,"用户名或者密码错误");
    }

}
