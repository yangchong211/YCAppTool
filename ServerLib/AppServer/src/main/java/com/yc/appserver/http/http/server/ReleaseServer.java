package com.yc.appserver.http.http.server;

import androidx.annotation.NonNull;

import com.yc.http.synthesis.config.IRequestServer;


public class ReleaseServer implements IRequestServer {

    @NonNull
    @Override
    public String getHost() {
        return "https://www.wanandroid.com/";
    }
}