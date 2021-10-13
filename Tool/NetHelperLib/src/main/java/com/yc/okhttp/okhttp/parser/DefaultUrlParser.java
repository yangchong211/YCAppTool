package com.yc.okhttp.okhttp.parser;


import com.yc.okhttp.okhttp.RetrofitUrlManager;

import okhttp3.HttpUrl;

/**
 * ================================================
 * 默认解析器, 可根据自定义策略选择不同的解析器
 * <p>
 * 如果您觉得 {@link DefaultUrlParser} 的解析策略并不能满足您的需求, 您可以自行实现更适合您的 {@link UrlParser}
 * 然后通过 {@link RetrofitUrlManager#setUrlParser(UrlParser)} 配置给框架, 即可替换 {@link DefaultUrlParser}
 * 自己改 {@link DefaultUrlParser} 的源码来达到扩展的目的是很笨的行为
 *
 * @see UrlParser
 * ================================================
 */
public class DefaultUrlParser implements UrlParser {

    private UrlParser mDomainUrlParser;
    private volatile UrlParser mAdvancedUrlParser;
    private volatile UrlParser mSuperUrlParser;
    private RetrofitUrlManager mRetrofitUrlManager;

    @Override
    public void init(RetrofitUrlManager retrofitUrlManager) {
        this.mRetrofitUrlManager = retrofitUrlManager;
        this.mDomainUrlParser = new DomainUrlParser();
        this.mDomainUrlParser.init(retrofitUrlManager);
    }

    @Override
    public HttpUrl parseUrl(HttpUrl domainUrl, HttpUrl url) {
        if (null == domainUrl) return url;

        if (url.toString().contains(RetrofitUrlManager.IDENTIFICATION_PATH_SIZE)) {
            if (mSuperUrlParser == null) {
                synchronized (this) {
                    if (mSuperUrlParser == null) {
                        mSuperUrlParser = new SuperUrlParser();
                        mSuperUrlParser.init(mRetrofitUrlManager);
                    }
                }
            }
            return mSuperUrlParser.parseUrl(domainUrl, url);
        }

        //如果是高级模式则使用高级解析器
        if (mRetrofitUrlManager.isAdvancedModel()) {
            if (mAdvancedUrlParser == null) {
                synchronized (this) {
                    if (mAdvancedUrlParser == null) {
                        mAdvancedUrlParser = new AdvancedUrlParser();
                        mAdvancedUrlParser.init(mRetrofitUrlManager);
                    }
                }
            }
            return mAdvancedUrlParser.parseUrl(domainUrl, url);
        }
        return mDomainUrlParser.parseUrl(domainUrl, url);
    }
}
