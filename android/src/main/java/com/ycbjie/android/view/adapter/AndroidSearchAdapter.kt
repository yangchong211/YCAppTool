package com.ycbjie.android.view.adapter


import android.annotation.SuppressLint
import android.app.Activity
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.ycbjie.android.R
import com.ycbjie.android.model.bean.HomeData
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder


/**
 * <pre>
 *     @author yangchong
 *     blog  :
 *     time  : 2018/7/17
 *     desc  : 搜索页面适配器
 *     revise:
 * </pre>
 */
class AndroidSearchAdapter: RecyclerArrayAdapter<HomeData> {


    private var activity: Activity?
    private val inflater: LayoutInflater
    companion object {
        val VIEW_TYPE_SELECTION = 0
        val VIEW_TYPE_HISTORY = 1
        val VIEW_TYPE_RECOMMEND = 2
        val VIEW_TYPE_RESULT = 3
        val VIEW_TYPE_HISTORY_SELECTION = 4
    }

    constructor(activity: Activity?) : super(activity){
        this.activity = activity
        inflater = LayoutInflater.from(activity)
    }

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<HomeData> {
        if (viewType == VIEW_TYPE_SELECTION) {
            return SelectionViewHolder(parent)
        } else if (viewType == VIEW_TYPE_HISTORY) {
            return HistoryViewHolder(parent)
        }else if (viewType == VIEW_TYPE_RECOMMEND) {
            return RecommendViewHolder(parent)
        }else if (viewType == VIEW_TYPE_RESULT) {
            return ResultViewHolder(parent)
        }else if (viewType == VIEW_TYPE_HISTORY_SELECTION) {
            return HistorySelectionViewHolder(parent)
        }
        return ResultViewHolder(parent)
    }

    override fun getViewType(position: Int): Int {
        return super.getViewType(position)
    }

    private inner class SelectionViewHolder internal constructor(parent: ViewGroup) :
            BaseViewHolder<HomeData>(parent, R.layout.item_project_list) {

        private val ivHead: ImageView = getView(R.id.ivHead)

        init {
            //绑定子view的点击事件
        }

        @SuppressLint("SetTextI18n")
        override fun setData(item: HomeData) {
            super.setData(item)
        }
    }

    private inner class HistoryViewHolder internal constructor(parent: ViewGroup) :
            BaseViewHolder<HomeData>(parent, R.layout.item_project_list) {

        private val ivHead: ImageView = getView(R.id.ivHead)

        init {
            //绑定子view的点击事件
        }

        @SuppressLint("SetTextI18n")
        override fun setData(item: HomeData) {
            super.setData(item)
        }
    }
    private inner class RecommendViewHolder internal constructor(parent: ViewGroup) :
            BaseViewHolder<HomeData>(parent, R.layout.item_project_list) {

        private val ivHead: ImageView = getView(R.id.ivHead)

        init {
            //绑定子view的点击事件
        }

        @SuppressLint("SetTextI18n")
        override fun setData(item: HomeData) {
            super.setData(item)
        }
    }
    private inner class ResultViewHolder internal constructor(parent: ViewGroup) :
            BaseViewHolder<HomeData>(parent, R.layout.item_collect_article) {

        private val ivHead: ImageView = getView(R.id.ivHead)
        private val tvName: TextView = getView(R.id.tvName)
        private val flLike: FrameLayout = getView(R.id.flLike)
        private val ivLike: ImageView = getView(R.id.ivLike)
        private val tvContent: TextView = getView(R.id.tvContent)
        private val tvTime: TextView = getView(R.id.tvTime)

        init {
            //绑定子view的点击事件
        }

        @SuppressLint("SetTextI18n")
        override fun setData(item: HomeData) {
            super.setData(item)
            var homeData = item
            tvName.text = homeData.author
            @Suppress("DEPRECATION")
            tvContent.text = Html.fromHtml(homeData.title)
            tvTime.text = homeData.niceDate
            if (homeData.collect) {
                ivLike.setBackgroundColor(context.resources.getColor(R.color.blackText3))
            } else {
                ivLike.setBackgroundColor(context.resources.getColor(R.color.colorAccent))
            }

        }
    }
    private inner class HistorySelectionViewHolder internal constructor(parent: ViewGroup) :
            BaseViewHolder<HomeData>(parent, R.layout.item_project_list) {

        private val ivHead: ImageView = getView(R.id.ivHead)

        init {
            //绑定子view的点击事件
        }

        @SuppressLint("SetTextI18n")
        override fun setData(item: HomeData) {
            super.setData(item)
        }
    }

}

