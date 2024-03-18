package com.yc.appserver.http.http.api;

import androidx.annotation.NonNull;

import com.yc.http.synthesis.config.IRequestApi;
import com.yc.http.synthesis.config.IRequestServer;

import java.io.File;

public final class UpdateImageApi implements IRequestServer, IRequestApi {

    @NonNull
    @Override
    public String getHost() {
        return "https://graph.baidu.com/";
    }

    @NonNull
    @Override
    public String getApi() {
        return "upload/";
    }

    /**
     * 本地图片
     */
    private File image;

    public UpdateImageApi(File image) {
        this.image = image;
    }

    public UpdateImageApi setImage(File image) {
        this.image = image;
        return this;
    }
}