package com.example.common.jvmmonitor.collector.impl;

import com.example.common.jvmmonitor.collector.Collector;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xianpeng.xia
 * on 2022/2/15 10:58 下午
 * gc收集器
 */
public enum Gc implements Collector {

    /**
     * 实例
     */
    INSTANCE;
    private final List<GarbageCollectorMXBean> garbageCollectorMXBeans = ManagementFactory.getGarbageCollectorMXBeans();
    private final Map<String, Long> gcCountMap = new HashMap<>(garbageCollectorMXBeans.size());
    private static final String PREFIX = "JVM.GC.";

    Gc() {
        for (GarbageCollectorMXBean mxBean : garbageCollectorMXBeans) {
            String memoryManagerName = mxBean.getName();
            gcCountMap.put(memoryManagerName, mxBean.getCollectionCount());
        }
    }

    @Override
    public Map<String, Number> collect() {
        Map<String, Number> map = new HashMap<>(10);
        for (GarbageCollectorMXBean mxBean : garbageCollectorMXBeans) {
            String memoryManagerName = mxBean.getName();
            Long oldCount = gcCountMap.get(memoryManagerName);
            Long newCount = mxBean.getCollectionCount();
            gcCountMap.put(memoryManagerName, newCount);

            map.put(PREFIX + memoryManagerName + ".GcCount", newCount - oldCount);
            map.put(PREFIX + memoryManagerName + ".GcTime", mxBean.getCollectionTime());
        }
        return map;
    }
}
