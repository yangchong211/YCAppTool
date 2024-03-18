package com.yc.http.synthesis.model;

import com.yc.http.synthesis.EasyConfig;

import java.util.HashMap;
import java.util.Set;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2019/07/20
 *    desc   : 请求头封装
 */
public final class HttpHeaders {

    /** 请求头存放集合 */
    private final HashMap<String, String> mHeaders = new HashMap<>(EasyConfig.getInstance().getHeaders());

    public void put(String key, String value) {
        if (key == null || value == null) {
            return;
        }
        mHeaders.put(key, value);
    }

    public void remove(String key) {
        if (key == null) {
            return;
        }
        mHeaders.remove(key);
    }

    public String get(String key) {
        return mHeaders.get(key);
    }

    public void clear() {
        mHeaders.clear();
    }

    public boolean isEmpty() {
        return mHeaders == null || mHeaders.isEmpty();
    }

    public Set<String> getKeys() {
        return mHeaders.keySet();
    }

    public HashMap<String, String> getHeaders() {
        return mHeaders;
    }
}