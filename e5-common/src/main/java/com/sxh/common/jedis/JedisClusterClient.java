package com.sxh.common.jedis;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisCluster;

import java.util.List;

/**
 * Created by HuaYu on 2018/2/18.
 */
public class JedisClusterClient implements  JedisClient {
    @Autowired
    JedisCluster jedisCluster;
    @Override
    public void set(String key, String value) {
        jedisCluster.set(key,value);
    }

    @Override
    public void hset(String key, String field, String value) {
        jedisCluster.hset(key,field,value);
    }

    @Override
    public void setex(String key, String value) {
        jedisCluster.setnx(key,value);
    }

    @Override
    public void expire(String key, int seconds) {
        jedisCluster.expire(key,seconds);
    }

    @Override
    public String get(String key) {
        return jedisCluster.get(key);
    }

    @Override
    public String hget(String key, String field) {
        return jedisCluster.hget(key,field);
    }

    @Override
    public Boolean exists(String key) {
        return jedisCluster.exists(key);

    }
    @Override
    public Boolean hexists(String key, String field) {
        return jedisCluster.hexists(key,field);
    }

    @Override
    public void del(String key) {
        jedisCluster.del(key);
    }

    @Override
    public void hdel(String key, String... field) {
        jedisCluster.hdel(key,field);
    }

    @Override
    public List<?> hvals(String key) {
        return jedisCluster.hvals(key);
    }
}
