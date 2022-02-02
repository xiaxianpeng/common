package com.example.common.cache.parser;

import com.example.common.cache.constants.Constant;
import com.example.common.util.JsonUtil;

import java.util.function.Function;

/**
 * @Author xiapeng
 * @Date: 2022/02/03/12:40 上午
 * @Description:
 */
public enum DefaultKeyGenerator implements Function<Object[], String> {
    INSTANCE;

    @Override
    public String apply(Object[] args) {
        if (args == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < args.length; i++) {
            if (args[i] == null) {
                continue;
            }
            String key = args[i] instanceof String ? args[i].toString() : JsonUtil.toString(args[i]);
            if (i == args.length - 1) {
                sb.append(key);
            } else {
                sb.append(key).append(Constant.KEY_CONNECTOR);
            }
        }
        return sb.toString();
    }
}
