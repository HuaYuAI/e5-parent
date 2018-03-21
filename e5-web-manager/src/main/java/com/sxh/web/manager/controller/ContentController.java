package com.sxh.web.manager.controller;

import com.sxh.common.utils.E3Result;
import com.sxh.common.utils.EasyUIDatagridRequest;
import com.sxh.common.utils.EasyUIDatagridResponse;
import com.sxh.common.utils.EasyUITreeResponse;
import com.sxh.service.content.api.ContentService;
import com.sxh.service.content.pojo.Content;
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
@RequestMapping("content")
public class ContentController {
    //远程调用dubbo
    @Autowired
    ContentService contentService;
    @RequestMapping("save")
    @ResponseBody
    public E3Result save(Content content){
        return contentService.save(content);
    }
    @RequestMapping("query/list")
    @ResponseBody
    public EasyUIDatagridResponse list(int categoryId,EasyUIDatagridRequest es){
        EasyUIDatagridResponse rs = contentService.list(categoryId,es);
        return rs;
    }
}
