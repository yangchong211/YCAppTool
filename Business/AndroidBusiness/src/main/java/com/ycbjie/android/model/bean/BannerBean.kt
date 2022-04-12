package com.ycbjie.android.model.bean

import android.os.Parcel
import android.os.Parcelable

/**
 *
 * 首页Banner
 */

open class BannerBean : Parcelable {

    /**
     * desc : 一起来做个App吧
     * id : 10
     * imagePath : http://www.wanandroid.com/blogimgs/50c115c2-cf6c-4802-aa7b-a4334de444cd.png
     * isVisible : 1
     * order : 0
     * title : 一起来做个App吧
     * type : 0
     * url : http://www.wanandroid.com/blog/show/2
     */


    /**
     * 在Kotlin中创建一个属性，
     * 如果是val，那么会自动生成getter方法，
     * 如果是var，那么会自动生成getter和setter方法。
     */

    private var desc: String? = null
    var id: Int = 0
    var imagePath: String? = null
    private var isVisible: Int = 0
    private var order: Int = 0
    var title: String? = null
    var type: Int = 0
    var url: String? = null

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.desc)
        dest.writeInt(this.id)
        dest.writeString(this.imagePath)
        dest.writeInt(this.isVisible)
        dest.writeInt(this.order)
        dest.writeString(this.title)
        dest.writeInt(this.type)
        dest.writeString(this.url)
    }

    protected constructor(`in`: Parcel) {
        this.desc = `in`.readString()
        this.id = `in`.readInt()
        this.imagePath = `in`.readString()
        this.isVisible = `in`.readInt()
        this.order = `in`.readInt()
        this.title = `in`.readString()
        this.type = `in`.readInt()
        this.url = `in`.readString()
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<BannerBean> = object : Parcelable.Creator<BannerBean> {
            override fun createFromParcel(source: Parcel): BannerBean {
                return BannerBean(source)
            }

            override fun newArray(size: Int): Array<BannerBean?> {
                return arrayOfNulls(size)
            }
        }
    }

}
