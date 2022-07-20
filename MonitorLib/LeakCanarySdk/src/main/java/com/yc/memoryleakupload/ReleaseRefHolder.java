package com.yc.memoryleakupload;

import com.squareup.leakcanary.RefWatcher;

import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * description:自定义RefWatcher
 */
public class ReleaseRefHolder {

    public static final String TAG = "ReleaseRefHolder";

    private static ReleaseRefHolder instance;

    private final Set<NameWeakReference> referenceSet = new CopyOnWriteArraySet<>();

    private final ReferenceQueue<Object> queue = new ReferenceQueue<>();

    private RefWatcher refWatcher;

    private boolean isWatching;

    private boolean isDebug;

    private ReleaseRefHolder() {
    }

    public static ReleaseRefHolder getInstance(){
        if (null == instance) {
            synchronized (ReleaseRefHolder.class){
                if (null == instance){
                    instance = new ReleaseRefHolder();
                }
            }
        }
        return instance;
    }

    /**
     * 保存引用对象
     * @param object 可以对象
     */
    public void addRefObject(Object object) {
        if (isWatching){
            return;
        }
        NameWeakReference weakReference = new NameWeakReference(object, queue, object.getClass().getName());
        referenceSet.add(weakReference);
    }

    public void setRefWatcher(RefWatcher refWatcher) {
        this.refWatcher = refWatcher;
    }

    /**
     * release模式手动触发查找内存泄漏
     */
    public void findLeakInRelease() {
        isWatching = true;
        NameWeakReference ref;
        while ((ref = (NameWeakReference) queue.poll()) != null) {
            referenceSet.remove(ref);
        }
        List<Object> list = new ArrayList<>();
        for (NameWeakReference weakReference : referenceSet) {
            Object object = weakReference.get();
            if (null != object) {
                LeakCanaryUtils.debug(TAG, "find leak object: " + weakReference.getName());
                list.add(object);
            }
        }
        if (list.isEmpty()){
            return;
        }
        refWatcher.watchRelease(list);
    }

    public void removeReference(String name){
        Iterator<NameWeakReference> referenceIterator = referenceSet.iterator();
        while (referenceIterator.hasNext()){
            NameWeakReference reference = referenceIterator.next();
            if (null == reference.get()){
                break;
            }
            if (reference.getName().equals(name)){
                //referenceSet.remove(reference);
                referenceIterator.remove();
            }
        }
    }

    public boolean hasReference(){
        return !referenceSet.isEmpty();
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean debug) {
        isDebug = debug;
    }

    public void release(){
        isWatching = false;
    }
}
