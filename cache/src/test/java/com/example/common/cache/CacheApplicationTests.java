package com.example.common.cache;

import com.example.common.cache.api.impl.CaffeineCache;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class CacheApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void CaffeineCacheTest() {
        CaffeineCache cache = new CaffeineCache(5);
        String t1 = cache.get("t1", String.class);
        log.info("t1={}", t1);
        cache.set("t1", "1", 2);
        t1 = cache.get("t1", String.class);
        log.info("t1={}", t1);
    }
}
