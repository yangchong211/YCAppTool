package com.ycbjie.android.view.adapter


import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.ycbjie.android.model.bean.HomeData
import com.ycbjie.android.R
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder


class AndroidHomeAdapter : RecyclerArrayAdapter<HomeData>{


    constructor(activity: Activity?) : super(activity)

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<HomeData> {
        return ViewHolder(parent)
    }


    private inner class ViewHolder internal constructor(parent: ViewGroup) :
            BaseViewHolder<HomeData>(parent, R.layout.item_android_home_news) {

        internal var ttIvHead: ImageView = getView(R.id.ttIvHead)
        internal var ttTvName: TextView = getView(R.id.ttTvName)
        internal var ivMore: ImageView = getView(R.id.ivMore)
        internal var flLike: FrameLayout = getView(R.id.flLike)
        internal var ivLike: ImageView = getView(R.id.ivLike)
        internal var tvContent: TextView = getView(R.id.tvContent)
        internal var tvTagTitle: TextView = getView(R.id.tvTagTitle)
        internal var tvSuperChapterName: TextView = getView(R.id.tvSuperChapterName)
        internal var tvLip: TextView = getView(R.id.tvLip)
        internal var tvChildChapterName: TextView = getView(R.id.tvChildChapterName)
        internal var tvTime: TextView = getView(R.id.tvTime)

        init {
            addOnClickListener(R.id.ivMore)
            addOnClickListener(R.id.flLike)
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun setData(data: HomeData?) {
            super.setData(data)
            val homeData: HomeData = data as HomeData
            ttTvName.text = homeData.author
            tvContent.text = homeData.title
            tvTime.text = homeData.niceDate
            tvSuperChapterName.text = homeData.superChapterName
            tvChildChapterName.text = homeData.chapterName
            tvContent.text = homeData.title
            val collect = homeData.collect
            if (collect) {
                ivLike.setBackgroundColor(Color.GRAY)
            } else {
                ivLike.setBackgroundColor(Color.GRAY)
            }
        }
    }


}

