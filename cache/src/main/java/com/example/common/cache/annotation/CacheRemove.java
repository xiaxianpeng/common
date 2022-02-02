package com.example.common.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author xiapeng
 * @Date: 2022/02/02/11:19 下午
 * @Description:缓存删除注解
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
public @interface CacheRemove {
    /**
     * cache名，必须和设置cache的name相同
     *
     * @return cache名
     */
    String name();

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

}
