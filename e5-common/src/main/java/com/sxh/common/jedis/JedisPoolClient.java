package com.sxh.common.jedis;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * Created by HuaYu on 2018/2/18.
 */
public class JedisPoolClient implements JedisClient {
    @Override
    public void del(String key) {
        Jedis jedis = jedisPool.getResource();
        jedis.del(key);
    }

    @Override
    public void hdel(String key, String... field) {
        Jedis jedis = jedisPool.getResource();
        jedis.hdel(key,field);
    }
    @Override
    public List<?> hvals(String key) {
        Jedis jedis = jedisPool.getResource();
        return jedis.hvals(key);
    }
    @Autowired
    JedisPool jedisPool;;//连接池
    @Override
    public void set(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        jedis.set(key,value);
    }

    @Override
    public void hset(String key, String field, String value) {
        Jedis jedis = jedisPool.getResource();
        jedis.hset(key,field,value);
    }

    @Override
    public void setex(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        jedis.setnx(key,value);
    }

    @Override
    public void expire(String key, int seconds) {
        Jedis jedis = jedisPool.getResource();
        jedis.expire(key,seconds);
    }

    @Override
    public String get(String key) {
        Jedis jedis = jedisPool.getResource();
        return jedis.get(key);
    }

    @Override
    public String hget(String key, String field) {
        Jedis jedis = jedisPool.getResource();
        return jedis.hget(key,field);
    }

    @Override
    public Boolean exists(String key) {
        Jedis jedis = jedisPool.getResource();
        return jedis.exists(key);
    }

    @Override
    public Boolean hexists(String key, String field) {
        Jedis jedis = jedisPool.getResource();
        return jedis.hexists(key,field);
    }
}
