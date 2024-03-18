package com.yc.http.synthesis.config;

import androidx.annotation.NonNull;

import com.yc.http.synthesis.model.BodyType;
import com.yc.http.synthesis.model.CacheMode;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2019/05/19
 *    desc   : 请求服务配置
 */
public interface IRequestServer extends
        IRequestHost, IRequestClient,
        IRequestType, IRequestCache {

    @NonNull
    @Override
    default BodyType getBodyType() {
        // 默认以表单的方式提交
        return BodyType.FORM;
    }

    @NonNull
    @Override
    default CacheMode getCacheMode() {
        // 默认的缓存方式
        return CacheMode.DEFAULT;
    }

    @Override
    default long getCacheTime() {
        return Long.MAX_VALUE;
    }
}