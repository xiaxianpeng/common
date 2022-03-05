package com.example.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xianpeng.xia
 * on 2022/3/5 5:45 PM
 * 上下文工具类
 */
public class ContextUtil {

    public static final ThreadLocal<Map<String, Object>> CONTEXT = ThreadLocal.withInitial(() -> {
        Map<String, Object> map = new HashMap<>(10);
        return map;
    });

    private ContextUtil() {
    }

    /**
     * 根据key获取上下文的值
     *
     * @param key key
     * @return 上下文的值
     */
    public static Object get(String key) {
        return CONTEXT.get().get(key);
    }

    /**
     * 获取上下文
     *
     * @return 上下文
     */
    public static Map<String, Object> getAll() {
        return CONTEXT.get();
    }

    /**
     * 设置上下文
     *
     * @return 上下文
     */
    public static void put(String key, Object value) {
        if (value == null) {
            return;
        }
        CONTEXT.get().put(key, value);
    }

    public static void putAll(Map<String, Object> map) {
        if (map == null) {
            return;
        }
        map.forEach(ContextUtil::put);
    }

    public static void remove(String key) {
        CONTEXT.get().remove(key);
    }

    public static void removeAll() {
        CONTEXT.remove();
    }
}
