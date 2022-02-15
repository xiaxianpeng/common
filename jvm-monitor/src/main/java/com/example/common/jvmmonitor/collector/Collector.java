package com.example.common.jvmmonitor.collector;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author xianpeng.xia
 * on 2022/2/15 9:51 下午
 * 采集器
 */
public interface Collector {

    Set<Collector> COLLECTORS = new HashSet<>(10);

    /**
     * 采集的指标
     *
     * @return 采集的指标
     */
    Map<String, Number> collect();

    /**
     * 名称
     *
     * @return 采集器的名称
     */
    default String name() {
        return this.getClass().getSimpleName();
    }


    /**
     * 名称
     *
     * @return 采集器的名称
     */
    default Map<String, String> tags() {
        return null;
    }

    /**
     * 注册
     */
    default void register() {
        COLLECTORS.add(this);
    }


    /**
     * 注册
     */
    static void register(Collector collector) {
        COLLECTORS.add(collector);
    }
}
