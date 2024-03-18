package com.yc.http.synthesis.request;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.yc.http.synthesis.model.HttpMethod;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2021/04/29
 *    desc   : Trace 请求
 */
public final class TraceRequest extends UrlRequest<TraceRequest> {

    public TraceRequest(LifecycleOwner lifecycleOwner) {
        super(lifecycleOwner);
    }

    @NonNull
    @Override
    public String getRequestMethod() {
        return HttpMethod.TRACE.toString();
    }
}