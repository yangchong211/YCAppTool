package com.yc.ipc.wrapper;

import android.os.Parcel;
import android.os.Parcelable;

import com.yc.ipc.annotation.ClassId;
import com.yc.ipc.util.CodeUtils;
import com.yc.ipc.util.HermesException;
import com.yc.ipc.util.TypeUtils;

public class ParameterWrapper extends BaseWrapper implements Parcelable {

    private String mData;

    //only used here.
    private Class<?> mClass;

    public static final Parcelable.Creator<ParameterWrapper> CREATOR
            = new Parcelable.Creator<ParameterWrapper>() {
        public ParameterWrapper createFromParcel(Parcel in) {
            ParameterWrapper parameterWrapper = new ParameterWrapper();
            parameterWrapper.readFromParcel(in);
            return parameterWrapper;
        }
        public ParameterWrapper[] newArray(int size) {
            return new ParameterWrapper[size];
        }
    };

    private ParameterWrapper() {

    }

    public ParameterWrapper(Class<?> clazz, Object object) throws HermesException {
        mClass = clazz;
        setName(!clazz.isAnnotationPresent(ClassId.class), TypeUtils.getClassId(clazz));
        mData = CodeUtils.encode(object);
    }

    public ParameterWrapper(Object object) throws HermesException{
        if (object == null) {
            setName(false, "");
            mData = null;
            mClass = null;
        } else {
            Class<?> clazz = object.getClass();
            mClass = clazz;
            setName(!clazz.isAnnotationPresent(ClassId.class), TypeUtils.getClassId(clazz));
            mData = CodeUtils.encode(object);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel(parcel, flags);
        parcel.writeString(mData);
    }

    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);
        mData = in.readString();
    }

    public String getData() {
        return mData;
    }

    public boolean isNull() {
        return mData == null;
    }

    public Class<?> getClassType() {
        return mClass;
    }
}
