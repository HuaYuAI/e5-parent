package com.sxh.service.order.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by HuaYu on 2018/2/25.
 */
//用于订单存储生成
public class OrderInfo extends  TbOrder implements Serializable {
    //条目集合
    private List<TbOrderItem> orderItems;
    //订单物流
    private TbOrderShipping orderShipping;
    public OrderInfo(){
    }
    public OrderInfo(List<TbOrderItem> orderItems, TbOrderShipping orderShipping) {
        this.orderItems = orderItems;
        this.orderShipping = orderShipping;
    }
    public List<TbOrderItem> getOrderItems() {

        return orderItems;
    }

    public void setOrderItems(List<TbOrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public TbOrderShipping getOrderShipping() {
        return orderShipping;
    }

    public void setOrderShipping(TbOrderShipping orderShipping) {
        this.orderShipping = orderShipping;
    }
}
