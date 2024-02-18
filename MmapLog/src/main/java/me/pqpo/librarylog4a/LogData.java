package me.pqpo.librarylog4a;


/**
 * Created by pqpo on 2017/11/21.
 */
public class LogData {

    public int logLevel;
    public String tag;
    public String msg;

    private LogData next;

    private static final Object sPoolSync = new Object();
    private static LogData sPool;
    private static int sPoolSize = 0;
    private static final int MAX_POOL_SIZE = 50;

    public static LogData obtain() {
        synchronized (sPoolSync) {
            if (sPool != null) {
                LogData m = sPool;
                sPool = m.next;
                m.next = null;
                sPoolSize--;
                return m;
            }
        }
        return new LogData();
    }

    public static LogData obtain(int logLevel, String tag, String msg) {
        LogData obtain = obtain();
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
