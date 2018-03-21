package com.sxh.service.order.api;

import com.sxh.common.utils.E3Result;
import com.sxh.service.order.pojo.OrderInfo;
import com.sxh.service.user.pojo.TbUser;

/**
 * Created by HuaYu on 2018/2/25.
 */
public interface OrderService {
    //创建订单的接口：1.订单主表，2.订单商品表，3.物流表
    public E3Result orderCreate(OrderInfo orderInfo, TbUser user);
}
