package com.yc.netlib.data;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * @desc: 一条网络请求记录
 */
public class NetworkRecord implements Serializable {

    private static final String METHOD_GET = "get";
    private static final String METHOD_POST = "post";
    private String url;
    private String method;
    private String requestId;
    private long requestLength;
    private long responseLength;
    private long startTime;
    private long endTime;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public long getRequestLength() {
        return requestLength;
    }

    public void setRequestLength(long requestLength) {
        this.requestLength = requestLength;
    }

    public long getResponseLength() {
        return responseLength;
    }

    public void setResponseLength(long responseLength) {
        this.responseLength = responseLength;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public boolean filter(String text) {
        // 目前只支持url筛选，后续需要再扩展
        if (getUrl() != null && !TextUtils.isEmpty(url) && url.contains(text)) {
            return true;
        }
        return false;
    }

    public boolean isGetRecord() {
        return getMethod() != null && TextUtils.equals(METHOD_GET, getMethod().toLowerCase());
    }

    public boolean isPostRecord() {
        return getMethod() != null && TextUtils.equals(METHOD_POST, getMethod().toLowerCase());
    }
}
