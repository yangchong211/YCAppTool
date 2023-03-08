package com.yc.notifymessage;

import android.animation.Animator;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import androidx.annotation.Nullable;


public class NotificationServiceImpl implements INotificationService<CustomNotification> {

    private NotifyContainerView mNotificationContainerView;
    @Nullable
    private WindowManager mWindowManager;
    @Nullable
    private WindowManager.LayoutParams mLayoutParams;
    private final NotificationManager mNotificationManager;
    /**
     * 用于标志改 Notification 是否展示
     */
    public boolean mIsShowing;

    public NotificationServiceImpl(NotificationManager notificationManager){
        mNotificationManager = notificationManager;
    }

    @Override
    public void show(CustomNotification notification) {
        try {
            if (notification == null || notification.getActivity() == null) {
                NotifyLoggerUtils.log("handleShow returned: mNotification == null || mNotification.getActivity() == null");
                return;
            }
            initNotificationView(notification);
            if (mNotificationContainerView == null
                    || mNotificationContainerView.getParent() != null
                    || mWindowManager == null
                    || mLayoutParams == null) {
                String reason = "unknown";
                if (mNotificationContainerView == null) {
                    reason = "mNotificationContainerView == null";
                } else if (mNotificationContainerView.getParent() != null) {
                    reason = "mNotificationContainerView.getParent() != null";
                } else if (mWindowManager == null) {
                    reason = "mWindowManager == null";
                } else if (mLayoutParams == null) {
                    reason = "mLayoutParams == null";
                }
                NotifyLoggerUtils.log("handleShow returned: " + reason);
                return;
            }
            //判断activity是否是非存活状态
            if (NotificationUtils.isActivityNotAlive(mNotificationContainerView.getActivity())) {
                NotifyLoggerUtils.log("handleShow returned: activity is finishing or destroyed!");
                return;
            }
            NotifyLoggerUtils.log("handleShow before addView: mLayoutParams.token" + mNotificationContainerView.getWindowToken());
            mLayoutParams.token = mNotificationContainerView.getWindowToken();
            mWindowManager.addView(mNotificationContainerView, mLayoutParams);
            NotifyLoggerUtils.log("handleShow after addView");
            changeIsShowing(true);
            mNotificationContainerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                           int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if (mNotificationContainerView == null) {
                        NotifyLoggerUtils.log("handleShow animation: mNotificationContainerView == null");
                        return;
                    } else if (NotificationUtils.isActivityNotAlive(mNotificationContainerView.getActivity())) {
                        NotifyLoggerUtils.log("handleShow animation: mNotificationContainerView.getActivity() is not alive : "
                                + mNotificationContainerView.getActivity());
                        return;
                    } else if (notification == null) {
                        NotifyLoggerUtils.log("handleShow animation: mNotification == null");
                        return;
                    }
                    resetAnimation(mNotificationContainerView);
                    mNotificationContainerView.animate().translationY(0).setDuration(NotificationNode.ANIM_DURATION).start();
                    mNotificationManager.startTimeout(notification.mType, notification.getTimeout());
                    mNotificationContainerView.removeOnLayoutChangeListener(this);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void cancel(CustomNotification notification , Animator.AnimatorListener listener) {
        if (mNotificationContainerView == null) {
            return;
        }
        resetAnimation(mNotificationContainerView);
        mNotificationContainerView.animate().translationY(-mNotificationContainerView.getHeight()).setDuration(NotificationNode.ANIM_DURATION);
        mNotificationContainerView.animate().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (listener != null) {
                    listener.onAnimationStart(animation);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mWindowManager != null && mNotificationContainerView != null
                        && mNotificationContainerView.getParent() != null) {
                    mWindowManager.removeViewImmediate(mNotificationContainerView);
                }
                if (listener != null) {
                    listener.onAnimationEnd(animation);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if (listener != null) {
                    listener.onAnimationCancel(animation);
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                if (listener != null) {
                    listener.onAnimationRepeat(animation);
                }
            }
        });
        mNotificationContainerView.animate().start();
        changeIsShowing(false);
    }

    @Override
    public boolean isShowing() {
        return mIsShowing;
    }

    @Override
    public void changeIsShowing(boolean isShowing) {
        mIsShowing = isShowing;
    }

    private void initNotificationView(CustomNotification notification) {
        Context context = notification.getActivity();
        if (notification.getNotificationView().getView() == null
                || notification.getNotificationView().getView().getParent() != null) {
            return;
        }
        if (context == null){
            return;
        }
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mLayoutParams.format = PixelFormat.TRANSLUCENT;
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mLayoutParams.gravity = Gravity.TOP;
        mLayoutParams.x = 0;
        mLayoutParams.y = NotificationUtils.getNotificationLocationY(context);

        mNotificationContainerView = new NotifyContainerView(context);
        ViewGroup.LayoutParams vl = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mNotificationContainerView.setLayoutParams(vl);
        mNotificationContainerView.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                //隐藏通知栏
                mNotificationManager.hideNotification();
            }
        });
        View view = notification.getNotificationView().getView();
        mNotificationContainerView.addView(view);
        mNotificationContainerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                mNotificationContainerView.setTranslationY(-mNotificationContainerView.getHeight());
                mNotificationContainerView.removeOnLayoutChangeListener(this);
            }
        });
        mNotificationContainerView.setCollapsible(notification.mIsCollapsible);
    }

    /**
     * 取消当前的动画
     */
    private void resetAnimation(View view) {
        if (view == null) {
            return;
        }
        view.animate().cancel();
        view.animate().setListener(null);
    }
}
