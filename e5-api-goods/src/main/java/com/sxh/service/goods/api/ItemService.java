package com.sxh.service.goods.api;

import com.sxh.common.utils.E3Result;
import com.sxh.common.utils.EasyUIDatagridRequest;
import com.sxh.common.utils.EasyUIDatagridResponse;
import com.sxh.service.goods.pojo.ItemDesc;
import com.sxh.service.goods.pojo.ItemParamItem;
import com.sxh.service.goods.pojo.Item;
/**
 * Created by HuaYu on 2018/2/18.
 */
public interface ItemService {
    //分页查询商品的接口，通过dubbo会暴露成url地址
    public EasyUIDatagridResponse list(EasyUIDatagridRequest es);

    public E3Result save(Item item, String itemDesc, String itemParams);
    //校验商品分类是否已经存在
    public E3Result check(Long catid);
    //保存商品分类和规格
    public E3Result saveParam(Long catid, String paramStr);

    ItemDesc selectDesc(Long itemId);

    ItemParamItem selectParam(Long itemId);

    Item findById(Long itemId);

    EasyUIDatagridResponse paramList(EasyUIDatagridRequest es);
}
