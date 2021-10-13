package com.yc.okhttp.okhttp.parser;

import android.text.TextUtils;

import com.yc.okhttp.okhttp.RetrofitUrlManager;
import com.yc.okhttp.okhttp.cache.Cache;
import com.yc.okhttp.okhttp.cache.LruCache;

import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;

/**
 * ================================================
 * 域名解析器, 此解析器用来解析域名, 默认将您的域名作为 BaseUrl, 只会将旧 URL 地址中的域名替换成你想要的地址
 * <p>
 * 比如:
 * 1.
 * 旧 URL 地址为 https://www.github.com/wiki, 您调用 {@link RetrofitUrlManager#putDomain(String, String)}
 * 方法传入的 URL 地址是 https://www.google.com/api, 经过本解析器解析后生成的新 URL 地址为 http://www.google.com/api/wiki
 * <p>
 * 2.
 * 旧 URL 地址为 https://www.github.com/wiki, 您调用 {@link RetrofitUrlManager#putDomain(String, String)}
 * 方法传入的 URL 地址是 https://www.google.com, 经过本解析器解析后生成的新 URL 地址为 http://www.google.com/wiki
 *
 * @see UrlParser
 * ================================================
 */
public class DomainUrlParser implements UrlParser {
    private Cache<String, String> mCache;

    @Override
    public void init(RetrofitUrlManager retrofitUrlManager) {
        this.mCache = new LruCache<>(100);
    }

    @Override
    public HttpUrl parseUrl(HttpUrl domainUrl, HttpUrl url) {
        // 如果 HttpUrl.parse(url); 解析为 null 说明,url 格式不正确,正确的格式为 "https://github.com:443"
        // http 默认端口 80, https 默认端口 443, 如果端口号是默认端口号就可以将 ":443" 去掉
        // 只支持 http 和 https

        if (null == domainUrl) return url;

        HttpUrl.Builder builder = url.newBuilder();

        if (TextUtils.isEmpty(mCache.get(getKey(domainUrl, url)))) {
            for (int i = 0; i < url.pathSize(); i++) {
                //当删除了上一个 index, PathSegment 的 item 会自动前进一位, 所以 remove(0) 就好
                builder.removePathSegment(0);
            }

            List<String> newPathSegments = new ArrayList<>();
            newPathSegments.addAll(domainUrl.encodedPathSegments());
            newPathSegments.addAll(url.encodedPathSegments());

            for (String PathSegment : newPathSegments) {
                builder.addEncodedPathSegment(PathSegment);
            }
        } else {
            builder.encodedPath(mCache.get(getKey(domainUrl, url)));
        }

        HttpUrl httpUrl = builder
                .scheme(domainUrl.scheme())
                .host(domainUrl.host())
                .port(domainUrl.port())
                .build();

        if (TextUtils.isEmpty(mCache.get(getKey(domainUrl, url)))) {
            mCache.put(getKey(domainUrl, url), httpUrl.encodedPath());
        }
        return httpUrl;
    }

    private String getKey(HttpUrl domainUrl, HttpUrl url) {
        return domainUrl.encodedPath() + url.encodedPath();
    }
}
