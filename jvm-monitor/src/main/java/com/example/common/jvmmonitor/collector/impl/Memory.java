package com.example.common.jvmmonitor.collector.impl;

import com.example.common.jvmmonitor.collector.Collector;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xianpeng.xia
 * on 2022/2/15 11:05 下午
 * 内存收集器
 */
public enum Memory implements Collector {
    /**
     * 实例
     */
    INSTANCE;
    private MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
    private static final String PREFIX = "JVM.Memory.";

    @Override
    public Map<String, Number> collect() {
        Map<String, Number> map = new HashMap<>(10);
        map.put(PREFIX + "ObjectPendingFinalizationCount", memoryMXBean.getObjectPendingFinalizationCount());

        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        map.put(PREFIX + "Heap.Init", heapMemoryUsage.getInit());
        map.put(PREFIX + "Heap.Max", heapMemoryUsage.getMax());
        map.put(PREFIX + "Heap.Used", heapMemoryUsage.getUsed());
        map.put(PREFIX + "Heap.Committed", heapMemoryUsage.getCommitted());

        MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();
        map.put(PREFIX + "NonHeap.Init", nonHeapMemoryUsage.getInit());
        map.put(PREFIX + "NonHeap.Max", nonHeapMemoryUsage.getMax());
        map.put(PREFIX + "NonHeap.Used", nonHeapMemoryUsage.getUsed());
        map.put(PREFIX + "NonHeap.Committed", nonHeapMemoryUsage.getCommitted());

        return map;
    }
}
