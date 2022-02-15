package com.example.common.jvmmonitor.collector.impl;

import com.example.common.jvmmonitor.collector.Collector;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xianpeng.xia
 * on 2022/2/15 11:17 下午
 *
 * NioBufferPool
 */
@Slf4j
public enum NioBufferPool implements Collector {
    INSTANCE;
    private ObjectName directNioBufferPoolObjectName;
    private ObjectName mappedNioBufferPoolObjectName;
    private static final String PREFIX = "JVM.Nio.Buffer.";

    NioBufferPool() {
        try {
            directNioBufferPoolObjectName = new ObjectName("java.nio:type=BufferPool,name=direct");
        } catch (MalformedObjectNameException e) {
            //do nothing
        }

        try {
            mappedNioBufferPoolObjectName = new ObjectName("java.nio:type=BufferPool,name=mapped");
        } catch (MalformedObjectNameException e) {
            //do nothing
        }
    }

    @Override
    public Map<String, Number> collect() {
        Map<String, Number> map = new HashMap<>(10);
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        map.putAll(sampleBufferPool(mBeanServer, directNioBufferPoolObjectName));
        map.putAll(sampleBufferPool(mBeanServer, mappedNioBufferPoolObjectName));
        return map;
    }

    private Map<String, Number> sampleBufferPool(MBeanServer mBeanServer, ObjectName objectName) {
        Map<String, Number> map = new HashMap<>(10);

        try {
            String name = (String) mBeanServer.getAttribute(objectName, "Name");
            Number totalCapacity = (Number) mBeanServer.getAttribute(objectName, "TotalCapacity");
            Number memoryUsed = (Number) mBeanServer.getAttribute(objectName, "MemoryUsed");
            Number count = (Number) mBeanServer.getAttribute(objectName, "Count");

            map.put(PREFIX + name + ".TotalCapacity", totalCapacity);
            map.put(PREFIX + name + ".MemoryUsed", memoryUsed);
            map.put(PREFIX + name + ".Count", count);
        } catch (MBeanException | AttributeNotFoundException | InstanceNotFoundException | ReflectionException e) {
            log.error("fetch nio buffer pool mbean attr failed", e);
        }
        return map;
    }
}
