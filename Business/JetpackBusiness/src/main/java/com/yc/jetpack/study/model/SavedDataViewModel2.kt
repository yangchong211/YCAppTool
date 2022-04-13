package com.yc.jetpack.study.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.LogUtils

class SavedDataViewModel2(private val correction: String ,
                          private val repository : SavedDataRepository) : ViewModel() {

    //数据观察对象
    private val name = MutableLiveData<String>()

    //数据观察类
    fun getName(): LiveData<String> {
        LogUtils.d("name : ${repository.getName()}")
        return name
    }

    fun saveNewName(newName: String) {
        name.value = newName
    }
}