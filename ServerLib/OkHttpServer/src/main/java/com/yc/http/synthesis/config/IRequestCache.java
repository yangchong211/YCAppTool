package com.yc.http.synthesis.config;

import androidx.annotation.NonNull;

import com.yc.http.synthesis.model.CacheMode;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2021/05/22
 *    desc   : 请求缓存配置
 */
public interface IRequestCache {

    /**
     * 获取缓存的模式
     */
    @NonNull
    CacheMode getCacheMode();

    /**
     * 获取缓存的有效时长（以毫秒为单位）
     */
    long getCacheTime();
}