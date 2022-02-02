package com.example.common.cache.api.impl;

import com.example.common.cache.api.CacheApi;
import com.example.common.cache.constants.Constant;
import com.example.common.util.BeanUtil;
import com.example.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * @Author xiapeng
 * @Date: 2022/01/31/12:08 上午
 * @Description:Redis缓存
 */
@Slf4j
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
        String operation = "get";
        try {
            String value = jedis.get(key);
            log.debug(Constant.SUCCESS, NAME, operation, key, value);
        } catch (Exception e) {
            log.error(Constant.FAIL, NAME, operation, key, "", e);
        }
        return null;
    }

    @Override
    public void set(String key, Object value, int expire) {
        String operation = "set";
        try {
            String val = value == null ? "" : JsonUtil.toString(value);
            String result = jedis.set(key, val);
            if (OK.equals(result)) {
                log.debug(Constant.SUCCESS, NAME, operation, key, value);
            } else {
                log.debug(Constant.FAIL, NAME, operation, key);
            }
            jedis.expire(key, expire);
        } catch (Exception e) {
            log.error(Constant.FAIL, NAME, operation, key, "", e);
        }
    }

    @Override
    public void remove(String key) {
        String operation = "remove";
        try {
            Long result = jedis.del(key);
            if (result > 0) {
                log.debug(Constant.SUCCESS, NAME, operation, key);
            } else {
                log.debug(Constant.FAIL, NAME, operation, key);
            }
        } catch (Exception e) {
            log.error(Constant.FAIL, NAME, operation, key, "", e);
        }
    }
}
