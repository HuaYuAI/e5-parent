package com.sxh.web.item.listener;

import com.alibaba.fastjson.JSON;
import com.sxh.service.goods.pojo.Item;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by HuaYu on 2018/2/23.
 */
public class ItemFtlCreateListener implements MessageListener{
    @Autowired
    FreeMarkerConfigurer freeMarkerConfigurer;
    @Override
    public void onMessage(Message message) {
        TextMessage itemStr = (TextMessage) message;
        try {
            Item item = JSON.parseObject(itemStr.getText(),Item.class);
            //生成静态页面
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");
            Map dataMap  = new HashMap();
            dataMap.put("item",item);
            FileWriter fileWriter = new FileWriter("D:\\javalibs\\nginx-1.8.0\\html\\"+item.getId()+".html");
            template.process(dataMap,fileWriter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
