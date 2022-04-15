package com.ycbjie.android.tools.base

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter
import org.yczbj.ycrefreshviewlib.inter.InterItemView

/**
 * <pre>
 *     @author 杨充
 *     blog  :
 *     time  : 2018/02/30
 *     desc  : kotlin学习：
 *     revise:
 * </pre>
 */

abstract class BaseItemView(private val activity: Activity?, @param:LayoutRes private val id: Int)
    : InterItemView {

    override fun onCreateView(parent: ViewGroup): View {
        return LayoutInflater.from(activity).inflate(id, parent, false)
    }

    override fun onBindView(headerView: View) {
        setBindView(headerView)
    }

    abstract fun setBindView(headerView: View)

}
