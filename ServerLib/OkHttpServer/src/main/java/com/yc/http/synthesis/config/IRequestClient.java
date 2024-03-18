package com.yc.http.synthesis.config;

import androidx.annotation.NonNull;

import com.yc.http.synthesis.EasyConfig;

import okhttp3.OkHttpClient;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2021/03/02
 *    desc   : OkHttpClient 配置
 */
public interface IRequestClient {

    /**
     * 获取 OkHttpClient
     */
    @NonNull
    default OkHttpClient getOkHttpClient() {
        return EasyConfig.getInstance().getClient();
    }
}