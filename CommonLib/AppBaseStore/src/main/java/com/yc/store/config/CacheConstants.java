package com.yc.store.config;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class CacheConstants {

    @Retention(RetentionPolicy.SOURCE)
    public @interface CacheType {
        int TYPE_DISK = 1;
        int TYPE_LRU = 2;
        int TYPE_MEMORY = 3;
        int TYPE_MMKV = 4;
        int TYPE_SP = 5;
        int TYPE_STORE = 6;
        int TYPE_FAST = 7;
    }

}
