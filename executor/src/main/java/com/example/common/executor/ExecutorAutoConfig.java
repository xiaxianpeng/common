package com.example.common.executor;

import com.example.common.base.iface.ConfigureAble;
import com.example.common.executor.ExecutorsConfig.ExecutorConfig;
import com.example.common.util.NamedThreadFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author xianpeng.xia
 * on 2022/3/5 5:21 PM
 * 注册线程池配置
 */
@Slf4j
public class ExecutorAutoConfig implements ConfigureAble {

    private final Map<String, ExecutorService> map = new HashMap<>(5);
    private static final String BEAN_NAME = "executorAutoConfig";

    public ExecutorService getExecutor(String name) {
        ExecutorService executorService = map.get(name);
        return executorService == null ? create(name, new ExecutorConfig(name)) : executorService;
    }

    private ExecutorService create(String name, ExecutorsConfig.ExecutorConfig executorConfig) {
        RejectedExecutionHandler rejectedExecutionHandler = null;
        switch (executorConfig.getPolicy()) {
            case "A":
                rejectedExecutionHandler = new AbortPolicy();
                break;
            case "C":
                rejectedExecutionHandler = new CallerRunsPolicy();
                break;
            case "D":
                rejectedExecutionHandler = new DiscardPolicy();
                break;
            default:
                break;
        }
        //  自定义的拒绝策略，异常时直接抛出，尽早让用户介入处理
        if (rejectedExecutionHandler == null) {
            if (StringUtils.isNotBlank(executorConfig.getPolicy())) {
                try {
                    Class<RejectedExecutionHandler> rejectedExecutionHandlerClass = (Class<RejectedExecutionHandler>) Class.forName(executorConfig.getPolicy());
                    rejectedExecutionHandler = rejectedExecutionHandlerClass.newInstance();
                } catch (Exception e) {
                    log.error("线程池:{} 自定义拒绝策略:{} 初始化失败", name, executorConfig.getPolicy(), e);
                }
            } else {
                rejectedExecutionHandler = new DiscardPolicy();
            }
        }
        BlockingQueue<Runnable> blockingQueue = executorConfig.getQueueSize() <= 0 ?
            new LinkedBlockingQueue<>() : new ArrayBlockingQueue<>(executorConfig.getQueueSize());
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            executorConfig.getCorePoolSize(),
            executorConfig.getMaximumPoolSize(),
            executorConfig.getKeepAlive(),
            TimeUnit.MILLISECONDS,
            blockingQueue,
            new NamedThreadFactory(name),
            rejectedExecutionHandler);

        if (executorConfig.isTransferContext()) {
            return new ExecutorServiceWrapper(executor);
        }
        return executor;
    }

    @Override
    public void init() {

    }

    @Override
    public void close() {

    }

    @Override
    public void refresh() {

    }
}
