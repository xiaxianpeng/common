package com.example.common.jvmmonitor.collector.impl;

import com.example.common.jvmmonitor.collector.Collector;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xianpeng.xia
 * on 2022/2/15 11:34 下午
 * os收集器
 */
public enum Os implements Collector {
    INSTANCE;
    private OperatingSystemMXBean operatingSystemMXBean;
    private static final String PREFIX = "JVM.OS.";

    Os() {
        try {
            operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        } catch (Exception e) {
            operatingSystemMXBean = null;
        }
    }

    @Override
    public Map<String, Number> collect() {
        Map<String, Number> map = new HashMap<>(10);
        if (operatingSystemMXBean == null) {
            return map;
        }
        map.put(PREFIX + "CommittedVirtualMemorySize", operatingSystemMXBean.getCommittedVirtualMemorySize());
        map.put(PREFIX + "FreePhysicalMemorySize", operatingSystemMXBean.getFreePhysicalMemorySize());
        map.put(PREFIX + "FreeSwapSpaceSize", operatingSystemMXBean.getFreeSwapSpaceSize());
        map.put(PREFIX + "ProcessCpuLoad", operatingSystemMXBean.getProcessCpuLoad());
        map.put(PREFIX + "ProcessCpuTime", operatingSystemMXBean.getProcessCpuTime());
        map.put(PREFIX + "SystemCpuLoad", operatingSystemMXBean.getSystemCpuLoad());
        map.put(PREFIX + "SystemLoadAverage", operatingSystemMXBean.getSystemLoadAverage());
        return map;
    }
}
