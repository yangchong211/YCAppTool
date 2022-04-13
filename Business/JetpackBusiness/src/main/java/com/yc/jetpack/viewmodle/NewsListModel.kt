package com.yc.jetpack.viewmodle

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * 继承ViewModel类
 */
class NewsListModel() : ViewModel(){

    /**
     * 集合
     */
    private val newsListInfo : MutableLiveData<List<String>?> by lazy(::MutableLiveData)

    /**
     * 集合观察类
     */
    fun newsListInfoLiveData(): MutableLiveData<List<String>?> {
        return newsListInfo
    }

    /**
     * 调用方法请求数据
     */
    fun getNewsList(){
        viewModelScope.launch {

        }
    }

}