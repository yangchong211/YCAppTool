package com.yc.apploglib.printer;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     email : yangchong211@163.com
 *     time  : 2018/11/9
 *     desc  : 延迟初始化
 *     revise:
 * </pre>
 */
public final class DelayInitializer<T> {

    private volatile T mT = null;

    private final Creator<T> mCreator;

    public DelayInitializer(Creator<T> creator) {
        mCreator = creator;
        if (mCreator == null) {
            throw new IllegalArgumentException("creator null!");
        }
    }

    public T get() {
        if (mT == null) {
            synchronized (this) {
                if (mT == null) {
                    mT = mCreator.create();
                    if (mT == null) {
                        throw new IllegalArgumentException("creator return null!");
                    }
                }
            }
        }
        return mT;
    }

    public interface Creator<T> {

        T create();
    }
}

