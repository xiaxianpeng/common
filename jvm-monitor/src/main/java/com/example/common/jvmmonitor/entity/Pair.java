package com.example.common.jvmmonitor.entity;

import lombok.Data;

/**
 * @author xianpeng.xia
 * on 2022/2/15 10:20 下午
 */
@Data
public class Pair<K, V> {

    private K k;
    private V v;

    public Pair() {
    }

    public Pair(K k, V v) {
        this.k = k;
        this.v = v;
    }

    public static <K, V> Pair<K, V> of(K k, V v) {
        return new Pair<>(k, v);
    }
}
