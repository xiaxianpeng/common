package com.example.common.cache.annotation;

import com.example.common.cache.constants.Constant;
import com.example.common.cache.enums.CacheEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author xiapeng
 * @Date: 2022/02/02/11:18 下午
 * @Description: 缓存注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
public @interface Cache {
    /**
     * 过期时间单位s，默认1h
     *
     * @return 过期时间
     */
    int expire() default Constant.EXPIRE;

    /**
     * cache类型
     *
     * @return cache 类型
     */
    CacheEnum type() default CacheEnum.CAFFEINE;

    /**
     * cache名
     *
     * @return cache 名
     */
    String name() default "";

    /**
     * key的前缀
     *
     * @return key的前缀
     */
    String prefix() default "";

    /**
     * key的spel表达式
     *
     * @return key的spel表达式
     */
    String key() default "";

    /**
     * condition的spel表达式
     *
     * @return condition的spel表达式
     */
    String condition() default "";

    boolean cacheFail() default false;

    String isFail() default "";
}
