package com.yc.http.easy;

import android.content.Context;

import java.util.HashMap;


public final class RequestConfig {

    /**
     * 请求接口根地址
     */
    private String baseUrl = "";
    /**
     * 上下文
     */
    private Context context;
    /**
     * 通用参数
     */
    private HashMap<String, Object> params;
    /**
     * 通用请求头
     */
    private HashMap<String, String> headers;

    private RequestConfig() {
        params = new HashMap<>();
        headers = new HashMap<>();
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public Context getContext() {
        return context;
    }

    public HashMap<String, Object> getParams() {
        return params;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final RequestConfig config;

        private Builder() {
            config = new RequestConfig();
        }

        public Builder setBaseUrl(String baseUrl) {
            config.baseUrl = baseUrl;
            return this;
        }

        public Builder setContext(Context context) {
            config.context = context;
            return this;
        }


        public Builder setParams(HashMap<String, Object> params) {
            if (params == null) {
                params = new HashMap<>();
            }
            config.params = params;
            return this;
        }

        public Builder setHeaders(HashMap<String, String> headers) {
            if (headers == null) {
                headers = new HashMap<>();
            }
            config.headers = headers;
            return this;
        }

        public Builder addHeader(String key, String value) {
            if (key != null && value != null) {
                config.headers.put(key, value);
            }
            return this;
        }

        public Builder removeHeader(String key) {
            if (key != null) {
                config.headers.remove(key);
            }
            return this;
        }

        public Builder addParam(String key, String value) {
            if (key != null && value != null) {
                config.params.put(key, value);
            }
            return this;
        }

        public Builder removeParam(String key) {
            if (key != null) {
                config.params.remove(key);
            }
            return this;
        }

        public RequestConfig build() {
            return config;
        }
    }

}
