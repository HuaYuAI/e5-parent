package com.sxh.web.manager.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.sxh.common.utils.E3Result;
import com.sxh.common.utils.EasyUIDatagridRequest;
import com.sxh.common.utils.EasyUIDatagridResponse;
import com.sxh.service.goods.api.ItemService;
import com.sxh.service.goods.pojo.Item;
import com.sxh.service.goods.pojo.ItemDesc;
import com.sxh.service.goods.pojo.ItemParamItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by HuaYu on 2018/2/18.
 */
@Controller
@RequestMapping("item")
public class ItemController {
    /**
     *
     */
    //面向服务，没有service可以依赖，使用的是dubbo
    //注入dubbo提供的服务
    @Autowired
    ItemService itemService;
    //分页查询商品
    @RequestMapping("list")
    @ResponseBody
    public EasyUIDatagridResponse list(EasyUIDatagridRequest es){
        EasyUIDatagridResponse rs = itemService.list(es);
        return rs;
    }

    /**
     * 保存商品，包括商品的基本信息（Item），商品描述（ItemDesc），商品的规格值
     * @param item
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public E3Result save(Item item, @RequestParam("desc") String itemDesc, String itemParams){

        E3Result save = itemService.save(item, itemDesc,itemParams);
        return save;
    }
    //校验商品规格是否已经存在
    @RequestMapping("param/query/itemcatid/{id}")
    @ResponseBody
    public E3Result check(@PathVariable("id") Long catid){
        E3Result check = itemService.check(catid);
        return check;
    }

    //绑定商品分类和规格
    //校验商品规格是否已经存在
    @RequestMapping("param/save/{id}")
    @ResponseBody
    public E3Result saveParam(@PathVariable("id") Long catid,String paramData){
        E3Result check = itemService.saveParam(catid,paramData);
        return check;
    }

    @RequestMapping("param/list")
    @ResponseBody
    public EasyUIDatagridResponse paramList(EasyUIDatagridRequest es){
        EasyUIDatagridResponse rs = itemService.paramList(es);
        return rs;
    }

    //展示商品详情
    @RequestMapping("showDesc")
    public String showDesc(Long itemId,Model model){
        //跳转之前，查询商品描述，以及商品规格值
        ItemDesc itemDesc = itemService.selectDesc(itemId);
        model.addAttribute("desc",itemDesc);
        //规格
        ItemParamItem paramItem = itemService.selectParam(itemId);
        String paramData = paramItem.getParamData();//规格值的字符串，需要转成json对象
        //使用阿里巴巴的fastjson工具类，转换
        JSONArray jsonObj = JSON.parseArray(paramData);
        model.addAttribute("jsonObj",jsonObj);

        return "item-desc";
    }
}
