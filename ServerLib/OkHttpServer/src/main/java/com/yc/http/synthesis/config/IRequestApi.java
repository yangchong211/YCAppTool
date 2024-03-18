package com.yc.http.synthesis.config;

import androidx.annotation.NonNull;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2019/05/19
 *    desc   : 请求接口配置
 */
public interface IRequestApi {

    /**
     * 请求接口
     */
    @NonNull
    String getApi();
}