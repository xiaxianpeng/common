package com.example.common.jvmmonitor.collector.impl;

import com.example.common.jvmmonitor.collector.Collector;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author xianpeng.xia
 * on 2022/2/15 11:47 下午
 * tomcat收集器
 */
@Slf4j
public enum Tomcat implements Collector {
    INSTANCE;
    private static final String PREFIX = "JVM.Tomcat.";

    @Override
    public Map<String, Number> collect() {
        Map<String, Number> map = new HashMap<>();
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

        try {
            Set<ObjectName> objectNames = mBeanServer.queryNames(new ObjectName("Tomcat:type=ThreadPool,*"), null);
            if (objectNames == null || objectNames.size() <= 0) {
                return map;
            }
            //会取到两个，subType= 取不到值
            //为防止tomcat版本变动后面可能会取到多个的情况，只取第一个
            for (ObjectName objectName : objectNames) {
                if (StringUtils.containsIgnoreCase(objectName.getCanonicalName(), "subType=")) {
                    continue;
                }
                Integer maxThreads = (Integer) mBeanServer.getAttribute(objectName, "maxThreads");
                Integer currentThreadCount = (Integer) mBeanServer.getAttribute(objectName, "currentThreadCount");
                Integer currentThreadBusy = (Integer) mBeanServer.getAttribute(objectName, "currentThreadBusy");
                Integer connectionCount = (Integer) mBeanServer.getAttribute(objectName, "connectionCount");
                Integer maxConnections = (Integer) mBeanServer.getAttribute(objectName, "maxConnections");

                map.put(PREFIX + "maxThreads", maxThreads);
                map.put(PREFIX + "currentThreadCount", currentThreadCount);
                map.put(PREFIX + "currentThreadBusy", currentThreadBusy);
                map.put(PREFIX + "connectionCount", connectionCount);
                map.put(PREFIX + "maxConnections", maxConnections);

                break;
            }
        } catch (Exception e) {
            log.error("无法获取Tomcat JVM指标", e);
        }
        return map;
    }
}
