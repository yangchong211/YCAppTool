package com.yc.logclient.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Throwable封装类,可序列化，用于跨进程传递
 */
public class ThrowableBean implements Parcelable {

    //包名
    public String pkg;
    //类名
    public String throwableClsName;
    //msg
    public String msg;
    //调用堆栈
    public StackTraceElement[] stackTraceElements;

    public ThrowableBean(String pkg, String throwableClsName, String msg, StackTraceElement[] elements) {
        this.pkg = pkg;
        this.throwableClsName = throwableClsName;
        this.msg = msg;
        this.stackTraceElements = elements;
    }


    protected ThrowableBean(Parcel in) {
        pkg = in.readString();
        throwableClsName = in.readString();
        msg = in.readString();
        int n = in.readInt();
        if (n > 0) {
            stackTraceElements = new StackTraceElement[n];
            for (int i = 0; i < n; i++) {
                stackTraceElements[i] = (StackTraceElement) in.readSerializable();
            }
        }
    }

    public static final Creator<ThrowableBean> CREATOR = new Creator<ThrowableBean>() {
        @Override
        public ThrowableBean createFromParcel(Parcel in) {
            return new ThrowableBean(in);
        }

        @Override
        public ThrowableBean[] newArray(int size) {
            return new ThrowableBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pkg);
        dest.writeString(throwableClsName);
        dest.writeString(msg);
        int len = stackTraceElements == null ? 0 : stackTraceElements.length;
        dest.writeInt(len);
        for (int i = 0; i < len; i++) {
            dest.writeSerializable(stackTraceElements[i]);
        }
    }
}
