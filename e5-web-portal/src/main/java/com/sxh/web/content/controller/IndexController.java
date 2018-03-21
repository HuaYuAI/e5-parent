package com.sxh.web.content.controller;

import com.alibaba.fastjson.JSON;
import com.sxh.service.content.api.ContentService;
import com.sxh.service.content.pojo.Content;
import com.sxh.service.goods.pojo.Item;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.*;

/**
 * Created by HuaYu on 2018/2/23.
 */
@Controller
public class IndexController {
    @Autowired
    ContentService contentService;
    @RequestMapping("/")
    public ModelAndView index(){
        //查询首页版块内容（只查询广告位） 89是大广告位的分类id，这里写死
        List<Content> contents = contentService.listByCatId(89L);
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("ad1List",contents);
        return mv;
    }

    /**
     * 从elasticsearch中查询商品，根据关键词搜索
     * @param keyword
     * @return
     */
    @RequestMapping("/search")
    public ModelAndView search(@RequestParam(defaultValue = "1" ,name = "page") int pageNo, String keyword) throws IOException {
        //连接es
        RestClient lowLevelRestClient = RestClient.builder(
                new HttpHost("192.168.177.123", 9200, "http")).build();
                /*new HttpHost("192.168.2.3", 9200, "http"),
                new HttpHost("192.168.2.4", 9200, "http")).build();*/
        RestHighLevelClient client = new RestHighLevelClient(lowLevelRestClient);
        SearchRequest searchRequest = new SearchRequest("es");//数据库 index
        searchRequest.types("myitem");//表名
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MultiMatchQueryBuilder mqb = QueryBuilders.multiMatchQuery(keyword, "title","sell_point");
        searchSourceBuilder.query(mqb);
        searchSourceBuilder.from(pageNo-1);//开始分页
        searchSourceBuilder.size(10);//每页多少条

        HighlightBuilder highlightBuilder = new HighlightBuilder();//标题加高亮
        highlightBuilder.preTags("<font color='red'>");
        highlightBuilder.postTags("</font>");
        highlightBuilder.field("title");
        searchSourceBuilder.highlighter(highlightBuilder);

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest);//根据查询条件搜
        SearchHits searchHits = searchResponse.getHits();//
        SearchHit[] searchHit = searchHits.getHits();//得到查询结果集

        List<Item> itemList = new ArrayList<>();
        for (SearchHit hit : searchHit) {
            String itemStr = hit.getSourceAsString();//每一条结果集得到一个商品json字符串
            Item item = JSON.parseObject(itemStr, Item.class);//通过阿里巴巴的fastjson将商品字符串转成商品对象
            HighlightField highlightField = hit.getHighlightFields().get("title");
            if(highlightField!=null &&highlightField.getFragments().length>0){
                String titleHighlight = highlightField.getFragments()[0].toString();//获取高亮显示的title
                if(!StringUtils.isEmpty(titleHighlight)){
                    item.setTitle(titleHighlight);
                }
            }

            itemList.add(item);
        }

        ModelAndView mv = new ModelAndView("search");
        //跳转页面之前，把数据带到跳转页面
        mv.addObject("itemList",itemList);
        mv.addObject("totalPages",searchHits.totalHits%10==0?searchHits.totalHits/10:searchHits.totalHits/10+1);
        mv.addObject("query",keyword);
        mv.addObject("totalCount",searchHits.totalHits);
        mv.addObject("page",pageNo);
        return mv;
    }

    public void esindex() throws IOException {
        RestClient lowLevelRestClient = RestClient.builder(
                new HttpHost("192.168.177.123", 9200, "http")).build();
                /*new HttpHost("192.168.2.3", 9200, "http"),
                new HttpHost("192.168.2.4", 9200, "http")).build();*/
        RestHighLevelClient client = new RestHighLevelClient(lowLevelRestClient);
        IndexRequest indexRequest = new IndexRequest("es", "myitem", "1");
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user", "kimchy");
        jsonMap.put("postDate", new Date());
        jsonMap.put("message", "trying out Elasticsearch");

        indexRequest.source(jsonMap);
        IndexResponse indexResponse = client.index(indexRequest);
       /* String index = indexResponse.getIndex();  //index名称，类型等信息
        String type = indexResponse.getType();
        String id = indexResponse.getId();
        long version = indexResponse.getVersion();
        if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {

        } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {

        }
        ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
        //对分片使用的判断
        if (shardInfo.getTotal() != shardInfo.getSuccessful()) {

        }
        if (shardInfo.getFailed() > 0) {
            for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                String reason = failure.reason();
            }
        }*/
		/*public void CreateDocument()throws Exception{
          String method = "PUT";
          String endpoint = "/test-index/test/1";
          HttpEntity entity = new NStringEntity(
                  "{\n" +
                          "    \"user\" : \"kimchy\",\n" +
                          "    \"post_date\" : \"2009-11-15T14:12:12\",\n" +
                          "    \"message\" : \"trying out Elasticsearch\"\n" +
                          "}", ContentType.APPLICATION_JSON);
          Response response = restClient.performRequest(method,endpoint, Collections.<String, String>emptyMap(),entity);
           System.out.println(EntityUtils.toString(response.getEntity()));
		}*/
    }
}
