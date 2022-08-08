package com.yc.toolmemorylib;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2018/5/6
 *     desc  : 手机RAM内存信息，物理内存信息
 *     revise:
 * </pre>
 */
public class RamMemoryInfo {

    /**
     * 可用RAM
     */
    public long availMem;
    /**
     * 手机总RAM
     */
    public long totalMem;
    /**
     * 内存占用满的阀值，超过即认为低内存运行状态，可能会Kill process
     */
    public long lowMemThreshold;
    /**
     * 是否低内存状态运行
     */
    public boolean isLowMemory;

}
