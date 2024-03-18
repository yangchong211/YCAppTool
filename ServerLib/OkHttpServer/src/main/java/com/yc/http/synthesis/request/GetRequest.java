package com.yc.http.synthesis.request;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.yc.http.synthesis.model.HttpMethod;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2019/07/20
 *    desc   : Get 请求
 */
public final class GetRequest extends UrlRequest<GetRequest> {

    public GetRequest(LifecycleOwner lifecycleOwner) {
        super(lifecycleOwner);
    }

    @NonNull
    @Override
    public String getRequestMethod() {
        return HttpMethod.GET.toString();
    }
}