package com.yc.http.synthesis.request;

import androidx.lifecycle.LifecycleOwner;

import com.yc.http.synthesis.EasyConfig;
import com.yc.http.synthesis.EasyLog;
import com.yc.http.synthesis.model.BodyType;
import com.yc.http.synthesis.model.HttpHeaders;
import com.yc.http.synthesis.model.HttpParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2020/10/07
 *    desc   : 不带 RequestBody 的请求
 */
public abstract class UrlRequest<T extends UrlRequest<?>> extends HttpRequest<T> {

    public UrlRequest(LifecycleOwner lifecycleOwner) {
        super(lifecycleOwner);
    }

    @Override
    protected void addHttpParams(HttpParams params, String key, Object value, BodyType type) {
        params.put(key, value);
    }

    @Override
    protected void addRequestParams(Request.Builder requestBuilder, HttpParams params, BodyType type) {
        HttpUrl.Builder urlBuilder = requestBuilder.build().url().newBuilder();
        // 添加参数
        if (!params.isEmpty()) {
            for (String key : params.getKeys()) {
                Object value = params.get(key);
                if (value instanceof List) {
                    // 如果这是一个 List 集合
                    List<?> list = ((List<?>) value);
                    for (Object itemValue : list) {
                        if (itemValue == null) {
                            continue;
                        }
                        // Get 请求参数重复拼接：https://blog.csdn.net/weixin_38355349/article/details/104499948
                        urlBuilder.addQueryParameter(key, String.valueOf(itemValue));
                    }
                } else if (value instanceof HashMap) {
                    // 如果这是一个 Map 集合
                    Map<?, ?> map = ((Map<?, ?>) value);
                    for (Object itemKey : map.keySet()) {
                        if (itemKey == null) {
                            continue;
                        }
                        Object itemValue = map.get(itemKey);
                        if (itemValue == null) {
                            continue;
                        }
                        urlBuilder.addQueryParameter(key, String.valueOf(itemValue));
                    }
                } else {
                    urlBuilder.addQueryParameter(key, String.valueOf(value));
                }
            }
        }
        HttpUrl link = urlBuilder.build();
        requestBuilder.url(link);
        requestBuilder.method(getRequestMethod(), null);
    }

    @Override
    protected void printRequestLog(Request request, HttpParams params, HttpHeaders headers, BodyType type) {
        if (!EasyConfig.getInstance().isLogEnabled()) {
            return;
        }

        EasyLog.printKeyValue(this, "RequestUrl", String.valueOf(request.url()));
        EasyLog.printKeyValue(this, "RequestMethod", getRequestMethod());

        if (!headers.isEmpty() || !params.isEmpty()) {
            EasyLog.printLine(this);
        }

        for (String key : headers.getKeys()) {
            EasyLog.printKeyValue(this, key, headers.get(key));
        }

        if (!headers.isEmpty() && !params.isEmpty()) {
            EasyLog.printLine(this);
        }

        for (String key : params.getKeys()) {
            Object value = params.get(key);
            if (value instanceof List) {
                // 如果这是一个 List 集合
                List<?> list = (List<?>) value;
                for (int i = 0; i < list.size(); i++) {
                    printKeyValue(key + "[" + i + "]", list.get(i));
                }
            } else if (value instanceof HashMap) {
                // 如果这是一个 Map 集合
                Map<?, ?> map = ((Map<?, ?>) value);
                for (Object itemKey : map.keySet()) {
                    if (itemKey == null) {
                        continue;
                    }
                    printKeyValue(String.valueOf(itemKey), map.get(itemKey));
                }
            } else {
                printKeyValue(key, String.valueOf(params.get(key)));
            }
        }

        if (!headers.isEmpty() || !params.isEmpty()) {
            EasyLog.printLine(this);
        }
    }
}