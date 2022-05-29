package com.yc.baseclasslib.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yc.baseclasslib.BuildConfig;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time   : 2019/5/18
 *     desc   : 实现所有fragment生命周期监听
 *     revise :
 * </pre>
 */
public class BaseLifecycleFragment extends BaseVisibilityFragment {

    /**
     * 当Fragment与宿主Activity建立联系的时候调用
     * @param context   上下文
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    /**
     * 用来完成Fragment的初始化创建工作
     * @param savedInstanceState    bundle
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Fragment已经对用户可见时调用，当然这个基于它的宿主Activity的onStart()方法已经被调用。
     */
    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * Fragment已经开始和用户交互时调用，当然这个基于它的宿主Activity的onResume()方法已经被调用。
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * Fragment不再和用户交互时调用，这通常发生在宿主Activity的onPause()方法被调用或者Fragment被修改（replace、remove）。
     */
    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * Fragment不再对用户可见时调用，这通常发生在宿主Activity的onStop()方法被调用或者Fragment被修改（replace、remove）。
     */
    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * 创建并返回View给Fragment
     * @param inflater      填充器
     * @param container     父布局
     * @param savedInstanceState    bundle
     * @return      返回View
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 试图创建后调用该方法
     * @param view          view
     * @param savedInstanceState        bundle
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * 通知Fragment当前Activity的onCreate()方法已经调用完成
     * @param savedInstanceState        bundle
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Fragment释放View资源时调用
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * Fragment与宿主Activity脱离联系时调用
     */
    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * Fragment与宿主Activity销毁时调用
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 通知Fragment以前保存的View状态都已经被恢复
     * @param savedInstanceState        bundle
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    private void info(String s) {
        if (BuildConfig.DEBUG) {
            Log.i(this.getClass().getSimpleName(), s);
        }
    }

}
