package com.example.common.cache.aspect;

import com.example.common.cache.annotation.Cache;
import com.example.common.cache.annotation.CacheRemove;
import com.example.common.cache.annotation.CacheUpdate;
import com.example.common.cache.api.CacheApi;
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
 * @Date: 2022/02/03/2:35 下午
 * @Description:
 */
@Aspect
@Order(0)
@Slf4j
public class CacheRemoveAspect {
    @Pointcut("@annotation(com.example.common.cache.annotation.CacheUpdate)")
    public void aspect() {
        //just empty
    }

    @SuppressWarnings("unchecked")
    @Around("aspect()&&@annotation(cacheRemove)")
    public Object handleCached(ProceedingJoinPoint point, CacheRemove cacheRemove) throws Throwable {
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        String condition = cacheRemove.condition();
        boolean conditionNotPassed = StringUtils.isNotBlank(condition)
                && !MethodUtil.getSPEL(point.getArgs(), method, condition, Boolean.class);
        // 如果spel条件不通过
        if (conditionNotPassed) {
            return point.proceed();
        }
        String name = cacheRemove.name();
        Optional<Cache> optional = CacheUtil.getCache(name);
        if (!optional.isPresent()) {
            return point.proceed();
        }
        String key = CacheUtil.getKey(method, cacheRemove.key(), optional.get().prefix(), point.getArgs());
        CacheApi cacheApi = CacheUtil.getCacheAPI(name);
        cacheApi.remove(key);
        return point.proceed();
    }
}
