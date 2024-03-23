package com.yc.jetpack.study.model

import android.util.Log
import androidx.lifecycle.*
import com.yc.toolutils.system.AppLogUtils
import kotlinx.coroutines.launch

class HasParamsViewModel(private val repository : SavedDataRepository) : ViewModel() {

    //数据观察对象
    private val nameLiveData = MutableLiveData<String>()

    //懒加载
    private val nameLiveDate2 : MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    //数据观察类
    fun getName(): LiveData<String> {
        AppLogUtils.d("name : ${repository.getName()}")
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

    /**
     * 销毁的时候用来清除操作
     */
    override fun onCleared() {
        super.onCleared()
        Log.d("ViewModel" , "onCleared")
    }
}