package com.example.common.cache.api.impl;

import com.example.common.cache.api.CacheApi;
import com.example.common.cache.constants.Constant;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Author xiapeng
 * @Date: 2022/01/30/2:06 上午
 * @Description: 本地缓存
 */
@Slf4j
public class CaffeineCache implements CacheApi {
    private final Cache<String, Object> cache;
    private static final String NAME = "Caffeine";

    public CaffeineCache(int expire) {
        this.cache = Caffeine.newBuilder().expireAfterWrite(expire, TimeUnit.SECONDS).build();
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        Object value = cache.getIfPresent(key);
        if (value == null) {
            log.debug(Constant.FAIL, NAME, "get", key);
            return null;
        }
        log.debug(Constant.SUCCESS, NAME, "get", key, value);
        return Constant.EMPTY.equals(value) ? null : (T) value;
    }

    @Override
    public void set(String key, Object value, int expire) {
        if (value == null) {
            value = Constant.EMPTY;
        }
        log.debug(Constant.SUCCESS, NAME, "set", key, value);
        cache.put(key, value);
    }

    @Override
    public void remove(String key) {
        log.debug(Constant.SUCCESS, NAME, "remove", key);
        cache.invalidate(key);
    }
}
