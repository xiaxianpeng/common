package com.example.common.executor;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xianpeng.xia
 * on 2022/3/5 4:51 PM
 * 线程池配置
 */
@ConfigurationProperties(prefix = Constant.PREFIX)
@Data
public class ExecutorsConfig {

    public List<ExecutorConfig> configs = new ArrayList<>();
    private String names;

    public List<ExecutorConfig> getConfigs() {
        return configs == null ? new ArrayList<>(10) : configs;
    }

    @Data
    public static class ExecutorConfig {

        /**
         * 名字
         */
        private String name;
        /**
         * 固定线程数，默认cpu个数，计时空闲也不会被回收
         */
        private int corePoolSize = Constant.CORES;
        /**
         * 固定线程数，默认cpu个数，计时空闲也不会被回收
         */
        private int maximumPoolSize = corePoolSize * 2;

        /**
         * 最大空闲时间，单位ms
         */
        private int keepAlive = 10000;
        /**
         * 拒绝策略，默认直接丢弃
         */
        private String policy = "D";
        /**
         * 队列长度，默认为0，会实现为LinkedBolckingQueue
         */
        private int queueSize = 0;
        /**
         * 是否透传上下文信息到子线程
         */
        private boolean transferContext = true;

        public ExecutorConfig() {
        }

        public ExecutorConfig(String name) {
            this.name = name;
        }
    }

}
