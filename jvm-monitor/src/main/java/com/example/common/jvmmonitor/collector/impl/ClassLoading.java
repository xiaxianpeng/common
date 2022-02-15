package com.example.common.jvmmonitor.collector.impl;

import com.example.common.jvmmonitor.collector.Collector;
import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xianpeng.xia
 * on 2022/2/15 10:53 下午
 * 类加载收集器
 */
public enum ClassLoading implements Collector {
    /**
     * 实例
     */
    INSTANCE;
    private final ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();
    private static final String PREFIX = "JVM.classLoading.";

    @Override
    public Map<String, Number> collect() {
        Map<String, Number> map = new HashMap<>(3);
        map.put(PREFIX + "LoadedClassCount", classLoadingMXBean.getLoadedClassCount());
        map.put(PREFIX + "TotalLoadedClassCount", classLoadingMXBean.getTotalLoadedClassCount());
        map.put(PREFIX + "UnloadedClassCount", classLoadingMXBean.getUnloadedClassCount());
        return map;
    }

}
