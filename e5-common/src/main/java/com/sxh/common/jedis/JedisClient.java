package com.sxh.common.jedis;

import java.util.List;

/**
 * 此接口负责，缓存的增删改查
 * Created by HuaYu on 2018/2/18.
 */
public interface JedisClient {
    public void set(String key,String value);//string类型数据
    public void hset(String key,String field,String value);//hash类型放数据

    public void setex(String key,String value);//放元素的时候指定好生命周期
    public void expire(String key,int seconds);//单独多长时间，缓存失效

    public String get(String key);//string 类型取值
    public String hget(String key,String field);//hash类型取值

    Boolean exists(String key);//判断一个key是否存在。查询时有没有
    Boolean hexists(String key,String field);//判断一个key是否存在。查询时有没有

    public void del(String key);
    public void hdel(String key,String... field);

    public List hvals(String s);
}
