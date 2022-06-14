package com.yc.notificationlib;

import android.app.PendingIntent;
import android.net.Uri;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import static android.app.Notification.PRIORITY_DEFAULT;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://www.jianshu.com/p/514eb6193a06
 *     time  : 2018/2/10
 *     desc  : 通知栏参数设置
 *     revise:
 * </pre>
 */
public final class NotificationParams {

    protected boolean ongoing;
    protected RemoteViews remoteViews;
    protected PendingIntent intent;
    protected String ticker;
    protected int priority;
    protected boolean onlyAlertOnce = false;
    protected long when;
    protected Uri sound;
    protected int defaults = 0;
    protected long[] pattern = null;
    protected int[] flags;
    protected boolean isFullScreen;
    @NotificationCompat.NotificationVisibility
    protected int visibility;

    public NotificationParams(){
        this.ongoing = false;
        this.remoteViews = null;
        this.intent = null;
        this.ticker = "";
        this.priority = PRIORITY_DEFAULT;
        this.when = 0;
        this.sound = null;
        this.isFullScreen = false;
        this.visibility = NotificationCompat.VISIBILITY_PRIVATE;
    }

    /**
     * 让通知左右滑的时候是否可以取消通知
     * @param ongoing                   是否可以取消通知
     * @return
     */
    public NotificationParams setOngoing(boolean ongoing){
        this.ongoing = ongoing;
        return this;
    }

    /**
     * 设置自定义view通知栏布局
     * @param remoteViews               view
     * @return
     */
    public NotificationParams setContent(RemoteViews remoteViews){
        this.remoteViews = remoteViews;
        return this;
    }

    /**
     * 设置内容点击
     * @param intent                    intent
     * @return
     */
    public NotificationParams setContentIntent(PendingIntent intent){
        this.intent = intent;
        return this;
    }

    /**
     * 设置状态栏的标题
     * @param ticker                    状态栏的标题
     * @return
     */
    public NotificationParams setTicker(String ticker){
        this.ticker = ticker;
        return this;
    }


    /**
     * 设置优先级
     * 注意：
     * Android 8.0以及上，在 NotificationChannel 的构造函数中指定，总共要五个级别；
     * Android 7.1（API 25）及以下的设备，还得调用NotificationCompat 的 setPriority方法来设置
     *
     * @param priority                  优先级，默认是Notification.PRIORITY_DEFAULT
     * @return
     */
    public NotificationParams setPriority(int priority){
        this.priority = priority;
        return this;
    }

    /**
     * 是否提示一次.true - 如果Notification已经存在状态栏即使在调用notify函数也不会更新
     * @param onlyAlertOnce             是否只提示一次，默认是false
     * @return
     */
    public NotificationParams setOnlyAlertOnce(boolean onlyAlertOnce){
        this.onlyAlertOnce = onlyAlertOnce;
        return this;
    }

    /**
     * 设置通知时间，默认为系统发出通知的时间，通常不用设置
     * @param when                      when
     * @return
     */
    public NotificationParams setWhen(long when){
        this.when = when;
        return this;
    }

    /**
     * 设置sound
     * @param sound                     sound
     * @return
     */
    public NotificationParams setSound(Uri sound){
        this.sound = sound;
        return this;
    }


    /**
     * 设置默认的提示音
     * @param defaults                  defaults
     * @return
     */
    public NotificationParams setDefaults(int defaults){
        this.defaults = defaults;
        return this;
    }

    /**
     * 自定义震动效果
     * @param pattern                  pattern
     * @return
     */
    public NotificationParams setVibrate(long[] pattern){
        this.pattern = pattern;
        return this;
    }

    /**
     * 设置flag标签
     * @param flags                     flags
     * @return
     */
    public NotificationParams setFlags(int... flags){
        this.flags = flags;
        return this;
    }

    /**
     * 设置设置悬浮式通知栏
     * @param isFullScreen                   是否是悬挂式
     * @return
     */
    public NotificationParams setFullScreen(boolean isFullScreen){
        this.isFullScreen = isFullScreen;
        return this;
    }

    /**
     * 控制锁定屏幕中通知的可见详情级别，请调用 setVisibility() 并指定以下值之一：
     * VISIBILITY_PUBLIC 显示通知的完整内容。
     * VISIBILITY_SECRET 不在锁定屏幕上显示该通知的任何部分。
     * VISIBILITY_PRIVATE 显示基本信息，例如通知图标和内容标题，但隐藏通知的完整内容。
     * 对于通知在锁定屏幕上是否可见，用户始终拥有最终控制权，甚至可以根据应用的通知渠道来控制公开范围。
     * @param visibility                    可见参数
     * @return
     */
    public NotificationParams setVisibility(
            @NotificationCompat.NotificationVisibility int visibility){
        this.visibility = visibility;
        return this;
    }
}
