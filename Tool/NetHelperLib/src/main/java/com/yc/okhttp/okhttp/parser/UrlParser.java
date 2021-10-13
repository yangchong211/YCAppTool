package com.yc.okhttp.okhttp.parser;

import com.yc.okhttp.okhttp.RetrofitUrlManager;

import okhttp3.HttpUrl;

/**
 * Url解析器
 */

public interface UrlParser {

    /**
     * 这里可以做一些初始化操作
     *
     * @param retrofitUrlManager {@link RetrofitUrlManager}
     */
    void init(RetrofitUrlManager retrofitUrlManager);

    /**
     * 将映射的 URL 解析成完整的{@link HttpUrl}
     *
     * @param domainUrl 用于替换的 URL 地址
     * @param url       旧 URL 地址
     * @return
     */
    HttpUrl parseUrl(HttpUrl domainUrl, HttpUrl url);
}
