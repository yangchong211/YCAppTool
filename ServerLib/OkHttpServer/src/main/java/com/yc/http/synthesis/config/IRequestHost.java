package com.yc.http.synthesis.config;

import androidx.annotation.NonNull;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2019/05/19
 *    desc   : 请求主机配置
 */
public interface IRequestHost {

    /**
     * 主机地址
     */
    @NonNull
    String getHost();
}