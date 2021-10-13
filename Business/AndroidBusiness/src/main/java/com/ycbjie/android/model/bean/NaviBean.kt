package com.ycbjie.android.model.bean

import android.os.Parcel
import android.os.Parcelable


/**
 * <pre>
 *     @author 杨充
 *     blog  :
 *     time  : 2017/05/30
 *     desc  : 网站导航页面
 *     revise:
 * </pre>
 */
class NaviBean() : Parcelable {

    /**
     * articles : [{"apkLink":"","author":"小编","chapterId":272,"chapterName":"常用网站","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":1848,"link":"https://developers.google.cn/","niceDate":"2018-01-07","origin":"","projectLink":"","publishTime":1515322795000,"superChapterId":0,"superChapterName":"","tags":[],"title":"Google开发者","type":0,"visible":0,"zan":0},{"apkLink":"","author":"小编","chapterId":272,"chapterName":"常用网站","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":1849,"link":"http://www.github.com/","niceDate":"2018-01-07","origin":"","projectLink":"","publishTime":1515322817000,"superChapterId":0,"superChapterName":"","tags":[],"title":"Github","type":0,"visible":0,"zan":0},{"apkLink":"","author":"小编","chapterId":272,"chapterName":"常用网站","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":1850,"link":"https://stackoverflow.com/","niceDate":"2018-01-07","origin":"","projectLink":"","publishTime":1515322829000,"superChapterId":0,"superChapterName":"","tags":[],"title":"stackoverflow","type":0,"visible":0,"zan":0},{"apkLink":"","author":"小编","chapterId":272,"chapterName":"常用网站","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":1851,"link":"https://juejin.im/","niceDate":"2018-01-07","origin":"","projectLink":"","publishTime":1515323408000,"superChapterId":0,"superChapterName":"","tags":[],"title":"掘金","type":0,"visible":0,"zan":0},{"apkLink":"","author":"小编","chapterId":272,"chapterName":"常用网站","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":1852,"link":"https://www.csdn.net/","niceDate":"2018-01-07","origin":"","projectLink":"","publishTime":1515323423000,"superChapterId":0,"superChapterName":"","tags":[],"title":"CSDN","type":0,"visible":0,"zan":0},{"apkLink":"","author":"小编","chapterId":272,"chapterName":"常用网站","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":1853,"link":"https://www.jianshu.com/","niceDate":"2018-01-07","origin":"","projectLink":"","publishTime":1515323438000,"superChapterId":0,"superChapterName":"","tags":[],"title":"简书","type":0,"visible":0,"zan":0},{"apkLink":"","author":"小编","chapterId":272,"chapterName":"常用网站","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":1854,"link":"http://www.androidweekly.cn/","niceDate":"2018-01-07","origin":"","projectLink":"","publishTime":1515323568000,"superChapterId":0,"superChapterName":"","tags":[],"title":"开发技术周报","type":0,"visible":0,"zan":0},{"apkLink":"","author":"小编","chapterId":272,"chapterName":"常用网站","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":1855,"link":"https://toutiao.io/","niceDate":"2018-01-07","origin":"","projectLink":"","publishTime":1515323607000,"superChapterId":0,"superChapterName":"","tags":[],"title":"开发者头条","type":0,"visible":0,"zan":0},{"apkLink":"","author":"小编","chapterId":272,"chapterName":"常用网站","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":1856,"link":"https://segmentfault.com/t/android","niceDate":"2018-01-07","origin":"","projectLink":"","publishTime":1515323635000,"superChapterId":0,"superChapterName":"","tags":[],"title":"segmentfault","type":0,"visible":0,"zan":0},{"apkLink":"","author":"小编","chapterId":272,"chapterName":"常用网站","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":1857,"link":"http://www.androiddevtools.cn/","niceDate":"2018-01-07","origin":"","projectLink":"","publishTime":1515323651000,"superChapterId":0,"superChapterName":"","tags":[],"title":"androiddevtools","type":0,"visible":0,"zan":0},{"apkLink":"","author":"小编","chapterId":272,"chapterName":"常用网站","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":1858,"link":"https://developers.googleblog.cn/","niceDate":"2018-01-07","origin":"","projectLink":"","publishTime":1515323695000,"superChapterId":0,"superChapterName":"","tags":[],"title":"Google中文Blog","type":0,"visible":0,"zan":0},{"apkLink":"","author":"gank.io","chapterId":272,"chapterName":"常用网站","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":1859,"link":"http://gank.io/","niceDate":"2018-01-07","origin":"","projectLink":"","publishTime":1515323720000,"superChapterId":0,"superChapterName":"","tags":[],"title":"干货集中营","type":0,"visible":0,"zan":0},{"apkLink":"","author":"小编","chapterId":272,"chapterName":"常用网站","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":1862,"link":"http://a.codekk.com/","niceDate":"2018-01-07","origin":"","projectLink":"","publishTime":1515324437000,"superChapterId":0,"superChapterName":"","tags":[],"title":"CodeKK","type":0,"visible":0,"zan":0},{"apkLink":"","author":"小编","chapterId":272,"chapterName":"常用网站","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":1863,"link":"https://xiaozhuanlan.com/","niceDate":"2018-01-07","origin":"","projectLink":"","publishTime":1515324456000,"superChapterId":0,"superChapterName":"","tags":[],"title":"小专栏","type":0,"visible":0,"zan":0},{"apkLink":"","author":"小编","chapterId":272,"chapterName":"常用网站","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":1864,"link":"http://www.wanandroid.com/article/list/0?cid=176","niceDate":"2018-01-07","origin":"","projectLink":"","publishTime":1515324541000,"superChapterId":0,"superChapterName":"","tags":[],"title":"国内大牛","type":0,"visible":0,"zan":0},{"apkLink":"","author":"小编","chapterId":272,"chapterName":"常用网站","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":1865,"link":"https://github.com/android-cn/android-dev-com","niceDate":"2018-01-07","origin":"","projectLink":"","publishTime":1515324559000,"superChapterId":0,"superChapterName":"","tags":[],"title":"国外大牛","type":0,"visible":0,"zan":0},{"apkLink":"","author":"小编","chapterId":272,"chapterName":"常用网站","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":1866,"link":"https://www.androidos.net.cn/sourcecode","niceDate":"2018-01-07","origin":"","projectLink":"","publishTime":1515324594000,"superChapterId":0,"superChapterName":"","tags":[],"title":"Android源码","type":0,"visible":0,"zan":0},{"apkLink":"","author":"小编","chapterId":272,"chapterName":"常用网站","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":1867,"link":"http://design.1sters.com/","niceDate":"2018-01-07","origin":"","projectLink":"","publishTime":1515324880000,"superChapterId":0,"superChapterName":"","tags":[],"title":"Material Design 中文版","type":0,"visible":0,"zan":0},{"apkLink":"","author":"小编","chapterId":272,"chapterName":"常用网站","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":1868,"link":"https://leetcode.com/","niceDate":"2018-01-07","origin":"","projectLink":"","publishTime":1515325010000,"superChapterId":0,"superChapterName":"","tags":[],"title":"leetcode","type":0,"visible":0,"zan":0},{"apkLink":"","author":"小编","chapterId":272,"chapterName":"常用网站","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":2405,"link":"https://dl.google.com/dl/android/maven2/index.html","niceDate":"2018-02-25","origin":"","projectLink":"","publishTime":1519537704000,"superChapterId":0,"superChapterName":"","tags":[],"title":"google mvn仓库","type":0,"visible":1,"zan":0},{"apkLink":"","author":"小编","chapterId":272,"chapterName":"常用网站","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":2406,"link":"http://jcenter.bintray.com/","niceDate":"2018-02-25","origin":"","projectLink":"","publishTime":1519537722000,"superChapterId":0,"superChapterName":"","tags":[],"title":"jcenter仓库","type":0,"visible":1,"zan":0}]
     * cid : 272
     * name : 常用网站
     */

    var cid: Int = 0
    var name: String? = null
    var articles: MutableList<HomeData>? = null

    constructor(parcel: Parcel) : this()

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NaviBean> {
        override fun createFromParcel(parcel: Parcel): NaviBean {
            return NaviBean(parcel)
        }

        override fun newArray(size: Int): Array<NaviBean?> {
            return arrayOfNulls(size)
        }
    }

}
