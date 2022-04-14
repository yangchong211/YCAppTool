package com.yc.jetpack.study.model

import androidx.lifecycle.*
import com.blankj.utilcode.util.LogUtils
import com.bumptech.glide.load.engine.Resource
import kotlinx.coroutines.launch

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

    fun getSupportDeviceList() {
        viewModelScope.launch {
            val resource = repository.getName()
            val data = repository.getData()
        }
    }

    fun saveNewName(newName: String) {
        name.value = newName
    }
}