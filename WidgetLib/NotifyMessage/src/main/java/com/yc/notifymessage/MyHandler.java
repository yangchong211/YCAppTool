package com.yc.notifymessage;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

public class MyHandler extends Handler {

    private final WeakReference<NotificationManager> mWeakReference;

    public MyHandler(NotificationManager notificationManager) {
        mWeakReference = new WeakReference<>(notificationManager);
    }

    @Override
    public void handleMessage(Message msg) {
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
