package com.ycbjie.todo.dagger;


import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/11/14.
 * 描    述：此版块训练dagger2+MVP
 * 修订历史：
 * ================================================
 */

@Module
public class UiModule {

    private AppCompatActivity mActivity;

    public UiModule(AppCompatActivity activity) {
        mActivity = activity;
    }

    @Provides
    public AppCompatActivity getActivity() {
        return mActivity;
    }

    @Provides
    public Context getContext() {
        return mActivity;
    }

    @Provides
    FragmentManager fragmentManager() {
        return mActivity.getSupportFragmentManager();
    }


}
