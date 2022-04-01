package com.yc.animbusiness;

import com.blankj.utilcode.util.LogUtils;
import com.pedaily.yc.ycdialoglib.toast.ToastUtils;
import com.yc.businessinterface.IAnimServiceProvider;
import com.yc.spi.annotation.ServiceProvider;

@ServiceProvider(IAnimServiceProvider.class)
public class AnimProviderImpl implements IAnimServiceProvider {

    public static final String TAG = "AnimProviderImpl";

    @Override
    public void setStartAnim() {
        LogUtils.d(TAG,"setStartAnim");
        ToastUtils.showRoundRectToast("开始动画");
    }

    @Override
    public void setAnimTime(int time) {
        LogUtils.d(TAG,"setAnimTime : "+time);
        ToastUtils.showRoundRectToast("设置动画时间");
    }

    @Override
    public int getAnimTime() {
        LogUtils.d(TAG,"getAnimTime");
        ToastUtils.showRoundRectToast("获取动画时间");
        return 10;
    }
}
