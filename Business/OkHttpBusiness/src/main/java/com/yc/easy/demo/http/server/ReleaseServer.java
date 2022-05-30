package com.yc.easy.demo.http.server;

import androidx.annotation.NonNull;

import com.yc.http.config.IRequestServer;


public class ReleaseServer implements IRequestServer {

    @NonNull
    @Override
    public String getHost() {
        return "https://www.wanandroid.com/";
    }
}