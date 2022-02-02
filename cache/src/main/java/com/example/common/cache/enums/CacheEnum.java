package com.example.common.cache.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author xiapeng
 * @Date: 2022/02/02/11:13 下午
 * @Description:cache的枚举
 */
public enum CacheEnum {
    /**
     * redis
     */
    REDIS,
    /**
     * 咖啡因
     */
    CAFFEINE;
    private static final Map<String, CacheEnum> MAP =
            Arrays.stream(CacheEnum.values()).collect(Collectors.toMap(CacheEnum::name, o -> o));

    public static CacheEnum nameOf(String name) {
        CacheEnum cacheEnum = MAP.get(name);
        return cacheEnum == null ? CAFFEINE : cacheEnum;
    }
}
