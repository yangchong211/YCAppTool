package com.yc.netlib.data;

import java.io.Serializable;
import java.util.Map;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2019/07/22
 *     desc  : 网络请求bean
 *     revise:
 * </pre>
 */
public class NetworkFeedBean implements Serializable {


    private String mRequestId;
    private String mUrl;
    private String mHost;
    private String mMethod;

    private Map<String, String> mRequestHeadersMap;

    private String mName;
    private int mStatus;
    private int mSize;
    private long mCostTime;
    private String mContentType;
    private String mBody;
    private Map<String, String> mResponseHeadersMap;

    private long mCreateTime;

    private String mCURL;
    private int connectTimeoutMillis;
    private int readTimeoutMillis;
    private int writeTimeoutMillis;

    public String getRequestId() {
        return mRequestId;
    }

    public void setRequestId(String requestId) {
        mRequestId = requestId;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getHost() {
        return mHost;
    }

    public void setHost(String host) {
        mHost = host;
    }

    public String getMethod() {
        return mMethod;
    }

    public void setMethod(String method) {
        mMethod = method;
    }

    public Map<String, String> getRequestHeadersMap() {
        return mRequestHeadersMap;
    }

    public void setRequestHeadersMap(Map<String, String> requestHeadersMap) {
        mRequestHeadersMap = requestHeadersMap;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public int getSize() {
        return mSize;
    }

    public void setSize(int size) {
        mSize = size;
    }

    public long getCostTime() {
        return mCostTime;
    }

    public void setCostTime(long costTime) {
        mCostTime = costTime;
    }

    public String getContentType() {
        return mContentType;
    }

    public void setContentType(String contentType) {
        mContentType = contentType;
    }

    public String getBody() {
        return mBody;
    }

    public void setBody(String body) {
        mBody = body;
    }

    public Map<String, String> getResponseHeadersMap() {
        return mResponseHeadersMap;
    }

    public void setResponseHeadersMap(Map<String, String> responseHeadersMap) {
        mResponseHeadersMap = responseHeadersMap;
    }

    public long getCreateTime() {
        return mCreateTime;
    }

    public void setCreateTime(long createTime) {
        mCreateTime = createTime;
    }

    public String getCURL() {
        return mCURL;
    }

    public void setCURL(String CURL) {
        mCURL = CURL;
    }

    public int getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    public void setConnectTimeoutMillis(int connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
    }

    public int getReadTimeoutMillis() {
        return readTimeoutMillis;
    }

    public void setReadTimeoutMillis(int readTimeoutMillis) {
        this.readTimeoutMillis = readTimeoutMillis;
    }

    public int getWriteTimeoutMillis() {
        return writeTimeoutMillis;
    }

    public void setWriteTimeoutMillis(int writeTimeoutMillis) {
        this.writeTimeoutMillis = writeTimeoutMillis;
    }

    @Override
    public String toString() {
        return "NetworkFeedBean{" +
                "mRequestId='" + mRequestId + '\'' +
                ", mUrl='" + mUrl + '\'' +
                ", mHost='" + mHost + '\'' +
                ", mMethod='" + mMethod + '\'' +
                ", mRequestHeadersMap=" + mRequestHeadersMap +
                ", mName='" + mName + '\'' +
                ", mStatus=" + mStatus +
                ", mSize=" + mSize +
                ", mCostTime=" + mCostTime +
                ", mContentType='" + mContentType + '\'' +
                ", mBody='" + mBody + '\'' +
                ", mResponseHeadersMap=" + mResponseHeadersMap +
                ", mCreateTime=" + mCreateTime +
                ", mCURL='" + mCURL + '\'' +
                '}';
    }
}
