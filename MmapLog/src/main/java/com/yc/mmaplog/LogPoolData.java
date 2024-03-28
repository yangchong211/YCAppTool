package com.yc.mmaplog;


/**
 * @author yangchong
 * GitHub : <a href="https://github.com/yangchong211">...</a>
 * email  : yangchong211@163.com
 * time  : 2017/09/23
 * desc  : 日志实体data
 * revise:
 */
public class LogPoolData {

    public int logLevel;
    public String tag;
    public String msg;
    private LogPoolData next;
    private static final Object sPoolSync = new Object();
    private static LogPoolData sPool;
    private static int sPoolSize = 0;
    private static final int MAX_POOL_SIZE = 50;

    public static LogPoolData obtain() {
        synchronized (sPoolSync) {
            if (sPool != null) {
                LogPoolData m = sPool;
                sPool = m.next;
                m.next = null;
                sPoolSize--;
                return m;
            }
        }
        return new LogPoolData();
    }

    public static LogPoolData obtain(int logLevel, String tag, String msg) {
        LogPoolData obtain = obtain();
        obtain.logLevel = logLevel;
        obtain.tag = tag;
        obtain.msg = msg;
        return obtain;
    }

    public void recycle() {
        logLevel = 0;
        tag = null;
        msg = null;
        synchronized (sPoolSync) {
            if (sPoolSize < MAX_POOL_SIZE) {
                next = sPool;
                sPool = this;
                sPoolSize++;
            }
        }
    }

}
