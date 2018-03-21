package com.sxh.service.search;

import com.alibaba.fastjson.JSON;
import com.sxh.service.goods.pojo.Item;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Created by HuaYu on 2018/2/23.
 */


public class itemChangeListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        //强制转换成字符串
        TextMessage itemStr = (TextMessage) message;
        //序列化成对象
        try {
            Item item = JSON.parseObject(itemStr.getText(), Item.class);

            //把商品保存到elasticsearch索引库
            RestClient lowLevelRestClient = RestClient.builder(
                    new HttpHost("192.168.177.123", 9200, "http")).build();
                    /*new HttpHost("192.168.2.3", 9200, "http"),
                    new HttpHost("192.168.2.4", 9200, "http")).build();*/
            RestHighLevelClient client = new RestHighLevelClient(lowLevelRestClient);
            IndexRequest indexRequest = new IndexRequest("es", "myitem",item.getId()+"" );
            indexRequest.source(itemStr.getText());
            IndexResponse indexResponse = client.index(indexRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
