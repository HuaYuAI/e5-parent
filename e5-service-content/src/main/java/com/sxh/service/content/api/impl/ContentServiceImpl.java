package com.sxh.service.content.api.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sxh.common.jedis.JedisClient;
import com.sxh.common.utils.*;
import com.sxh.service.content.api.ContentService;
import com.sxh.service.content.mapper.ContentMapper;
import com.sxh.service.content.pojo.Content;
import com.sxh.service.content.pojo.ContentCategory;
import com.sxh.service.content.pojo.ContentCategoryExample;
import com.sxh.service.content.pojo.ContentExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by HuaYu on 2018/2/18.
 */
@Service("contentService")
public class ContentServiceImpl implements ContentService {
    @Autowired
    ContentMapper contentMapper;

    @Autowired
    JedisClient jedisClient;//缓存接口类，负责添加和查询缓存
    @Override
    public E3Result save(Content content) {
        content.setCreated(new Date());//创建时间
        content.setUpdated(new Date());//修改时间
        contentMapper.insert(content);
        //数据库有了，还需要同步更新缓存
        //我们可以这么做，先把缓存删除
        jedisClient.hdel(Constant.INDEX_CAT,content.getCategoryId()+"");
        return E3Result.ok();
    }

    /**
     * 首页布局我们加入缓存
     * @param
     * @return
     */
    @Override
    public List<Content> listByCatId(long catId) {
        //首先查询缓存，缓存中有直接返回，缓存中没有再查数据库
        //缓存的加入不能影响正常业务，及时缓存关闭，业务一样可以运转
        try {
            if(jedisClient.hexists(Constant.INDEX_CAT,catId+"")){
                //如果存在，直接返回
                String adStr = jedisClient.hget(Constant.INDEX_CAT, catId + "");
                //字符串转list，使用fastjson
                List<Content> contents = JSON.parseArray(adStr, Content.class);
                return contents;
            }
        }catch (Exception e){

        }

        ContentExample example = new ContentExample();
        ContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(catId);
        List<Content> contents = contentMapper.selectByExample(example);
        //第一次查询时，缓存中没有，从数据库查询出来以后，放缓存中一份
        //list转字符串，使用fastjson
        String str = JSON.toJSONString(contents);

        try {
            jedisClient.hset(Constant.INDEX_CAT,catId+"",str);
        } catch (Exception e) {
        }
        return contents;
    }

    @Override
    public EasyUIDatagridResponse list(long categoryId, EasyUIDatagridRequest es) {
        EasyUIDatagridResponse rs = new EasyUIDatagridResponse();
        //使用pageHelper
        //开始分页查询。aop
        PageHelper.startPage(es.getPage(),es.getRows());
        ContentExample example=new ContentExample();
        example.createCriteria().andCategoryIdEqualTo(categoryId);
        List<Content> contents = contentMapper.selectByExample(example);
        PageInfo<Content> pg = new PageInfo<Content>(contents);
        //总条数
        rs.setTotal(pg.getTotal());
        //分页记录
        rs.setRows(pg.getList());
        return rs;
    }


}
