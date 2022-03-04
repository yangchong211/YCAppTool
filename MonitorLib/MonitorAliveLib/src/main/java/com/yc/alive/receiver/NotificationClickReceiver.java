package com.yc.alive.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yc.alive.alive.YcKeepAlive;
import com.yc.alive.constant.YcConstant;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/19
 *     desc  : 前台通知点击
 *     revise:
 * </pre>
 */
public final class NotificationClickReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (YcConstant.ACTION_CLICK_NOTIFICATION.equals(intent.getAction())) {
            if (YcKeepAlive.sForegroundNotification != null) {
                if (YcKeepAlive.sForegroundNotification.getNotificationClickListener() != null) {
                    YcKeepAlive.sForegroundNotification.getNotificationClickListener().onNotificationClick(context, intent);
                }
            }
        }
    }
}
