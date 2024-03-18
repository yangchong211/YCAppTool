package com.yc.http.synthesis.config;

import androidx.annotation.NonNull;

import com.yc.http.synthesis.annotation.HttpIgnore;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2019/05/19
 *    desc   : 请求接口简单配置类
 */
public class RequestApi implements IRequestApi {

    /** 接口地址 */
    @HttpIgnore
    private final String mApi;

    public RequestApi(String api) {
        mApi = api;
    }

    @NonNull
    @Override
    public String getApi() {
        return mApi;
    }

    @NonNull
    @Override
    public String toString() {
        return mApi;
    }
}