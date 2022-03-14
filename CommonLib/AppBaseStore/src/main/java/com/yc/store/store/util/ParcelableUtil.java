package com.yc.store.store.util;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ParcelableUtil {

    public static byte[] marshall(@NonNull Parcelable parcelable){
        if(parcelable == null){
            return new byte[0];
        }
        Parcel parcel = Parcel.obtain();
        parcelable.writeToParcel(parcel, 0);
        byte[] bytes = parcel.marshall();
        parcel.recycle();
        return bytes;
    }

    public static <T> T unmarshall(@NonNull byte[] bytes, Parcelable.Creator<T> creator){
        if(bytes == null || creator == null){
            return null;
        }
        return creator.createFromParcel(unmarshall(bytes));
    }

    private static Parcel unmarshall(byte[] bytes){
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes, 0, bytes.length);
        parcel.setDataPosition(0);
        return parcel;
    }
}
