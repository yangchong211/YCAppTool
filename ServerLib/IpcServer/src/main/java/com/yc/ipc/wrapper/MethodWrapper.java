package com.yc.ipc.wrapper;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Method;

import com.yc.ipc.annotation.MethodId;
import com.yc.ipc.util.TypeUtils;

public class MethodWrapper extends BaseWrapper implements Parcelable {

    private TypeWrapper[] mParameterTypes;

    private TypeWrapper mReturnType;

    public static final Parcelable.Creator<MethodWrapper> CREATOR
            = new Parcelable.Creator<MethodWrapper>() {
        public MethodWrapper createFromParcel(Parcel in) {
            MethodWrapper methodWrapper = new MethodWrapper();
            methodWrapper.readFromParcel(in);
            return methodWrapper;
        }
        public MethodWrapper[] newArray(int size) {
            return new MethodWrapper[size];
        }
    };

    private MethodWrapper() {}


    public MethodWrapper(Method method) {
        setName(!method.isAnnotationPresent(MethodId.class), TypeUtils.getMethodId(method));
        Class<?>[] classes = method.getParameterTypes();
        if (classes == null) {
            classes = new Class<?>[0];
        }
        int length = classes.length;
        mParameterTypes = new TypeWrapper[length];
        for (int i = 0; i < length; ++i) {
            mParameterTypes[i] = new TypeWrapper(classes[i]);
        }
        mReturnType = new TypeWrapper(method.getReturnType());
    }

    public MethodWrapper(String methodName, Class<?>[] parameterTypes) {
        setName(true, methodName);
        if (parameterTypes == null) {
            parameterTypes = new Class<?>[0];
        }
        int length = parameterTypes.length;
        mParameterTypes = new TypeWrapper[length];
        for (int i = 0; i < length; ++i) {
            mParameterTypes[i] = new TypeWrapper(parameterTypes[i]);
        }
        mReturnType = null;
    }

    public MethodWrapper(Class<?>[] parameterTypes) {
        setName(false, "");
        if (parameterTypes == null) {
            parameterTypes = new Class<?>[0];
        }
        int length = parameterTypes.length;
        mParameterTypes = new TypeWrapper[length];
        for (int i = 0; i < length; ++i) {
            mParameterTypes[i] = parameterTypes[i] == null ? null : new TypeWrapper(parameterTypes[i]);
        }
        mReturnType = null;
    }

    public TypeWrapper[] getParameterTypes() {
        return mParameterTypes;
    }

    public TypeWrapper getReturnType() {
        return mReturnType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel(parcel, flags);
        parcel.writeParcelableArray(mParameterTypes, flags);
        parcel.writeParcelable(mReturnType, flags);
    }

    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);
        ClassLoader classLoader = MethodWrapper.class.getClassLoader();
        Parcelable[] parcelables = in.readParcelableArray(classLoader);
        if (parcelables == null) {
            mParameterTypes = null;
        } else {
            int length = parcelables.length;
            mParameterTypes = new TypeWrapper[length];
            for (int i = 0; i < length; ++i) {
                mParameterTypes[i] = (TypeWrapper) parcelables[i];
            }
        }
        mReturnType = in.readParcelable(classLoader);
    }

}
