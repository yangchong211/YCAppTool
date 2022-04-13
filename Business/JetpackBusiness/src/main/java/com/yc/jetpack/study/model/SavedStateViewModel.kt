package com.yc.jetpack.study.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SavedStateViewModel : ViewModel() {

    //数据观察对象
    private val name = MutableLiveData<String>()

    //数据观察类
    fun getName(): LiveData<String> {
        return name
    }

    fun saveNewName(newName: String) {
        name.value = newName
    }
}