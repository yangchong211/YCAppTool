package com.yc.baseclasslib.fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2017/5/18
 *     desc   : 懒加载Fragment基类
 *     revise :
 * </pre>
 */
public abstract class BaseLazyFragment extends BaseVisibilityFragment {

    /**
     * 懒加载过
     */
    private boolean isLazyLoaded = false;
    /**
     * Fragment的View加载完毕的标记
     */
    private boolean isPrepared = false;

    /**
     * 当Fragment与宿主Activity建立联系的时候调用
     * @param context 上下文
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        addOnVisibilityChangedListener(listener);
    }

    /**
     * Fragment与宿主Activity脱离联系时调用
     */
    @Override
    public void onDetach() {
        super.onDetach();
        removeOnVisibilityChangedListener(listener);
    }

    private final OnFragmentVisibilityListener listener = new OnFragmentVisibilityListener() {
        @Override
        public void onFragmentVisibilityChanged(boolean visibility) {
            if (visibility){
                //页面可见的时候会回调该方法
                //第二步：这里调用一次lazyLoad(）
                lazyLoad();
            }
        }
    };

    /**
     * 第一步,改变isPrepared标记
     * 当onViewCreated()方法执行时,表明View已经加载完毕,此时改变isPrepared标记为true
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isPrepared = true;
    }

    /**
     * 调用懒加载
     * 第三步:在lazyLoad()方法中进行双重标记判断,通过后即可进行数据加载
     */
    private void lazyLoad() {
        if (isPrepared && !isLazyLoaded) {
            onLazyLoad();
            isLazyLoaded = true;
        }
    }

    /**
     * 第四步:定义抽象方法onLazyLoad(),具体加载数据的工作,交给子类去完成
     */
    @UiThread
    public abstract void onLazyLoad();


}
