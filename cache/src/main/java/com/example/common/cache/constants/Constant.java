package com.example.common.cache.constants;

/**
 * @Author xiapeng
 * @Date: 2022/01/30/2:54 上午
 * @Description:
 */
public class Constant {
    private Constant() {
    }

    public static final String SUCCESS = "{} {} success:key={},value={}";
    public static final String FAIL = "{} {} fail:key={}";
    public static final String EMPTY = "";
    /**
     * 默认超时时间1小时
     */
    public static final int EXPIRE = 3600;
    /**
     * 开发和测试默认超时时间1s
     */
    public static final int DEV_EXPIRE = 1;
    /**
     * key连接字符串
     */
    public static final String KEY_CONNECTOR = "_";
    /**
     * 返回值的spel
     */
    public static final String RETURN_VALUE = "returnValue";
}
