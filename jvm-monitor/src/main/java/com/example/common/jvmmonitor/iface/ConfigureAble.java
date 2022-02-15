package com.example.common.jvmmonitor.iface;

/**
 * @author xianpeng.xia
 * on 2022/2/16 12:08 上午
 */
public interface ConfigureAble {

    /**
     * 初始化
     */
    void init();

    /**
     * 关闭
     */
    void close();

    /**
     * 刷新
     */
    void refresh();
}
