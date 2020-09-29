package com.ycbjie.android.model.bean

import android.os.Parcel
import android.os.Parcelable


class HomeTag() : Parcelable {

    /**
     * name : 导航
     * url : /navi#281
     */

    var name: String? = null
    var url: String? = null

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        url = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HomeTag> {
        override fun createFromParcel(parcel: Parcel): HomeTag {
            return HomeTag(parcel)
        }

        override fun newArray(size: Int): Array<HomeTag?> {
            return arrayOfNulls(size)
        }
    }
}
