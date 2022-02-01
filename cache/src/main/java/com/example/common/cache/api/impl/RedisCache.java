package com.example.common.cache.api.impl;

import com.example.common.cache.api.CacheApi;
import com.example.common.util.BeanUtil;
import redis.clients.jedis.Jedis;

/**
 * @Author xiapeng
 * @Date: 2022/01/31/12:08 上午
 * @Description:
 */
public enum RedisCache implements CacheApi {
    /**
     * 实例
     */
    INSTANCE;
    private static final String OK = "OK";

    private final Jedis jedis;
    private static final String NAME = "Redis";

    RedisCache() {
        jedis = BeanUtil.getBeanFactory().getBean(Jedis.class);
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        return null;
    }

    @Override
    public void set(String key, Object value, int expire) {

    }

    @Override
    public void remove(String key) {

    }
}
