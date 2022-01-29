package com.example.common.cache.api;

/**
 * @Author xiapeng
 * @Date: 2022/01/30/1:16 上午
 * @Description: cache api
 */
public interface CacheApi {
    /**
     * get
     *
     * @param key   键
     * @param clazz 值类型
     * @param <T>   cache类型参数
     * @return t
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * set
     *
     * @param key    健
     * @param value  值
     * @param expire 过期时间(s)
     */
    void set(String key, Object value, int expire);

    /**
     * remove
     *
     * @param key 健
     */
    void remove(String key);
}
