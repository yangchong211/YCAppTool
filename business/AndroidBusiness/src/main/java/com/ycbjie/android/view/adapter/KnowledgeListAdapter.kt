package com.ycbjie.android.view.adapter


import android.annotation.SuppressLint
import android.app.Activity
import android.view.ViewGroup
import android.widget.*
import com.ycbjie.android.model.bean.HomeData
import com.ycbjie.android.R
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder


/**
 * <pre>
 *     @author yangchong
 *     blog  :
 *     time  : 2018/7/17
 *     desc  :
 *     revise:
 * </pre>
 */
class KnowledgeListAdapter: RecyclerArrayAdapter<HomeData> {


    private var activity: Activity?

    constructor(activity: Activity?) : super(activity){
        this.activity = activity
    }

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<HomeData> {
        return MyViewHolder(parent)
    }

    private inner class MyViewHolder internal constructor(parent: ViewGroup) :
            BaseViewHolder<HomeData>(parent, R.layout.item_knowledge_list) {

        private val llInfo: LinearLayout = getView(R.id.llInfo)
        private val ttIvHead: ImageView = getView(R.id.ttIvHead)
        private val ttTvName: TextView = getView(R.id.ttTvName)
        private val ivMore: ImageView = getView(R.id.ivMore)
        private val flLike: FrameLayout = getView(R.id.flLike)
        private val ivLike: ImageView = getView(R.id.ivLike)
        private val tvContent: TextView = getView(R.id.tvContent)
        private val tvTime: TextView = getView(R.id.tvTime)

        init {

        }

        @SuppressLint("SetTextI18n")
        override fun setData(item: HomeData) {
            super.setData(item)
            ttTvName.text = item.author
            tvContent.text = item.title
            tvTime.text = item.niceDate
        }
    }


}

