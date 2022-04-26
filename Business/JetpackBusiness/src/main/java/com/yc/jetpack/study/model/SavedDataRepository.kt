package com.yc.jetpack.study.model

import androidx.lifecycle.MutableLiveData

/**
 * 提供一些数据
 */
class SavedDataRepository {

    fun getName(): String {
        return "yc"
    }

    suspend fun getData(): String {
        return "你就是个逗比"
    }

}