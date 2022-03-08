package com.yc.ipc.wrapper;

import android.os.Parcel;
import android.os.Parcelable;

import com.yc.ipc.annotation.ClassId;
import com.yc.ipc.util.TimeStampGenerator;
import com.yc.ipc.util.TypeUtils;

public class ObjectWrapper extends BaseWrapper implements Parcelable {

    public static final int TYPE_OBJECT_TO_NEW = 0;

    public static final int TYPE_OBJECT_TO_GET = 1;

    public static final int TYPE_OBJECT = 3;

    public static final int TYPE_CLASS = 4;

    public static final int TYPE_CLASS_TO_GET = 5;

    private long mTimeStamp;

    //only used here
    private Class<?> mClass;

    private int mType;

    public static final Parcelable.Creator<ObjectWrapper> CREATOR
            = new Parcelable.Creator<ObjectWrapper>() {
        public ObjectWrapper createFromParcel(Parcel in) {
            ObjectWrapper objectWrapper = new ObjectWrapper();
            objectWrapper.readFromParcel(in);
            return objectWrapper;
        }
        public ObjectWrapper[] newArray(int size) {
            return new ObjectWrapper[size];
        }
    };

    private ObjectWrapper() {}

    public ObjectWrapper(Class<?> clazz, int type) {
        setName(!clazz.isAnnotationPresent(ClassId.class), TypeUtils.getClassId(clazz));
        mClass = clazz;
        mTimeStamp = TimeStampGenerator.getTimeStamp();
        mType = type;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel(parcel, flags);
        parcel.writeLong(mTimeStamp);
        parcel.writeInt(mType);
    }

    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);
        mTimeStamp = in.readLong();
        mType = in.readInt();
    }

    public long getTimeStamp() {
        return mTimeStamp;
    }

    public Class<?> getObjectClass() {
        return mClass;
    }

    public void setType(int type) {
        mType = type;
    }

    public int getType() {
        return mType;
    }
}
