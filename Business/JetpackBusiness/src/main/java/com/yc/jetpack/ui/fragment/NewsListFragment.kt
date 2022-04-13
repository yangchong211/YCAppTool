package com.yc.jetpack.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.yc.jetpack.R
import com.yc.jetpack.viewmodle.NewsListModel

class NewsListFragment : Fragment() {

    private var viewModel: NewsListModel ?= null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(NewsListModel::class.java)
        //添加注册
        viewModel?.newsListInfoLiveData()?.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()){
                return@Observer
            }

        })
    }

    override fun onResume() {
        super.onResume()
        //开始获取数据
        viewModel?.newsListInfoLiveData()
    }

}