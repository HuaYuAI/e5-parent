package com.sxh.service.goods.api.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sxh.common.jedis.JedisClient;
import com.sxh.common.utils.E3Result;
import com.sxh.common.utils.EasyUIDatagridRequest;
import com.sxh.common.utils.EasyUIDatagridResponse;
import com.sxh.common.utils.IDUtils;
import com.sxh.service.goods.api.ItemService;
import com.sxh.service.goods.mapper.ItemDescMapper;
import com.sxh.service.goods.mapper.ItemMapper;
import com.sxh.service.goods.mapper.ItemParamItemMapper;
import com.sxh.service.goods.mapper.ItemParamMapper;
import com.sxh.service.goods.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.Date;
import java.util.List;

/**
 * Created by HuaYu on 2018/2/18.
 */
@Service("itemService")
public class ItemServiceImpl implements ItemService {
    @Autowired
    ItemMapper itemMapper;
    @Autowired
    ItemDescMapper itemDescMapper;

    @Autowired
    ItemParamMapper itemParamMapper;

    @Autowired
    ItemParamItemMapper itemParamItemMapper;

    @Resource(name="topicDestination")
    Destination topicDestination;

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    JedisClient jedisClient;//缓存接口类，负责添加和查询缓存

    public EasyUIDatagridResponse list(EasyUIDatagridRequest es) {
        EasyUIDatagridResponse rs = new EasyUIDatagridResponse();
        //使用pageHelper
        //开始分页查询。aop
        PageHelper.startPage(es.getPage(),es.getRows());
        List<Item> items = itemMapper.selectByExample(null);
        PageInfo<Item> pg = new PageInfo<Item>(items);
        //总条数
        rs.setTotal(pg.getTotal());
        //分页记录
        rs.setRows(pg.getList());
        return rs;
    }


    @Transactional
    public E3Result save(Item item, String desc, String itemParams) {
        item.setCreated(new Date());//创建时间
        item.setUpdated(new Date());//修改时间
        item.setStatus((byte)1);//状态 上下架
        item.setId(IDUtils.genItemId());//使用封装的工具类获取id
        //保存商品基本信息
        itemMapper.insert(item);//要配置selectkey

        //保存商品描述
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(new Date());
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        itemDescMapper.insert(itemDesc);

        //保存商品的规格值
        ItemParamItem paramItem = new ItemParamItem();
        paramItem.setItemId(item.getId());
        paramItem.setParamData(itemParams);//规格值的json字符串
        paramItem.setCreated(new Date());
        paramItem.setUpdated(new Date());
        itemParamItemMapper.insert(paramItem);

        //继续扩展，添加商品到mysql数据库完毕后，同样，要向elasticsearch中添加一份
        //我们这里异步调用，使用mq，保证主线任务先完成，先提示用户商品添加成功，使用mq消息队列，将新增
        //的商品，推送到mq，由mq通知其他系统，去进行相关的操作
        //我们需要更新三个系统：1.缓存redis  2.es索引   3.freemaker静态页面

        //把商品数据以字符串形式发送到mq
        jmsTemplate.send(topicDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                String itemStr = JSON.toJSONString(item);//把商品数据序列化成字符串
                TextMessage textMessage = session.createTextMessage(itemStr);
                return textMessage;
            }
        });
        return E3Result.ok();
    }

    public E3Result check(Long catid) {
        ItemParamExample example = new ItemParamExample();
        ItemParamExample.Criteria criteria = example.createCriteria();
        criteria.andItemCatIdEqualTo(catid);
        List<ItemParam> itemParams = itemParamMapper.selectByExampleWithBLOBs(example);
        if(null!=itemParams&&itemParams.size()>0){
            return E3Result.ok(itemParams.get(0));//数据返回  {status：200，data：{paramDate："[主体：[品牌，型号]]"}}
        }
        E3Result e3Result=new E3Result();
        e3Result.setStatus(201);
        return e3Result;
    }

    public E3Result saveParam(Long catid, String paramStr) {
        ItemParam itemParam = new ItemParam();
        itemParam.setItemCatId(catid);
        itemParam.setParamData(paramStr);
        itemParam.setCreated(new Date());
        itemParam.setUpdated(new Date());
        itemParamMapper.insert(itemParam);
        return E3Result.ok();
    }

    //查询商品描述
    @Override
    public ItemDesc selectDesc(Long itemId) {
        //1.先判断缓存中有没有
        try {
            if(jedisClient.hexists(itemId+"","desc")){
                String baseInfo = jedisClient.hget(itemId + "", "desc");
                ItemDesc itemDesc = JSON.parseObject(baseInfo, ItemDesc.class);
                return itemDesc;
            }
        } catch (Exception e) {
        }
        ItemDescExample example = new ItemDescExample();
        ItemDescExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        List<ItemDesc> itemDescs = itemDescMapper.selectByExampleWithBLOBs(example);
        if(itemDescs!=null && itemDescs.size()>0){
            ItemDesc itemDesc = itemDescs.get(0);
            String itemDescStr = JSON.toJSONString(itemDesc);
            //查完数据库，往缓存中放一份
            try {
                jedisClient.hset(itemId+"","desc",itemDescStr);
            } catch (Exception e) {
            }
            return itemDesc;
        }

        return null;
    }

    //查询商品规格值
    @Override
    public ItemParamItem selectParam(Long itemId) {
        ItemParamItemExample example = new ItemParamItemExample();
        ItemParamItemExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        List<ItemParamItem> itemParamItems = itemParamItemMapper.selectByExampleWithBLOBs(example);
        if(itemParamItems!=null && itemParamItems.size()>0){
            return itemParamItems.get(0);
        }
        return null;
    }
    //根据主键，查询商品
    @Override
    public Item findById(Long itemId) {
        //查询商品之前，因为商品的信息量较大，为了解决网络响应延迟，我们加入缓存
        //1.先判断缓存中有没有
        try {
            if(jedisClient.hexists(itemId+"","base")){
                String baseInfo = jedisClient.hget(itemId + "", "base");
                Item item = JSON.parseObject(baseInfo, Item.class);
                return item;
            }
        } catch (Exception e) {
        }
        Item item = itemMapper.selectByPrimaryKey(itemId);
        String itemStr = JSON.toJSONString(item);
        //查完数据库，往缓存中放一份
        try {
            jedisClient.hset(itemId+"","base",itemStr);
        } catch (Exception e) {
        }
        return item;
    }

    @Override
    public EasyUIDatagridResponse paramList(EasyUIDatagridRequest es) {
        EasyUIDatagridResponse rs = new EasyUIDatagridResponse();
        //使用pageHelper
        //开始分页查询。aop
        PageHelper.startPage(es.getPage(),es.getRows());
        List<ItemParam> itemParams = itemParamMapper.selectByExample(new ItemParamExample());
        PageInfo<ItemParam> pg = new PageInfo<ItemParam>(itemParams);
        //总条数
        rs.setTotal(pg.getTotal());
        //分页记录
        rs.setRows(pg.getList());
        return rs;
    }
}
