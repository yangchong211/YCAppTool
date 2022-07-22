package com.yc.lifehelper

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * @author: 杨充
 * @email  : yangchong211@163.com
 * @time   : 2018/04/28
 * @desc   : apm小工具，模拟滑动卡顿
 * @revise :
 */
class BlockListActivity : FragmentActivity() {

    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: MyAdapter? = null
    private val mListData = ArrayList<Int>()

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_block_list)
        mRecyclerView = findViewById(R.id.rv_block_list)
        for (i in 0..99) {
            mListData.add(i)
        }
        mRecyclerView?.layoutManager = LinearLayoutManager(this)
        mAdapter = MyAdapter()
        mRecyclerView?.adapter = mAdapter

        //模拟滑动卡顿的情况
        mRecyclerView?.setOnScrollChangeListener(View.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (Math.random() < 0.01) {
                try {
                    Thread.sleep(2600)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        })
    }

    internal inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView
        fun setData(position: Int) {
            textView.text = mListData[position].toString()
        }

        init {
            textView = itemView.findViewById(R.id.tv_block_list)
        }
    }

    internal inner class MyAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
            val v = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_block_scrolling, viewGroup, false)
            return MyHolder(v)
        }

        override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, i: Int) {
            (viewHolder as MyHolder).setData(i)
        }

        override fun getItemCount(): Int {
            return mListData.size
        }
    }
}