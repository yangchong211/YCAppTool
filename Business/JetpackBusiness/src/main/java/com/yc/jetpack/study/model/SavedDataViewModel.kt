package com.yc.jetpack.study.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.LogUtils

class SavedDataViewModel(private val repository : SavedDataRepository) : ViewModel() {

    //用户获取有参数model
    class ViewModeFactory(private val repository: SavedDataRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SavedDataViewModel(repository) as T
        }
    }

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