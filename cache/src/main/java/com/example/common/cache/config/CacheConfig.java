package com.example.common.cache.config;

import com.example.common.cache.constants.Constant;
import com.example.common.cache.util.CacheUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;

/**
 * @Author xiapeng
 * @Date: 2022/02/03/12:32 上午
 * @Description:cache配置
 */
@ConfigurationProperties(prefix = "cache")
@Data
public class CacheConfig {
    private int expire = Constant.EXPIRE;
    private String prefix = "DD";

    @PostConstruct
    public void init() {
        CacheUtil.setCacheConfig(this);
    }
}
