package com.ycbjie.android.view.adapter


import android.annotation.SuppressLint
import android.app.Activity
import android.view.ViewGroup
import android.widget.*
import com.ycbjie.android.model.bean.TreeBean
import com.ycbjie.android.R
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder


/**
 * <pre>
 *     @author yangchong
 *     blog  :
 *     time  : 2018/7/17
 *     desc  : 选择优惠券适配器
 *     revise:
 * </pre>
 */
class AndroidProjectTreeAdapter: RecyclerArrayAdapter<TreeBean> {


    private var activity: Activity?

    constructor(activity: Activity?) : super(activity){
        this.activity = activity
    }

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<TreeBean> {
        return MyViewHolder(parent)
    }

    private inner class MyViewHolder internal constructor(parent: ViewGroup) :
            BaseViewHolder<TreeBean>(parent, R.layout.item_project_tree_list) {

        private val rbCheck: CheckBox = getView(R.id.rbCheck)
        private val tvText: TextView = getView(R.id.tvText)

        init {

        }

        @SuppressLint("SetTextI18n")
        override fun setData(item: TreeBean) {
            super.setData(item)
            tvText.text = item.name
            rbCheck.isChecked = selectProject?.id == item.id
        }
    }

    private var selectProject: TreeBean? = null
    fun setSelect(selectBean: TreeBean) {
        this.selectProject = selectBean
    }

}

