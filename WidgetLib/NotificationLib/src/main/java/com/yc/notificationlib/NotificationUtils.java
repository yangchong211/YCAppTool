package com.yc.notificationlib;


import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.List;

import static android.app.Notification.PRIORITY_DEFAULT;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://www.jianshu.com/p/514eb6193a06
 *     time  : 2018/2/10
 *     desc  : 通知栏工具类
 *     revise:
 * </pre>
 */
public class NotificationUtils extends ContextWrapper {

    private static String CHANNEL_ID = "default";
    private static String CHANNEL_NAME = "Default_Channel";
    private NotificationManager mManager;
    private NotificationChannel channel;

    public NotificationUtils(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //android 8.0以上需要特殊处理，也就是targetSDKVersion为26以上
            createNotificationChannel(CHANNEL_ID, CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT);
        }
    }

    public NotificationUtils(Context base, String channelId) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //android 8.0以上需要特殊处理，也就是targetSDKVersion为26以上
            createNotificationChannel(channelId, CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT);
        }
    }

    public NotificationUtils(Context base, String channelId, String channelName) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //android 8.0以上需要特殊处理，也就是targetSDKVersion为26以上
            createNotificationChannel(channelId, channelName,NotificationManager.IMPORTANCE_DEFAULT);
        }
    }

    /**
     * 8.0以上需要创建通知栏渠道channel
     */
    @TargetApi(Build.VERSION_CODES.O)
    public NotificationChannel createNotificationChannel(String channelId, String channelName , int importance) {
        //第一个参数：channel_id
        //第二个参数：channel_name，这个是用来展示给用户看的
        //第三个参数：设置通知重要性级别
        //注意：该级别必须要在 NotificationChannel 的构造函数中指定，总共要五个级别；
        //范围是从 NotificationManager.IMPORTANCE_NONE(0) ~ NotificationManager.IMPORTANCE_HIGH(4)
        if (!TextUtils.isEmpty(channelId)) {
            CHANNEL_ID = channelId;
        }
        if (!TextUtils.isEmpty(channelName)) {
            CHANNEL_NAME = channelName;
        }
        channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
        /*channel.canBypassDnd();//是否绕过请勿打扰模式
        channel.enableLights(true);//闪光灯
        channel.setLockscreenVisibility(VISIBILITY_SECRET);//锁屏显示通知
        channel.setLightColor(Color.RED);//闪关灯的灯光颜色
        channel.canShowBadge();//桌面launcher的消息角标
        channel.enableVibration(true);//是否允许震动
        channel.getAudioAttributes();//获取系统通知响铃声音的配置
        channel.getGroup();//获取通知取到组
        channel.setBypassDnd(true);//设置可绕过 请勿打扰模式
        channel.setVibrationPattern(new long[]{100, 100, 200});//设置震动模式
        channel.shouldShowLights();//是否会有灯光*/
        getManager().createNotificationChannel(channel);
        return channel;
    }

    /**
     * 获取创建一个NotificationManager的对象
     *
     * @return NotificationManager对象
     */
    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    /**
     * 获取创建一个NotificationChannel的对象
     *
     * @return NotificationChannel对象
     */
    private NotificationChannel getNotificationChannel() {
        if (channel == null) {
            channel = createNotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
        }
        return channel;
    }

    /**
     * 获取创建一个NotificationChannel的对象
     *
     * @return NotificationChannel对象
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationChannel getNotificationChannel(String channelId) {
        if (TextUtils.isEmpty(channelId)) {
            return getNotificationChannel();
        }
        NotificationManager manager = getManager();
        return manager.getNotificationChannel(channelId);
    }

    /**
     * 清空所有的通知
     */
    public void clearNotification() {
        getManager().cancelAll();
    }

    /**
     * 清空特定的通知
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void clearNotificationChannel(String channelId) {
        if (channelId == null || channelId.length() == 0) {
            return;
        }
        NotificationManager manager = getManager();
        manager.deleteNotificationChannel(channelId);
    }

    /**
     * 清空所有的channel
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void clearAllNotification() {
        NotificationManager manager = getManager();
        List<NotificationChannel> notificationChannels = manager.getNotificationChannels();
        if (notificationChannels != null) {
            for (int i = 0; i < notificationChannels.size(); i++) {
                NotificationChannel notificationChannel = notificationChannels.get(i);
                if (notificationChannel == null) {
                    continue;
                }
                String id = notificationChannel.getId();
                CharSequence name = notificationChannel.getName();
                Log.d("notification channel ", id + " , " + name);
                manager.deleteNotificationChannel(id);
            }
        }
    }

    /**
     * 清空所有channel的通知
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void clearAllGroupNotification() {
        NotificationManager manager = getManager();
        List<NotificationChannelGroup> notificationChannelGroups = manager.getNotificationChannelGroups();
        if (notificationChannelGroups != null) {
            for (int i = 0; i < notificationChannelGroups.size(); i++) {
                NotificationChannelGroup notificationChannelGroup = notificationChannelGroups.get(i);
                if (notificationChannelGroup == null) {
                    continue;
                }
                String id = notificationChannelGroup.getId();
                CharSequence name = notificationChannelGroup.getName();
                Log.d("notification group ", id + " , " + name);
                manager.deleteNotificationChannel(id);
            }
        }
    }

    /**
     * 获取Notification
     *
     * @param title   title
     * @param content content
     */
    public Notification getNotification(String title, String content, int icon) {
        NotificationCompat.Builder builder = getNotificationCompat(title, content, icon);
        Notification build = builder.build();
        NotificationParams notificationParams = getNotificationParams();
        if (notificationParams.flags != null && notificationParams.flags.length > 0) {
            for (int a = 0; a < notificationParams.flags.length; a++) {
                build.flags |= notificationParams.flags[a];
            }
        }
        return build;
    }

    /**
     * 建议使用这个发送通知
     * 调用该方法可以发送通知
     *
     * @param notifyId notifyId
     * @param title    title
     * @param content  content
     */
    public void sendNotification(int notifyId, String title, String content, int icon) {
        NotificationCompat.Builder builder = getNotificationCompat(title, content, icon);
        Notification build = builder.build();
        NotificationParams notificationParams = getNotificationParams();
        if (notificationParams.flags != null && notificationParams.flags.length > 0) {
            for (int a = 0; a < notificationParams.flags.length; a++) {
                build.flags |= notificationParams.flags[a];
            }
        }
        getManager().notify(notifyId, build);
    }

    private NotificationCompat.Builder getNotificationCompat(String title, String content, int icon) {
        NotificationCompat.Builder builder;
        NotificationParams notificationParams = getNotificationParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        } else {
            //注意用下面这个方法，在8.0以上无法出现通知栏。8.0之前是正常的。这里需要增强判断逻辑
            builder = new NotificationCompat.Builder(getApplicationContext());
        }
        //这三个参数比传
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSmallIcon(icon);
        //设置优先级
        builder.setPriority(notificationParams.priority);
        //是否提示一次.true - 如果Notification已经存在状态栏即使在调用notify函数也不会更新
        builder.setOnlyAlertOnce(notificationParams.onlyAlertOnce);
        //让通知左右滑的时候是否可以取消通知
        builder.setOngoing(notificationParams.ongoing);
        //设置自定义布局
        if (notificationParams.remoteViews != null) {
            builder.setContent(notificationParams.remoteViews);
        }
        if (notificationParams.intent != null) {
            builder.setContentIntent(notificationParams.intent);
        }
        if (notificationParams.ticker != null && notificationParams.ticker.length() > 0) {
            builder.setTicker(notificationParams.ticker);
        }
        if (notificationParams.when != 0) {
            builder.setWhen(notificationParams.when);
        }
        if (notificationParams.sound != null) {
            builder.setSound(notificationParams.sound);
        }
        if (notificationParams.defaults != 0) {
            builder.setDefaults(notificationParams.defaults);
        }
        if (notificationParams.isFullScreen) {
            //悬挂式
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_CALL);
                //悬挂式关键点
                //使用全屏意图仅为最高优先级的警报，如果你的应用瞄准Android 10 或更高级别，你需要请求USE_FULL_SCREEN_INTENT权限
                builder.setFullScreenIntent(notificationParams.intent, true);
            }
        }
        //设置锁定屏幕公开范围
        builder.setVisibility(notificationParams.visibility);
        //点击自动删除通知
        builder.setAutoCancel(true);
        return builder;
    }

    private NotificationParams params;

    public NotificationParams getNotificationParams() {
        if (params == null) {
            return new NotificationParams();
        }
        return params;
    }

    public NotificationUtils setNotificationParams(NotificationParams params) {
        this.params = params;
        return this;
    }

}
