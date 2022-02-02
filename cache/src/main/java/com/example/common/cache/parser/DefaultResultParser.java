package com.example.common.cache.parser;

import java.util.function.Predicate;

/**
 * @Author xiapeng
 * @Date: 2022/02/03/12:46 上午
 * @Description:
 */
public enum DefaultResultParser implements Predicate<Object> {
    INSTANCE;

    @Override
    public boolean test(Object o) {
        return o == null ? false : true;
    }
}
