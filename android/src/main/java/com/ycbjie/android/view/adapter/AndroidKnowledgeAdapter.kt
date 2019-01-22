package com.ycbjie.android.view.adapter


import android.support.v4.app.FragmentActivity
import android.support.v7.widget.ActionMenuView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ycbjie.android.model.bean.TreeBean
import com.ycbjie.android.view.weight.FlowLayout
import com.ycbjie.android.R
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder


class AndroidKnowledgeAdapter : RecyclerArrayAdapter<TreeBean>{

    var openTags = mutableListOf<Int>()

    constructor(activity: FragmentActivity?) : super(activity)

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<TreeBean> {
        return ViewHolder(parent)
    }


    private inner class ViewHolder internal constructor(parent: ViewGroup) :
            BaseViewHolder<TreeBean>(parent, R.layout.item_android_knowledge_tree) {

        internal var tvName: TextView = getView(R.id.tvName)
        internal var tvKinds: TextView = getView(R.id.tvKinds)
        internal var flowLayout: FlowLayout = getView(R.id.flowLayout)

        override fun setData(data: TreeBean?) {
            super.setData(data)
            if (data != null) {
                tvName.text = data.name
                var kind: String = data.children?.size.toString()
                kind += "分类"
                tvKinds.text = kind

                var tags: MutableList<TreeBean> = data.children as MutableList<TreeBean>

                var views = mutableListOf<View>()
                for (tag in tags) {

                    val textView = LayoutInflater.from(context)!!.inflate(R.layout.tag_view_flowlayout, null) as TextView
                    textView.text = tag.name
                    textView.id = tag.id
                    val margin = ViewGroup.MarginLayoutParams(ActionMenuView.LayoutParams.WRAP_CONTENT, ActionMenuView.LayoutParams.WRAP_CONTENT)
                    margin.rightMargin = 10
                    margin.topMargin = 10
                    margin.leftMargin = 10
                    margin.bottomMargin = 10
                    textView.layoutParams = margin
                    views.add(textView)

                    textView.setOnClickListener({
                        knowledgeItemClick?.knowledgeItemClick(tag, tags.indexOf(tag), dataPosition)
                    })
                }
                flowLayout.addItems(views)

                if (openTags.contains(data.id)) {
                    flowLayout.visibility = View.VISIBLE
                } else {
                    flowLayout.visibility = View.GONE
                }

                tvKinds.setOnClickListener({
                    if (flowLayout.visibility == View.VISIBLE) {
                        flowLayout.visibility = View.GONE
                        openTags.remove(data.id)
                    } else {
                        flowLayout.visibility = View.VISIBLE
                        openTags.add(data.id)
                    }
                })

            }
        }
    }

    var knowledgeItemClick: KnowledgeItemListener? = null
    interface KnowledgeItemListener {
        fun knowledgeItemClick(bean: TreeBean, index: Int, position: Int)
    }

}

