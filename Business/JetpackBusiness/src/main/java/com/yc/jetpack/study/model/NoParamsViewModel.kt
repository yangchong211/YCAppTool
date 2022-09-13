package com.yc.jetpack.study.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NoParamsViewModel : ViewModel() {

    //数据观察对象
    private val name = MutableLiveData<String>()

    //数据观察类
    fun getName(): MutableLiveData<String> {
        return name
    }

    fun saveNewName(newName: String) {
        name.value = newName
    }
}