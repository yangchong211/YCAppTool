package com.ycbjie.android.model.bean

import android.os.Parcel
import android.os.Parcelable


class ProjectListBean() : Parcelable {

    var curPage: Int = 0
    var datas: MutableList<HomeData>? = null
    var offset: Int = 0
    var isOver: Boolean = false
    var pageCount: Int = 0
    var size: Int = 0
    var total: Int = 0

    constructor(parcel: Parcel) : this() {
        curPage = parcel.readInt()
        offset = parcel.readInt()
        isOver = parcel.readByte() != 0.toByte()
        pageCount = parcel.readInt()
        size = parcel.readInt()
        total = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(curPage)
        parcel.writeInt(offset)
        parcel.writeByte(if (isOver) 1 else 0)
        parcel.writeInt(pageCount)
        parcel.writeInt(size)
        parcel.writeInt(total)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProjectListBean> {
        override fun createFromParcel(parcel: Parcel): ProjectListBean {
            return ProjectListBean(parcel)
        }

        override fun newArray(size: Int): Array<ProjectListBean?> {
            return arrayOfNulls(size)
        }
    }
}
