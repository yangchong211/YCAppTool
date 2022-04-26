package com.yc.jetpack.study.livedata;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TextViewModel extends ViewModel {

    /**
     * LiveData是抽象类，MutableLiveData是具体实现类
     */
    private MutableLiveData<String> mCurrentText;

    public MutableLiveData<String> getCurrentText() {
        if (mCurrentText == null) {
            mCurrentText = new MutableLiveData<>();
        }
        return mCurrentText;
    }

}
