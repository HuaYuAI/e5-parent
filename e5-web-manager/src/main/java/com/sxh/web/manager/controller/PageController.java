package com.sxh.web.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by HuaYu on 2018/2/18.
 */
@Controller
public class PageController {
    @RequestMapping("{page}")
    public String page(@PathVariable("page") String page){
        return page;
    }
}
