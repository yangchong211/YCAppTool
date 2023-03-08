package com.yc.notifymessage;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 用于展示顶部样式
 *     revise:
 * </pre>
 */
public class CustomNotification<T> implements Parcelable {

    public static final Creator<CustomNotification> CREATOR = new Creator<CustomNotification>() {
        @Override
        public CustomNotification createFromParcel(Parcel in) {
            return new CustomNotification(in);
        }

        @Override
        public CustomNotification[] newArray(int size) {
            return new CustomNotification[size];
        }
    };

    private static final int TYPE_UNKNOWN = -1;

    /**
     * 显示的通知 View
     */
    NotificationView<T> mView;
    /**
     * 自动隐藏时间
     */
    int mTimeout = 0;
    /**
     * 显示优先级
     */
    int mPriority = 0;
    /**
     * 是否常驻
     */
    boolean mIsPin;
    /**
     * 是否可被上划收起
     */
    boolean mIsCollapsible;
    /**
     * 是否可被覆盖（暂时无用）
     */
    boolean mIsOverride = true;
    /**
     * 通知类型（必须设置）
     */
    int mType = TYPE_UNKNOWN;
    /**
     * 与 view 绑定的 data 类
     */
    T mData;
    /**
     * 消失监听listener
     */
    OnDismissListener mListener;

    public CustomNotification() {

    }

    protected CustomNotification(Parcel in) {
        mTimeout = in.readInt();
        mPriority = in.readInt();
        mIsPin = in.readByte() != 0;
        mIsCollapsible = in.readByte() != 0;
        mIsOverride = in.readByte() != 0;
        mType = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mTimeout);
        dest.writeInt(mPriority);
        dest.writeByte((byte) (mIsPin ? 1 : 0));
        dest.writeByte((byte) (mIsCollapsible ? 1 : 0));
        dest.writeByte((byte) (mIsOverride ? 1 : 0));
        dest.writeInt(mType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public CustomNotification<T> setTimeOut(int timeOut) {
        this.mTimeout = timeOut;
        return this;
    }

    public CustomNotification<T> setPin(boolean isPin) {
        this.mIsPin = isPin;
        return this;
    }

    public CustomNotification<T> setPriority(int priority) {
        this.mPriority = priority;
        return this;
    }

    public CustomNotification<T> setType(int type) {
        this.mType = type;
        return this;
    }

    public CustomNotification<T> setCollapsible(boolean bool) {
        this.mIsCollapsible = bool;
        return this;
    }

    public CustomNotification<T> setOverride(boolean bool) {
        this.mIsOverride = bool;
        return this;
    }

    public CustomNotification<T> setNotificationView(NotificationView<T> view) {
        this.mView = view;
        return this;
    }

    public CustomNotification<T> setData(T data) {
        mData = data;
        return this;
    }

    public CustomNotification<T> setData(T data, boolean rebind) {
        mData = data;
        if (rebind) {
            getNotificationView().bindNotification(this);
        }
        return this;
    }

    CustomNotification<T> setDismissListener(OnDismissListener listener) {
        mListener = listener;
        return this;
    }

    public NotificationView<T> getNotificationView() {
        return mView;
    }

    public int getTimeout() {
        return mTimeout;
    }

    public int getPriority() {
        return mPriority;
    }

    public boolean isPin() {
        return mIsPin;
    }

    public boolean isCollapsible() {
        return mIsCollapsible;
    }

    public boolean isOverride() {
        return mIsOverride;
    }

    @Nullable
    public Activity getActivity() {
        return mView == null ? null : mView.getActivity();
    }


    @Nullable
    public T getData() {
        return mData;
    }

    public OnDismissListener getListener() {
        return mListener;
    }

    /**
     * 显示当前设置的通知，注意：通知类型必须设置
     */
    public void show() {
        checkArgument();
        if (mView != null) {
            mView.bindNotification(this);
        }
        NotificationManager.getInstance().notify(this);
    }

    /**
     * 检查参数是否正确，主要检查通知类型是否设置
     */
    private void checkArgument() {
        if (mType == TYPE_UNKNOWN) {
            throw new IllegalArgumentException("type should be set");
        }
    }

    public static void cancel(int type) {
        NotificationManager.getInstance().cancel(type);
    }

}
