

package com.yc.ipc.internal;

import android.os.Parcel;
import android.os.Parcelable;

import com.yc.ipc.wrapper.MethodWrapper;
import com.yc.ipc.wrapper.ParameterWrapper;

/**
 * Created by yangchong on 16/4/7.
 */
public class CallbackMail implements Parcelable {

    private long mTimeStamp;

    private int mIndex;

    private MethodWrapper mMethod;

    private ParameterWrapper[] mParameters;

    public static final Creator<CallbackMail> CREATOR
            = new Creator<CallbackMail>() {
        public CallbackMail createFromParcel(Parcel in) {
            CallbackMail mail = new CallbackMail();
            mail.readFromParcel(in);
            return mail;
        }
        public CallbackMail[] newArray(int size) {
            return new CallbackMail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(mTimeStamp);
        parcel.writeInt(mIndex);
        parcel.writeParcelable(mMethod, flags);
        parcel.writeParcelableArray(mParameters, flags);
    }

    public void readFromParcel(Parcel in) {
        mTimeStamp = in.readLong();
        mIndex = in.readInt();
        ClassLoader classLoader = CallbackMail.class.getClassLoader();
        mMethod = in.readParcelable(classLoader);
        Parcelable[] parcelables = in.readParcelableArray(classLoader);
        if (parcelables == null) {
            mParameters = null;
        } else {
            int length = parcelables.length;
            mParameters = new ParameterWrapper[length];
            for (int i = 0; i < length; ++i) {
                mParameters[i] = (ParameterWrapper) parcelables[i];
            }
        }

    }

    private CallbackMail() {

    }

    public CallbackMail(long timeStamp, int index, MethodWrapper method, ParameterWrapper[] parameters) {
        mTimeStamp = timeStamp;
        mIndex = index;
        mMethod = method;
        mParameters = parameters;
    }

    public ParameterWrapper[] getParameters() {
        return mParameters;
    }

    public int getIndex() {
        return mIndex;
    }

    public MethodWrapper getMethod() {
        return mMethod;
    }

    public long getTimeStamp() {
        return mTimeStamp;
    }

}
