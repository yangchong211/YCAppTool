package com.ycbjie.android.view.adapter


import android.annotation.SuppressLint
import android.app.Activity
import android.support.v7.widget.ActionMenuView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.ycbjie.android.model.bean.HomeData
import com.ycbjie.android.model.bean.NaviBean
import com.ycbjie.android.view.weight.FlowLayout
import com.ycbjie.android.R
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder


/**
 * <pre>
 *     @author 杨充
 *     blog  :
 *     time  : 2017/05/30
 *     desc  : 网站导航页面
 *     revise:
 * </pre>
 */
class AndroidNavWebsiteAdapter: RecyclerArrayAdapter<NaviBean> {

    var layoutInflater: LayoutInflater? = null
    var itemClickLister: ItemClickListener? = null
    private var activity: Activity?

    constructor(activity: Activity?) : super(activity){
        this.activity = activity
        layoutInflater = LayoutInflater.from(activity)
    }

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<NaviBean> {
        return MyViewHolder(parent)
    }

    private inner class MyViewHolder internal constructor(parent: ViewGroup) :
            BaseViewHolder<NaviBean>(parent, R.layout.item_web_navi) {

        private val tvTitle: TextView = getView(R.id.tvTitle)
        private val flowLayout: FlowLayout = getView(R.id.flowLayout)

        init {
            addOnClickListener(R.id.flowLayout)
        }

        @SuppressLint("SetTextI18n")
        override fun setData(item: NaviBean) {
            super.setData(item)
            tvTitle.text = item.name
            val articles: MutableList<HomeData> = item.articles!!
            //addOnClickListener(R.id.flowLayout)
            val views = mutableListOf<View>()
            for (tag in articles) {
                var textView = layoutInflater!!.inflate(R.layout.tag_view_flowlayout, null) as TextView
                textView.text = tag.title
                textView.id = tag.id
                val margin = ViewGroup.MarginLayoutParams(ActionMenuView.LayoutParams.WRAP_CONTENT, ActionMenuView.LayoutParams.WRAP_CONTENT)
                margin.rightMargin = 10
                margin.topMargin = 10
                margin.leftMargin = 10
                margin.bottomMargin = 10
                textView.layoutParams = margin
                textView.setTextColor(itemView.context.resources.getColor(R.color.blackText3))
                views.add(textView)

                textView.setOnClickListener({
                    itemClickLister?.itemClick(tag)
                })
            }
            flowLayout.addItems(views)
        }
    }

    interface ItemClickListener {
        fun itemClick(data: HomeData)
    }

}

