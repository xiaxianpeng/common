package com.example.common.jvmmonitor.config;

import com.example.common.jvmmonitor.annotation.RefreshAble;
import com.example.common.jvmmonitor.collector.Collector;
import com.example.common.jvmmonitor.collector.impl.ClassLoading;
import com.example.common.jvmmonitor.collector.impl.Gc;
import com.example.common.jvmmonitor.collector.impl.Memory;
import com.example.common.jvmmonitor.collector.impl.MemoryPool;
import com.example.common.jvmmonitor.collector.impl.NioBufferPool;
import com.example.common.jvmmonitor.collector.impl.Os;
import com.example.common.jvmmonitor.collector.impl.Thread;
import com.example.common.jvmmonitor.collector.impl.Tomcat;
import com.example.common.jvmmonitor.iface.ConfigureAble;
import com.example.common.util.NamedThreadFactory;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

/**
 * @author xianpeng.xia
 * on 2022/2/16 12:07 上午
 */
@Import({JvmMonitorConfig.class})
@Component
@Data
@Slf4j
public class JvmMonitorAutoConfig implements ConfigureAble {

    @Resource
    private JvmMonitorConfig jvmMonitorConfig;
    private ScheduledExecutorService scheduledExecutorService;
    private static final String SPACE = "";

    @Override
    public void init() {
        if (!jvmMonitorConfig.isJvmEnable()) {
            return;
        }
        scheduledExecutorService = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("JvmMonitor"));
        ClassLoading.INSTANCE.register();
        Gc.INSTANCE.register();
        Memory.INSTANCE.register();
        MemoryPool.INSTANCE.register();
        NioBufferPool.INSTANCE.register();
        Os.INSTANCE.register();
        Thread.INSTANCE.register();
        Tomcat.INSTANCE.register();
        scheduledExecutorService.scheduleAtFixedRate(this::run, 10, 10, TimeUnit.SECONDS);
        log.info("jvm monitor配置加载成功");
    }

    @Override
    public void close() {
        if (scheduledExecutorService != null && !scheduledExecutorService.isShutdown()) {
            scheduledExecutorService.shutdown();
        }
    }

    @Override
    @RefreshAble(prefix = "jvm-monitor")
    public void refresh() {
        close();
        init();
        log.info("jvm monitor配置刷新成功");
    }

    private void run() {
        List<String> excludeSet = jvmMonitorConfig.getExclude();
        for (Collector collector : Collector.COLLECTORS) {
            if (excludeSet != null && excludeSet.contains(collector.name())) {
                continue;
            }
            Map<String, Number> data = collector.collect();
            if (data == null) {
                continue;
            }
            data.forEach((k, v) -> {
                if (v.doubleValue() <= 0) {
                    return;
                }
                if (k.contains(SPACE)) {
                    k = k.replaceAll(SPACE, "");
                }
                log.info("监控指标,名称:{},值:{}", k, v);
            });
        }
    }
}
