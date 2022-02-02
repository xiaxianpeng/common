package com.example.common.cache.util;

import com.example.common.cache.annotation.Cache;
import com.example.common.cache.api.CacheApi;
import com.example.common.cache.api.impl.CaffeineCache;
import com.example.common.cache.config.CacheConfig;
import com.example.common.cache.constants.Constant;
import com.example.common.cache.parser.DefaultKeyGenerator;
import com.example.common.cache.parser.DefaultResultParser;
import com.example.common.util.MethodUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author xiapeng
 * @Date: 2022/02/03/12:25 上午
 * @Description:工具类
 */
public class CacheUtil {
    private static final Map<String, Method> NAME_METHOD_MAP = new ConcurrentHashMap<>(20);
    private static CacheConfig cacheConfig = new CacheConfig();
    private static final Map<String, CacheApi> NAME_API_MAP = new ConcurrentHashMap<>(20);

    private CacheUtil() {
    }

    public static void setCacheConfig(CacheConfig cacheConfig) {
        CacheUtil.cacheConfig = cacheConfig;
    }

    /**
     * 通过cache名获取cache api
     *
     * @param name cache名
     * @return CacheApi
     */
    public static CacheApi getCacheAPI(String name) {
        CacheApi cacheApi = CacheUtil.NAME_API_MAP.get(name);
        if (cacheApi == null) {
            cacheApi = new CaffeineCache(Constant.EXPIRE);
            CacheUtil.NAME_API_MAP.put(name, cacheApi);
        }
        return cacheApi;
    }

    /**
     * 缓存名
     *
     * @param method 方法
     * @return 缓存名
     */
    public static String getName(Method method) {
        Cache cache = method.getDeclaredAnnotation(Cache.class);
        String defaultName = cache.name();
        return StringUtils.isBlank(defaultName) ? method.getName() : defaultName;
    }

    /**
     * 通过参数列表和cache名获取key
     *
     * @param method 方法
     * @param spel   spel表达式
     * @param prefix key前缀
     * @param args   参数列表
     * @return key
     */
    public static String getKey(Method method, String spel, String prefix, Object... args) {
        if (StringUtils.isBlank(prefix)) {
            prefix = cacheConfig.getPrefix();
        }
        String key = StringUtils.isBlank(spel) ? DefaultKeyGenerator.INSTANCE.apply(args) :
                MethodUtil.getSPEL(args, method, spel, String.class);
        return prefix + Constant.KEY_CONNECTOR + key;
    }

    /**
     * 通过参数列表和cache的名字获取key
     *
     * @param name cache名
     * @param args 参数列表
     * @return key
     */
    public static String getKey(String name, Object... args) {
        Method method = NAME_METHOD_MAP.get(name);
        if (method == null) {
            return null;
        }
        Optional<Cache> optional = getCache(name);
        if (!optional.isPresent()) {
            return "";
        }
        Cache cache = optional.get();
        return getKey(method, cache.key(), cache.prefix(), args);
    }

    public static Optional<Cache> getCache(String name) {
        Method method = NAME_METHOD_MAP.get(name);
        if (method == null) {
            return Optional.empty();
        }
        return Optional.of(method.getDeclaredAnnotation(Cache.class));
    }

    /**
     * 是否允许缓存
     *
     * @param point  切点
     * @param cache  缓存注解
     * @param result 结果
     * @return 是否允许缓存
     */
    private static boolean allowCache(ProceedingJoinPoint point, Cache cache, Object result) {
        if (cache.cacheFail()) {
            return true;
        }
        if (result == null) {
            return false;
        }
        if (StringUtils.isNotBlank(cache.isFail())) {
            Method method = ((MethodSignature) point.getSignature()).getMethod();
            MethodUtil.setSpelContextVariable(Constant.RETURN_VALUE, result);
            return MethodUtil.getSPEL(point.getArgs(), method, cache.isFail(), Boolean.class);
        }
        return DefaultResultParser.INSTANCE.test(result);
    }

    public static Object invoke(ProceedingJoinPoint point, String name, String key) throws Throwable {
        Object result = null;
        try {
            result = point.proceed();
            return result;
        } finally {
            Optional<Cache> optional = getCache(name);
            if (optional.isPresent()) {
                Cache cache = optional.get();
                if (allowCache(point, cache, result)) {
                    CacheApi cacheAPI = CacheUtil.getCacheAPI(name);
                    cacheAPI.set(key, result, getExpire(cache.expire()));
                }
            }
        }
    }

    /**
     * 获取cache的过期时间
     *
     * @param expire 过期时间
     * @return 过期时间
     */
    private static int getExpire(int expire) {
        if (expire != cacheConfig.getExpire()) {
            return expire;
        }
        return Constant.EXPIRE;
    }
}
