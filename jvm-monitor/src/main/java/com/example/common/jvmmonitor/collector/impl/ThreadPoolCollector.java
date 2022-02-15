package com.example.common.jvmmonitor.collector.impl;

import com.example.common.jvmmonitor.collector.Collector;
import com.example.common.jvmmonitor.entity.Pair;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Supplier;

/**
 * @author xianpeng.xia
 * on 2022/2/15 10:13 下午
 * 线程池收集器
 */
public class ThreadPoolCollector implements Collector {

    private final String name;
    private final ThreadPoolExecutor executor;
    private final Supplier<Pair<String, String>> tagSupplier;
    private final String prefix;
    private final Pair<Long, Long> pair = new Pair<>(0L, 0L);

    public ThreadPoolCollector(String name, ThreadPoolExecutor executor, Supplier<Pair<String, String>> tagSupplier) {
        this.name = name;
        this.executor = executor;
        this.tagSupplier = tagSupplier;
        this.prefix = "JVM." + name + "ThreadPool.";
    }

    @Override
    public Map<String, Number> collect() {
        long taskCount = executor.getTaskCount() - pair.getK();
        long completedTaskCount = executor.getCompletedTaskCount() - pair.getV();
        pair.setK(taskCount);
        pair.setV(completedTaskCount);

        Map<String, Number> map = new HashMap<>(3);
        map.put(prefix + "Size.Core", executor.getCorePoolSize());
        map.put(prefix + "Size.Max", executor.getMaximumPoolSize());
        map.put(prefix + "Size.Largest", executor.getLargestPoolSize());
        map.put(prefix + "Size.Current", executor.getPoolSize());
        map.put(prefix + "Active", executor.getActiveCount());
        map.put(prefix + "Task.Count", taskCount);
        map.put(prefix + "Task.Completed", completedTaskCount);

        return map;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Map<String, String> tags() {
        Map<String, String> map = new HashMap<>();
        map.put(tagSupplier.get().getK(), tagSupplier.get().getV());
        return map;
    }
}
