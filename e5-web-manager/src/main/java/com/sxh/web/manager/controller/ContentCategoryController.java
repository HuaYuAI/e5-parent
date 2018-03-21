package com.sxh.web.manager.controller;

import com.sxh.common.utils.E3Result;
import com.sxh.common.utils.EasyUITreeResponse;
import com.sxh.service.content.api.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by HuaYu on 2018/2/18.
 */
@Controller
@RequestMapping("content/category")
public class ContentCategoryController {
    /**
     *
     */
    //调用dubbo服务
    @Autowired
    ContentCategoryService contentCategoryService;

    @RequestMapping("list")
    @ResponseBody
    public List<EasyUITreeResponse> list(@RequestParam(defaultValue = "0") Long id){
        return contentCategoryService.list(id);
    }
    //新增内容分类
    @RequestMapping("create")
    @ResponseBody
    public E3Result create(Long parentId, String name){
        return contentCategoryService.save(parentId,name);
    }
}
