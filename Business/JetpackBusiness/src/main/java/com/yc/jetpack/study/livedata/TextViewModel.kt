package com.yc.jetpack.study.livedata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TextViewModel : ViewModel() {
    /**
     * LiveData是抽象类，MutableLiveData是具体实现类
     */
    private var mCurrentText: MutableLiveData<String>? = null

    private val mNextText: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    fun getCurrentText(): MutableLiveData<String>{
        if (mCurrentText == null) {
            mCurrentText = MutableLiveData()
        }
        return mCurrentText as MutableLiveData<String>
    }

    fun getNextText() : MutableLiveData<String> = mNextText

    fun getNextText2() : MutableLiveData<String> {
        return mNextText
    }

}