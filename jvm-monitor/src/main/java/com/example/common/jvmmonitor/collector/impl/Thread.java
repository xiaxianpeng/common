package com.example.common.jvmmonitor.collector.impl;

import com.example.common.jvmmonitor.collector.Collector;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xianpeng.xia
 * on 2022/2/15 11:43 下午
 * 线程收集器
 */
public enum Thread implements Collector {
    INSTANCE;
    private ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    private static final String PREFIX = "JVM.Thread.";

    @Override
    public Map<String, Number> collect() {
        Map<String, Number> map = new HashMap<>(10);
        map.put(PREFIX + "PeakThreadCount", threadMXBean.getPeakThreadCount());
        map.put(PREFIX + "DaemonThreadCount", threadMXBean.getDaemonThreadCount());
        map.put(PREFIX + "TotalStartedThreadCount", threadMXBean.getTotalStartedThreadCount());
        map.put(PREFIX + "ThreadCount", threadMXBean.getThreadCount());
        return map;
    }
}
