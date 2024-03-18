package com.yc.http.synthesis.config;

import androidx.annotation.NonNull;

import com.yc.http.synthesis.annotation.HttpIgnore;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2019/05/19
 *    desc   : 服务器简单配置
 */
public final class RequestServer implements IRequestServer {

    /** 主机地址 */
    @HttpIgnore
    private final String mHost;

    public RequestServer(String host) {
        mHost = host;
    }

    @NonNull
    @Override
    public String getHost() {
        return mHost;
    }

    @NonNull
    @Override
    public String toString() {
        return mHost;
    }
}