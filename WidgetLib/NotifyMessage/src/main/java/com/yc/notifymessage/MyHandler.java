package com.yc.notifymessage;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/03/22
 *     desc  : Handler
 *     revise: 弱引用–>随时可能会被垃圾回收器回收，不一定要等到虚拟机内存不足时才强制回收。
 * </pre>
 */
public class MyHandler extends Handler {

    /**
     * 采用弱引用管理NotificationManager
     * 弱引用（WeakReference）：如果一个对象只被弱引用指向，当JVM进行垃圾回收时，无论内存是否充足，都会回收该对象。
     */
    private final WeakReference<NotificationManager> mWeakReference;

    public MyHandler(NotificationManager notificationManager) {
        mWeakReference = new WeakReference<>(notificationManager);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        if (mWeakReference == null || mWeakReference.get() == null) {
            return;
        }
        int action = msg.what;
        if (action == NotificationManager.MSG_SHOW) {
            CustomNotification notification = msg.getData()
                    .getParcelable(NotificationManager.BUNDLE_NOTIFICATION);
            if (notification != null) {
                mWeakReference.get().showNotification(notification);
            }
        } else {
            int type = action - NotificationManager.MSG_HIDE;
            mWeakReference.get().hideNotification(type);
        }
    }

}
