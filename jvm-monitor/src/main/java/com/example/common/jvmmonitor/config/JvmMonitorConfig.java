package com.example.common.jvmmonitor.config;

import java.util.List;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xianpeng.xia
 * on 2022/2/16 12:04 上午
 * jvm monitor配置信息
 */
@ConfigurationProperties(prefix = "jvm-monitor")
@Data
@Slf4j
public class JvmMonitorConfig {

    private boolean jvmEnable = true;

    /**
     * 指定不收集哪些标准指标
     */
    private List<String> exclude;
}
