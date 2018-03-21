package com.sxh.web.content.controller;

import com.sxh.service.goods.api.ItemService;
import com.sxh.service.goods.pojo.Item;
import com.sxh.service.goods.pojo.ItemDesc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by HuaYu on 2018/2/23.
 */
@Controller
public class ItemController {
    //注入itemService  ，通过dubbo远程调用
    @Autowired
    ItemService itemService;

    /**
     * 查询商品详情的controller
     * @param itemId
     * @return
     */
    @RequestMapping("item/{itemId}")
    public ModelAndView showItem(@PathVariable("itemId") Long itemId){
        ModelAndView mv = new ModelAndView("item");
        //跳转页面之前，查询商品描述，商品基本信息，商品规格，商品评价
        ItemDesc itemDesc = itemService.selectDesc(itemId);
        Item item = itemService.findById(itemId);
        //把数据加到requrest中
        mv.addObject("item",item);
        mv.addObject("itemDesc",itemDesc);
        return mv;
    }
}
