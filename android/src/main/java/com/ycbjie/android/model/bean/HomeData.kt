package com.ycbjie.android.model.bean

import android.os.Parcel
import android.os.Parcelable


class HomeData() : Parcelable {

    /**
     * apkLink :
     * author : lanzry
     * chapterId : 73
     * chapterName : 面试相关
     * collect : false
     * courseId : 13
     * desc :
     * envelopePic :
     * fresh : false
     * id : 2674
     * link : http://www.jianshu.com/p/5a272afb4c2e
     * niceDate : 2018-03-18
     * origin :
     * projectLink :
     * publishTime : 1521383343000
     * superChapterId : 195
     * superChapterName : 热门专题
     * tags : []
     * title : 一年经验-有赞面经
     * type : 0
     * visible : 1
     * zan : 0
     */

    private var apkLink: String? = null
    var author: String? = null
    var chapterId: Int = 0
    var chapterName: String? = null
    var collect: Boolean = false
    var courseId: Int = 0
    var desc: String? = null
    var envelopePic: String? = null
    var isFresh: Boolean = false
    var id: Int = 0
    var link: String? = ""
    var niceDate: String? = null
    var origin: String? = null
    var projectLink: String? = null
    var publishTime: Long = 0
    var superChapterId: Int = 0
    var superChapterName: String? = null
    var title: String? = ""
    var type: Int = 0
    var visible: Int = 0
    var zan: Int = 0
    var tags: List<HomeTag>? = null
    var originId:Int = 0

    constructor(parcel: Parcel) : this() {
        apkLink = parcel.readString()
        author = parcel.readString()
        chapterId = parcel.readInt()
        chapterName = parcel.readString()
        collect = parcel.readByte() != 0.toByte()
        courseId = parcel.readInt()
        desc = parcel.readString()
        envelopePic = parcel.readString()
        isFresh = parcel.readByte() != 0.toByte()
        id = parcel.readInt()
        link = parcel.readString()
        niceDate = parcel.readString()
        origin = parcel.readString()
        projectLink = parcel.readString()
        publishTime = parcel.readLong()
        superChapterId = parcel.readInt()
        superChapterName = parcel.readString()
        title = parcel.readString()
        type = parcel.readInt()
        visible = parcel.readInt()
        zan = parcel.readInt()
        originId = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(apkLink)
        parcel.writeString(author)
        parcel.writeInt(chapterId)
        parcel.writeString(chapterName)
        parcel.writeByte(if (collect) 1 else 0)
        parcel.writeInt(courseId)
        parcel.writeString(desc)
        parcel.writeString(envelopePic)
        parcel.writeByte(if (isFresh) 1 else 0)
        parcel.writeInt(id)
        parcel.writeString(link)
        parcel.writeString(niceDate)
        parcel.writeString(origin)
        parcel.writeString(projectLink)
        parcel.writeLong(publishTime)
        parcel.writeInt(superChapterId)
        parcel.writeString(superChapterName)
        parcel.writeString(title)
        parcel.writeInt(type)
        parcel.writeInt(visible)
        parcel.writeInt(zan)
        parcel.writeInt(originId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HomeData> {
        override fun createFromParcel(parcel: Parcel): HomeData {
            return HomeData(parcel)
        }

        override fun newArray(size: Int): Array<HomeData?> {
            return arrayOfNulls(size)
        }
    }
}
