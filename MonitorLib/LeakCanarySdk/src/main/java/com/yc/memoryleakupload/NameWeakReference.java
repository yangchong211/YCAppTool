package com.yc.memoryleakupload;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * description:自定义软引用
 */
public class NameWeakReference extends WeakReference<Object> {

    private String name;

    public NameWeakReference(Object referent, ReferenceQueue<? super Object> q, String name) {
        super(referent, q);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
