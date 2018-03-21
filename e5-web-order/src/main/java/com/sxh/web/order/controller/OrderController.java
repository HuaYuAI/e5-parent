package com.sxh.web.order.controller;

import com.alibaba.fastjson.JSON;
import com.sxh.common.jedis.JedisClient;
import com.sxh.common.utils.Constant;
import com.sxh.common.utils.CookieUtils;
import com.sxh.common.utils.E3Result;
import com.sxh.service.goods.api.ItemService;
import com.sxh.service.goods.pojo.Item;
import com.sxh.service.goods.pojo.ItemExt;
import com.sxh.service.order.api.OrderService;
import com.sxh.service.order.pojo.OrderInfo;
import com.sxh.service.user.pojo.TbUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HuaYu on 2018/2/25.
 */
@Controller
@RequestMapping("order")
public class OrderController {
    @Autowired
    JedisClient jedisClient;
    @Autowired
    ItemService itemService;
    @Autowired
    OrderService orderService;

    /**
     * 订单确认页
     * 1.跳转之前先拦截，判断有没有登录
     * 2.获取购物车商品，从cookie中，合并到redis
     * @param response
     * @param request
     * @return
     */
    @RequestMapping("order-cart")
    public String orderConfirm(HttpServletResponse response, HttpServletRequest request){
        //获取cookie中商品的json字符串，转成list（此时商品购物车还在cookie中，没有合并）
        List<ItemExt> itemExtList = getCartListFromCookie(request);
        //判断用户是否登录
        TbUser user = (TbUser) request.getSession().getAttribute("user");
        //如果登录，合并cookie到redis

        //1.合并cookie到redis
        mergeCookieToRedis(itemExtList, user.getId());
        //2.查redis
        itemExtList = getCartFromRedis(user.getId());
        //3.删除cookie
        CookieUtils.deleteCookie(request, response, "TT_CART");
        request.setAttribute("cartList",itemExtList);
        return "order-cart";
    }
    private List<ItemExt> getCartListFromCookie(HttpServletRequest request) {
        String itemListJsonData = CookieUtils.getCookieValue(request, "TT_CART", true);
        //将商品的字符串转成list对象
        List<ItemExt> itemExtList = JSON.parseArray(itemListJsonData, ItemExt.class);
        if (itemExtList == null) {
            itemExtList = new ArrayList<>();
        }
        return itemExtList;
    }
    /**
     * 合并cookie到redis
     *
     * @param itemExtList
     */
    private void mergeCookieToRedis(List<ItemExt> itemExtList, long userId) {
        for (ItemExt itemExt : itemExtList) {
            addCartToRedis(itemExt.getId(), itemExt.getNumber(), userId);
        }
    }
    /**
     * 购物车商品加入到redis中，绑定当前用户
     *
     * @param itemId
     * @param num
     */
    void addCartToRedis(long itemId, Integer num, long userId) {
        //判断redis中是否已经存在该商品了，如果有，就累加，如果没有就新增
        if (jedisClient.hexists("redis_cart:" + userId, itemId + "")) {
            //从redis取出当前商品的json字符串
            String itemStr = jedisClient.hget("redis_cart:" + userId, itemId + "");
            //将当前商品字符串转成对象，fastjson
            ItemExt itemExt = JSON.parseObject(itemStr, ItemExt.class);
            //将商品数量累加
            itemExt.setNumber(itemExt.getNumber() + num);
            //将累加后的商品数量，再放到redis中
            jedisClient.hset("redis_cart:" + userId, "" + itemId, JSON.toJSONString(itemExt));
        } else {
            Item item = itemService.findById(itemId);
            ItemExt itemExt = new ItemExt();
            BeanUtils.copyProperties(item, itemExt);
            itemExt.setNumber(num);
            jedisClient.hset("redis_cart:" + userId, "" + itemId, JSON.toJSONString(itemExt));
        }
    }

    /**
     * 从redis中获取当前用户的购物车商品
     * @param userId
     * @return
     */
    private List<ItemExt> getCartFromRedis(Long userId) {
        List<ItemExt> itemExtList = new ArrayList<>();
        List<String> itemStrList = (List<String>) jedisClient.hvals("redis_cart:" + userId);
        for (String itemStr : itemStrList) {
            ItemExt itemExt = JSON.parseObject(itemStr, ItemExt.class);
            itemExtList.add(itemExt);
        }
        return  itemExtList;
    }

    /**
     * 订单生成，保存到数据库表中，状态开始是1，未支付
     * @return
     */
    @RequestMapping("create")
    public String orderCreate(OrderInfo orderInfo, HttpServletRequest request){
        //调用e3-service-order提供的服务，完成订单入库
        TbUser user = (TbUser) request.getSession().getAttribute(Constant.USER);
        E3Result e3Result = orderService.orderCreate(orderInfo, user);

        request.setAttribute("payment",orderInfo.getPayment());//总价
        request.setAttribute("orderId",e3Result.getData());//总价
        return "success";
    }
}
