package com.yc.ycthreadpoollib.debug;

public final class ThreadInfoBean {

    private final int hashCode;
    protected final String threadName;

    ThreadInfoBean(int hashCode, String threadName) {
        this.hashCode = hashCode;
        this.threadName = threadName;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ThreadInfoBean)) {
            return false;
        }

        final ThreadInfoBean another = (ThreadInfoBean) o;
        return another.hashCode == hashCode && threadName.equals(another.threadName);
    }

}
