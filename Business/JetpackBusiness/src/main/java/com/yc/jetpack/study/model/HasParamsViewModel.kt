package com.yc.jetpack.study.model

import androidx.lifecycle.*
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.launch

class HasParamsViewModel(private val repository : SavedDataRepository) : ViewModel() {

    //数据观察对象
    private val nameLiveData = MutableLiveData<String>()

    //数据观察类
    fun getName(): LiveData<String> {
        LogUtils.d("name : ${repository.getName()}")
        return nameLiveData
    }

    fun getSupportData() {
        viewModelScope.launch {
            val name = repository.getName()
            val data = repository.getData()

            nameLiveData.postValue(name + data)
        }
    }

    fun saveNewName(newName: String) {
        nameLiveData.value = newName
    }
}