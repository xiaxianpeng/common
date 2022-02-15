package com.example.common.util;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xianpeng.xia
 * on 2022/2/16 12:14 上午
 */
@Slf4j
public class NamedThreadFactory implements ThreadFactory {

    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(0);
    private final String namePrefix;
    private final boolean daemon;
    private final Integer priority;
    private final Thread.UncaughtExceptionHandler exceptionHandler;

    /**
     * @param name 名字
     */
    public NamedThreadFactory(String name) {
        SecurityManager securityManager = System.getSecurityManager();
        group = (securityManager != null) ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
        namePrefix = name;
        daemon = false;
        priority = Thread.NORM_PRIORITY;
        exceptionHandler = (thread, exception) -> log.error(thread.getName(), exception);
    }

    public NamedThreadFactory(ThreadGroup group, String namePrefix, boolean daemon, Integer priority, UncaughtExceptionHandler exceptionHandler) {
        this.group = group;
        this.namePrefix = namePrefix;
        this.daemon = daemon;
        this.priority = priority;
        this.exceptionHandler = exceptionHandler;
    }

    /**
     * 创建新线程的方法
     */
    @Override
    public Thread newThread(Runnable runnable) {
        String name = namePrefix + "-thread-" + threadNumber.incrementAndGet();
        Thread thread = new Thread(group, runnable, name, 0);
        thread.setDaemon(daemon);
        thread.setPriority(priority);
        thread.setUncaughtExceptionHandler(exceptionHandler);
        return thread;
    }
}
