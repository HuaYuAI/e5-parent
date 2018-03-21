package com.sxh.web.cart.controller;

import com.alibaba.fastjson.JSON;
import com.sxh.common.jedis.JedisClient;
import com.sxh.common.utils.CookieUtils;
import com.sxh.common.utils.E3Result;
import com.sxh.service.goods.api.ItemService;
import com.sxh.service.goods.pojo.Item;
import com.sxh.service.user.pojo.TbUser;
import com.sxh.service.goods.pojo.ItemExt;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by HuaYu on 2018/2/24.
 */
@Controller
@RequestMapping("cart")
public class CartController {
    @Autowired
    ItemService itemService;
    @Autowired
    JedisClient jedisClient;
    @RequestMapping("add/{itemId}")
    public String addCart(@PathVariable("itemId")long itemId, Integer num, HttpServletRequest request, HttpServletResponse response){
        //登录情况下，将购物车商品加到redis中，不再加入cookie中
        TbUser user=(TbUser)request.getSession().getAttribute("user");
        if(user!=null){
            addCartToRedis(itemId,num,user.getId());
            return "cartSuccess";
        }
        //未登录，现获取cookie中的其他商品，然后将商品添加到cookie中,使用工具类。
        /*Cookie cooke=new Cookie("","");
        response.addCookie(cooke);*/
        List<ItemExt> itemExtList=getCartListFromCookie(request);
        //判断商品是否存在，如果存在累加数量，如果不存在，那么cookie新增商品
        //循环遍历以前的商品，将商品的数量进行累加,巧用中间变量，如果在for循环中用ifelse会循环增加
        boolean flag=false;
        for(ItemExt ext:itemExtList){
            //如果当前商品的id存在，就累加
            if(itemId==ext.getId()){
                ext.setNumber(ext.getNumber()+num);
                flag=true;
                break;
            }
        }
        if(!flag) {//如果flag为false，说明原cookie没有因此新增加
            //根据商品id，查旬出商品实体类
            Item item=itemService.findById(itemId);
            //把商品序列话成json字符串，保存到cookie中
            ItemExt itemExt=new ItemExt();
            //beanUtils工具类，将一个类的属性值会全部拷贝到另一个类的对应值里
            BeanUtils.copyProperties(item,itemExt);
            itemExt.setNumber(num);
            itemExtList.add(itemExt);
        }
        //把修改后的商品，重新放到cookie中
        CookieUtils.setCookie(request,response,"TT_CART",JSON.toJSONString(itemExtList),true);
        return "cartSuccess";
    }
    //从cookie中获取商品信息
    private List<ItemExt> getCartListFromCookie(HttpServletRequest request){
        String itemListJsonData= CookieUtils.getCookieValue(request,"TT_CART",true);
        //将商品字符串转换成list对象
        List<ItemExt> itemExtList=JSON.parseArray(itemListJsonData,ItemExt.class);
        if(itemExtList==null){
            itemExtList=new ArrayList<>();
        }
        return itemExtList;
    }
    @RequestMapping("cart")
    public String showCart(HttpServletRequest request,HttpServletResponse response){
        //获取cookie中商品的json字符串，转成list
        List<ItemExt> itemExtList=getCartListFromCookie(request);
        //判断是否是用户登录
        TbUser user=(TbUser)request.getSession().getAttribute("user");
        //如果使用户登录合并cookie到redis，并删除cookie中的值
        if(user!=null){
            //合并cookie到redis
            mergeCookieToRedis(itemExtList,user.getId());
            //查询redis
            itemExtList=getcartListFromRedis(user.getId());
            //删除cookie中的值
            CookieUtils.deleteCookie(request,response,"TT_CART");
        }
        request.setAttribute("cartList",itemExtList);
        return "cart";
    }
    //从redis中查询信息
    private List<ItemExt> getcartListFromRedis(Long userId){
        List<ItemExt> itemExtList = new ArrayList<>();
        List<String> itemStrList=(List<String>)jedisClient.hvals("redis_cart:"+userId);
        for (String itemStr:itemStrList) {
            ItemExt itemExt=JSON.parseObject(itemStr,ItemExt.class);
            itemExtList.add(itemExt);
        }
        return itemExtList;
    }
    //将cookie中的值添加到redis中
    private void mergeCookieToRedis(List<ItemExt> itemExtList,long userId){
        for (ItemExt itemExt:itemExtList) {
            addCartToRedis(itemExt.getId(),itemExt.getNumber(),userId);
        }
    }
    //修改购物车中的信息，使用restful风格
    @RequestMapping("update/num/{itemId}/{num}")
    @ResponseBody
    public E3Result updateCart(HttpServletRequest request,HttpServletResponse response,@PathVariable("itemId")long itemId,@PathVariable("num") int num){
        //获取cookie中商品的接送字符串，转成list
        List<ItemExt> itemExtList=getCartListFromCookie(request);
        for (ItemExt ext:itemExtList) {
            ext.setNumber(num);
        }
        request.setAttribute("cartList",itemExtList);
        CookieUtils.setCookie(request,response,"TT_CART",JSON.toJSONString(itemExtList),true);
        return E3Result.ok();
    }
    //向redis中添加商品
    void addCartToRedis(long itemId,Integer num,long userId){
        //判断redis中是否已经存在该商品，如果有，就累加，如果没有就增加
        if(jedisClient.hexists("redis_cart:"+userId,itemId+"")){
            //从redis中取出当前商品的json字符串
            String itemStr=jedisClient.hget("redis_cart:"+userId,itemId+"");
            //将当前商品字符串转成对象，fastjson
            ItemExt itemExt= JSON.parseObject(itemStr,ItemExt.class);
            //将商品数量累加
            itemExt.setNumber(itemExt.getNumber()+num);
            //将累加后的商品数量，在放到redis中
            jedisClient.hset("redis_cart:"+userId,""+itemId,JSON.toJSONString(itemExt));
        }else{
            Item item=itemService.findById(itemId);
            ItemExt itemExt=new ItemExt();
            //将一个类中的内容拷贝到另一个类中
            BeanUtils.copyProperties(item,itemExt);
            //
            itemExt.setNumber(num);
            jedisClient.hset("redis_cart:"+userId,""+itemId, JSON.toJSONString(itemExt));
        }
    }
}
