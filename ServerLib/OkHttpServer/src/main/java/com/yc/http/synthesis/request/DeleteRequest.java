package com.yc.http.synthesis.request;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.yc.http.synthesis.model.HttpMethod;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2020/10/07
 *    desc   : Delete 请求
 *    doc    : Delete 请求该用 Url 还是 Body 来传递参数：
 *             https://stackoverflow.com/questions/299628/is-an-entity-body-allowed-for-an-http-delete-request
 */
public final class DeleteRequest extends UrlRequest<DeleteRequest> {

    public DeleteRequest(LifecycleOwner lifecycleOwner) {
        super(lifecycleOwner);
    }

    @NonNull
    @Override
    public String getRequestMethod() {
        return HttpMethod.DELETE.toString();
    }
}