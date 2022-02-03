package com.example.common.cache.aspect;

import com.example.common.cache.annotation.Cache;
import com.example.common.cache.annotation.CacheUpdate;
import com.example.common.cache.util.CacheUtil;
import com.example.common.util.MethodUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @Author xiapeng
 * @Date: 2022/02/03/2:24 下午
 * @Description:缓存更新aop
 */
@Aspect
@Slf4j
@Order(0)
public class CacheUpdateAspect {
    @Pointcut("@annotation(com.example.common.cache.annotation.CacheUpdate)")
    public void aspect() {
        //just empty
    }

    @SuppressWarnings("unchecked")
    @Around("aspect()&&@annotation(cacheUpdate)")
    public Object handleCached(ProceedingJoinPoint point, CacheUpdate cacheUpdate) throws Throwable {
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        String condition = cacheUpdate.condition();
        boolean conditionNotPassed = StringUtils.isNotBlank(condition)
                && !MethodUtil.getSPEL(point.getArgs(), method, condition, Boolean.class);
        // 如果spel条件不通过
        if (conditionNotPassed) {
            return point.proceed();
        }
        String name = cacheUpdate.name();
        Optional<Cache> optional = CacheUtil.getCache(name);
        if (!optional.isPresent()) {
            return point.proceed();
        }
        Cache cache = optional.get();
        String key = CacheUtil.getKey(method, cacheUpdate.key(), cache.prefix(), point.getArgs());
        return CacheUtil.invoke(point, name, key);
    }
}
