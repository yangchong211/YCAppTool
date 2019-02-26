package com.ycbjie.android.view.adapter


import android.app.Activity
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.ViewGroup
import android.widget.ImageView
import com.ycbjie.android.R
import com.ycbjie.android.model.bean.HomeData
import com.ycbjie.android.model.bean.ProjectListBean
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder


class AndroidCollectAdapter : RecyclerArrayAdapter<HomeData>{


    constructor(activity: Activity?) : super(activity)

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ProjectListBean> {
        return ViewHolder(parent)
    }


    private inner class ViewHolder internal constructor(parent: ViewGroup) :
            BaseViewHolder<ProjectListBean>(parent, R.layout.item_android_home_news) {

        internal var ttIvHead: ImageView = getView(R.id.ttIvHead)

        init {
            addOnClickListener(R.id.ivMore)
            addOnClickListener(R.id.flLike)
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun setData(data: ProjectListBean?) {
            super.setData(data)
        }
    }




}

