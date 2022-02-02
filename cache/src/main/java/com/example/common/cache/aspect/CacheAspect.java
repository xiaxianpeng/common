package com.example.common.cache.aspect;

import com.example.common.cache.annotation.Cache;
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

import java.lang.reflect.Method;

/**
 * @Author xiapeng
 * @Date: 2022/02/02/11:43 下午
 * @Description:缓存aop
 */
@Aspect
@Slf4j
public class CacheAspect {
    @Pointcut("@annotation(com.example.common.cache.annotation.Cache)")
    public void aspect() {
        // just empty
    }

    /**
     * cache aop
     *
     * @param point 连接点
     * @param cache 缓存
     * @return 执行接口
     * @throws Throwable 可抛出类型
     */
    @SuppressWarnings("unchecked")
    @Around("aspect()&&@annotation(cache)")
    public Object handleCached(ProceedingJoinPoint point, Cache cache) throws Throwable {
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        String condition = cache.condition();
        boolean conditionNotPassed = StringUtils.isNotBlank(condition)
                && !MethodUtil.getSPEL(point.getArgs(), method, condition, Boolean.class);
        // 如果spel条件不通过
        if (conditionNotPassed) {
            return point.proceed();
        }
        String name = CacheUtil.getName(method);
        String key = CacheUtil.getKey(name, point.getArgs());
        CacheApi cacheApi = CacheUtil.getCacheAPI(name);
        Object result = cacheApi.get(key, method.getReturnType());
        log.debug("hit cache,key={},value={}", key, result);
        if(result!=null){
            return result;
        }
        return null;
    }
}
