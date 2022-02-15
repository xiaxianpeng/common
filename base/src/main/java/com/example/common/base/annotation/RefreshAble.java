package com.example.common.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xianpeng.xia
 * on 2022/2/16 12:28 上午
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
public @interface RefreshAble {

    /**
     * 观察的key的前缀，如果包含多个，以","隔开
     *
     * @return 返回被观察的prefix
     */
    String prefix() default "";

    /**
     * 观察的key，如果包含多个，以","隔开
     *
     * @return 返回被观察的key
     */
    String key() default "";
}
