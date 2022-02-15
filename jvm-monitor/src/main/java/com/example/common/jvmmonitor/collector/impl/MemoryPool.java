package com.example.common.jvmmonitor.collector.impl;

import com.example.common.jvmmonitor.collector.Collector;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xianpeng.xia
 * on 2022/2/15 11:11 下午
 * 内存池收集器
 */
public enum MemoryPool implements Collector {
    /**
     * 实例
     */
    INSTANCE;

    private List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
    private static final String PREFIX = "JVM.MemoryPool.";

    @Override
    public Map<String, Number> collect() {
        Map<String, Number> map = new HashMap<>();
        for (MemoryPoolMXBean mxBean : memoryPoolMXBeans) {
            String memoryPoolName = mxBean.getName();
            MemoryUsage memoryUsage = mxBean.getUsage();
            map.put(PREFIX + memoryPoolName + ".Init", memoryUsage.getInit());
            map.put(PREFIX + memoryPoolName + ".Used", memoryUsage.getUsed());
            map.put(PREFIX + memoryPoolName + ".Committed", memoryUsage.getCommitted());
            map.put(PREFIX + memoryPoolName + ".Max", memoryUsage.getMax());
        }
        return map;
    }
}
