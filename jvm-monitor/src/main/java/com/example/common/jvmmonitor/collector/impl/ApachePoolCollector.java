package com.example.common.jvmmonitor.collector.impl;

import com.example.common.jvmmonitor.collector.Collector;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import javafx.util.Pair;
import org.apache.commons.pool2.impl.GenericObjectPool;

/**
 * @author xianpeng.xia
 * on 2022/2/15 9:57 下午
 * ApachePool收集器
 */
public class ApachePoolCollector implements Collector {

    private final String name;
    private final GenericObjectPool pool;
    private final Supplier<Pair<String, String>> tagSupplier;
    private final String prefix;

    public ApachePoolCollector(String name, GenericObjectPool pool, Supplier<Pair<String, String>> tagSupplier) {
        this.name = name;
        this.pool = pool;
        this.tagSupplier = tagSupplier;
        this.prefix = "JVM." + name + "Pool.";
    }

    @Override
    public Map<String, Number> collect() {
        Map<String, Number> map = new HashMap<>(3);
        map.put(prefix + "Active", pool.getNumActive());
        map.put(prefix + "Idle", pool.getNumIdle());
        map.put(prefix + "Waiters", pool.getNumWaiters());
        map.put(prefix + "BorrowWaitTime", pool.getMeanBorrowWaitTimeMillis());
        return map;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Map<String, String> tags() {
        Map<String, String> map = new HashMap<>(1);
        map.put(tagSupplier.get().getKey(), tagSupplier.get().getValue());
        return map;
    }

}
