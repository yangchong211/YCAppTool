

package com.yc.ipc.wrapper;

import android.os.Parcel;
import android.os.Parcelable;

import com.yc.ipc.annotation.ClassId;
import com.yc.ipc.util.TypeUtils;

/**
 * Created by yangchong on 16/4/9.
 */
public class TypeWrapper extends BaseWrapper implements Parcelable {

    public static final Parcelable.Creator<TypeWrapper> CREATOR
            = new Parcelable.Creator<TypeWrapper>() {
        public TypeWrapper createFromParcel(Parcel in) {
            TypeWrapper typeWrapper = new TypeWrapper();
            typeWrapper.readFromParcel(in);
            return typeWrapper;
        }
        public TypeWrapper[] newArray(int size) {
            return new TypeWrapper[size];
        }
    };

    private TypeWrapper() {

    }

    public TypeWrapper(Class<?> clazz) {
        setName(!clazz.isAnnotationPresent(ClassId.class), TypeUtils.getClassId(clazz));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel(parcel, flags);
    }

    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);
    }

}
