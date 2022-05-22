package com.yc.apploglib;

public class DelayInitializer<T> {
    private volatile T mT = null;

    private final Creator<T> mCreator;

    /**
     * Constructor.
     *
     * @param creator non null.
     */
    public DelayInitializer(Creator<T> creator) {
        mCreator = creator;
        if (mCreator == null) {
            throw new IllegalArgumentException("creator null!");
        }
    }

    /**
     * Retrieve the object.
     *
     * @return the target object.
     */
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

    /**
     * Creator for create the object.
     *
     * @param <T> traget object class.
     */
    public interface Creator<T> {

        /**
         * Create the target object.
         *
         * @return target object, nonnull.
         */
        T create();
    }
}

