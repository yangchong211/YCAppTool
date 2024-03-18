package com.yc.ycthreadpoollib.pool.config;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/05/17
 *     desc  : 枚举
 *     revise:
 * </pre>
 */
public enum ThreadPoolType {
    CACHED,
    FIXED,
    SCHEDULED,
    SINGLE,
    CUSTOM;

    private ThreadPoolType() {
    }
}
