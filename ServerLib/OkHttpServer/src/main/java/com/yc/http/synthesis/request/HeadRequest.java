package com.yc.http.synthesis.request;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.yc.http.synthesis.model.HttpMethod;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2020/10/07
 *    desc   : Head 请求
 */
public final class HeadRequest extends UrlRequest<HeadRequest> {

    public HeadRequest(LifecycleOwner lifecycleOwner) {
        super(lifecycleOwner);
    }

    @NonNull
    @Override
    public String getRequestMethod() {
        return HttpMethod.HEAD.toString();
    }
}