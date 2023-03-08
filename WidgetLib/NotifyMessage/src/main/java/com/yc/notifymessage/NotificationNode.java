package com.yc.notifymessage;

import android.animation.Animator;

import androidx.annotation.NonNull;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 通知栏 通知链表节点
 *     revise:
 * </pre>
 */
public class NotificationNode {

    static final int EQUALS = 0;
    static final int ERROR = -1;
    static final int GREATER = 1;
    static final int SMALLER = 2;
    public static final int ANIM_DURATION = 200;

    protected CustomNotification mNotification;
    private final INotificationService<CustomNotification> notificationServiceImpl;

    public NotificationNode(CustomNotification notification,
                            NotificationManager notificationManager) {
        notificationServiceImpl = new NotificationServiceImpl(notificationManager);
        mNotification = notification;
    }

    int getPriority() {
        return mNotification == null ? -1 : mNotification.mPriority;
    }

    boolean isShowing() {
        return notificationServiceImpl.isShowing();
    }

    void changeIsShowing(boolean isShowing){
        notificationServiceImpl.changeIsShowing(isShowing);
    }

    CustomNotification getNotification() {
        return mNotification;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof NotificationNode)) {
            return false;
        }
        CustomNotification notification = ((NotificationNode) obj).mNotification;
        return notification != null && getNotification() != null
                && notification.mType == getNotification().mType || super.equals(obj);
    }

    @Override
    public int hashCode() {
        return mNotification == null ? -1 : mNotification.mType;
    }

    int compareTo(@NonNull NotificationNode o) {
        if (getNotification() == null || o.getNotification() == null) {
            return ERROR;
        }
        int result = getNotification().mPriority - o.getNotification().mPriority;
        return result > 0 ? GREATER : result < 0 ? SMALLER : EQUALS;
    }

    protected void handleShow() {
        notificationServiceImpl.show(mNotification);
    }

    protected void handleHide(final Animator.AnimatorListener listener) {
        notificationServiceImpl.cancel(mNotification,listener);
    }

}
