package com.yc.notificationlib;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

public final class NotifyAppUtils {

    /**
     * 判断是否开启悬浮通知
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean isFloatImportance(NotificationChannel channel){
        if (channel.getImportance() == NotificationManager.IMPORTANCE_HIGH) {
            return true;
        }
        return false;
    }


    /**
     * 判断通知是否是静默不重要的通知。
     * 主要是该类通知被用户手动给关闭
     *
     * @param channelId 通知栏channelId
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean isNoImportance(NotificationManager manager,String channelId) {
        NotificationChannel channel = getNotificationChannel(manager,channelId);
        if (channel !=null){
            return isNoImportance(channel);
        }
        return false;
    }

    /**
     * 判断通知是否是静默不重要的通知。
     * 主要是该类通知被用户手动给关闭
     *
     * @param channel 通知栏channel
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean isNoImportance(NotificationChannel channel) {
        if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
            return true;
        }
        return false;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void openChannelSetting(NotificationManager manager,String channelId , Context context) {
        NotificationChannel channel = getNotificationChannel(manager,channelId);
        openChannelSetting(channel,context);
    }

    /**
     * 获取创建一个NotificationChannel的对象
     *
     * @return NotificationChannel对象
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationChannel getNotificationChannel(NotificationManager manager,String channelId) {
        if (!TextUtils.isEmpty(channelId)) {
            return manager.getNotificationChannel(channelId);
        }
        return null;
    }

    /**
     * 跳转到相关设置页面
     * @param channel
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void openChannelSetting(NotificationChannel channel, Context context) {
        if (channel == null) {
            return;
        }
        if (channel.getImportance() != NotificationManager.IMPORTANCE_NONE) {
            Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.getId());
            context.startActivity(intent);
        }
    }


}
