package com.example.common.util;

import org.apache.commons.lang3.StringUtils;

import java.security.InvalidParameterException;

/**
 * @Author xiapeng
 * @Date: 2022/01/31/1:06 上午
 * @Description:
 */
public class ValidateUtil {
    private ValidateUtil() {
    }

    /**
     * 判断某个参数是否为空
     *
     * @param object      参数
     * @param description 参数描述
     */
    public static void notNull(Object object, String description) {
        if (object == null) {
            description = StringUtils.isBlank(description) ? "参数" : description;
            throw new InvalidParameterException(description + "为空");
        }
    }
}
