package com.example.common.executor;

import com.example.common.util.ContextUtil;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * @author xianpeng.xia
 * on 2022/3/5 5:40 PM
 * 静态代理后的线程池，透传上下文信息到子线程中
 */
public class ExecutorServiceWrapper implements ExecutorService {

    private final ExecutorService executorService;

    /**
     * 包私有的线程池包装类构造器
     *
     * @param executorService 线程池
     */
    ExecutorServiceWrapper(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public void shutdown() {
        executorService.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return executorService.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return executorService.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return executorService.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return executorService.awaitTermination(timeout, unit);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return executorService.submit(get(task, getContextMap()));
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return executorService.submit(get(task, getContextMap()), result);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return executorService.submit(get(task, getContextMap()));
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return executorService.invokeAll(tasks.stream().map(callable -> get(callable, getContextMap())).collect(Collectors.toList()));
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return executorService.invokeAll(tasks.stream().map(callable -> get(callable, getContextMap())).collect(Collectors.toList()), timeout, unit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return executorService.invokeAny(tasks.stream().map(callable -> get(callable, getContextMap())).collect(Collectors.toList()));
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return executorService.invokeAny(tasks.stream().map(callable -> get(callable, getContextMap())).collect(Collectors.toList()), timeout, unit);
    }

    @Override
    public void execute(Runnable command) {
        executorService.execute(get(command, getContextMap()));
    }

    private <T> Callable<T> get(Callable<T> callable, Map<String, Object> parentContextMap) {
        return () -> {
            if (ContextUtil.CONTEXT.get() == null) {
                ContextUtil.CONTEXT.get().clear();
            }
            ContextUtil.putAll(parentContextMap);
            return callable.call();
        };
    }

    private Runnable get(Runnable runnable, Map<String, Object> parentContextMap) {
        return () -> {
            if (ContextUtil.CONTEXT.get() == null) {
                ContextUtil.CONTEXT.get().clear();
            }
            ContextUtil.putAll(parentContextMap);
            runnable.run();
        };
    }

    private Map<String, Object> getContextMap() {
        return new HashMap<>(ContextUtil.getAll());
    }
}
