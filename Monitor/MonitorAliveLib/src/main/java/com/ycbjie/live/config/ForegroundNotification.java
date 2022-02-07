package com.ycbjie.live.config;


import androidx.annotation.NonNull;

import java.io.Serializable;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/19
 *     desc  : 默认前台服务通知样式
 *     revise:
 * </pre>
 */
public class ForegroundNotification implements Serializable {
    /**
     * 标题
     */
    private String mTitle;
    /**
     * 描述
     */
    private String mDescription;
    /**
     * 图标资源
     */
    private int mIconRes;
    /**
     * 是否显示
     */
    private boolean mIsShow;
    private ForegroundNotificationClickListener mNotificationClickListener;

    private ForegroundNotification() {

    }

    public ForegroundNotification(String title, String description, int iconRes, ForegroundNotificationClickListener foregroundNotificationClickListener) {
        mTitle = title;
        mDescription = description;
        mIconRes = iconRes;
        mNotificationClickListener = foregroundNotificationClickListener;
    }

    public ForegroundNotification(String title, String description, int iconRes) {
        mTitle = title;
        mDescription = description;
        mIconRes = iconRes;
    }

    /**
     * 初始化
     *
     * @return ForegroundNotification
     */
    public static ForegroundNotification init() {
        return new ForegroundNotification();
    }

    /**
     * 设置标题
     *
     * @param title 标题
     * @return ForegroundNotification
     */
    public ForegroundNotification title(@NonNull String title) {
        mTitle = title;
        return this;
    }

    /**
     * 设置副标题
     *
     * @param description 副标题
     * @return ForegroundNotification
     */
    public ForegroundNotification description(@NonNull String description) {
        mDescription = description;
        return this;
    }

    /**
     * 设置图标
     *
     * @param iconRes 图标
     * @return ForegroundNotification
     */
    public ForegroundNotification icon(@NonNull int iconRes) {
        mIconRes = iconRes;
        return this;
    }

    /**
     * 设置前台通知点击事件
     *
     * @param notificationClickListener 前台通知点击回调
     * @return ForegroundNotification
     */
    public ForegroundNotification setNotificationClickListener(@NonNull ForegroundNotificationClickListener notificationClickListener) {
        mNotificationClickListener = notificationClickListener;
        return this;
    }

    public String getTitle() {
        return mTitle == null ? "" : mTitle;
    }

    public String getDescription() {
        return mDescription == null ? "" : mDescription;
    }

    public int getIconRes() {
        return mIconRes;
    }

    public boolean isShow() {
        return mIsShow;
    }

    public ForegroundNotification setIsShow(boolean show) {
        mIsShow = show;
        return this;
    }

    public ForegroundNotificationClickListener getNotificationClickListener() {
        return mNotificationClickListener;
    }
}
