package com.yc.toolutils.memory;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2018/5/6
 *     desc  : Dalvik堆内存，只要App用到的内存都算（包括共享内存）
 *     revise:
 * </pre>
 */
public final class DalvikHeapMem {

    public long freeMem;
    public long maxMem;
    public long allocated;

}
