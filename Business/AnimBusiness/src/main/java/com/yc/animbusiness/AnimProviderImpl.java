package com.yc.animbusiness;

import com.blankj.utilcode.util.LogUtils;
import com.yc.api.getIt.ServiceProvider;

@ServiceProvider(IAnimProvider.class)
public class AnimProviderImpl implements IAnimProvider{

    public static final String TAG = "AnimProviderImpl";

    @Override
    public void setStartAnim() {
        LogUtils.d(TAG,"setStartAnim");
    }

    @Override
    public void setAnimTime(int time) {
        LogUtils.d(TAG,"setAnimTime : "+time);
    }

    @Override
    public int getAnimTime() {
        LogUtils.d(TAG,"getAnimTime");
        return 10;
    }
}
