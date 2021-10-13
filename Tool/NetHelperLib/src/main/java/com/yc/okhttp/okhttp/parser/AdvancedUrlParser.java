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
 * 高级解析器, 当 BaseUrl 中有除了域名以外的其他 Path 时, 可使用此解析器
 * <p>
 * 比如:
 * 1.
 * 旧 URL 地址为 https://www.github.com/wiki/part, 您在 App 初始化时传入 {@link RetrofitUrlManager#startAdvancedModel(String)}
 * 的 BaseUrl 为 https://www.github.com/wiki
 * 您调用 {@link RetrofitUrlManager#putDomain(String, String)} 方法传入的 URL 地址是 https://www.google.com/api
 * 经过本解析器解析后生成的新 URL 地址为 http://www.google.com/api/part
 * <p>
 * 2.
 * 旧 URL 地址为 https://www.github.com/wiki/part, 您在 App 初始化时传入 {@link RetrofitUrlManager#startAdvancedModel(String)}
 * 的 BaseUrl 为 https://www.github.com/wiki
 * 您调用 {@link RetrofitUrlManager#putDomain(String, String)} 方法传入的 URL 地址是 https://www.google.com
 * 经过本解析器解析后生成的新 URL 地址为 http://www.google.com/part
 * <p>
 * 3.
 * 旧 URL 地址为 https://www.github.com/wiki/part, 您在 App 初始化时传入 {@link RetrofitUrlManager#startAdvancedModel(String)}
 * 的 BaseUrl 为 https://www.github.com
 * 您调用 {@link RetrofitUrlManager#putDomain(String, String)} 方法传入的 URL 地址是 https://www.google.com/api
 * 经过本解析器解析后生成的新 URL 地址为 http://www.google.com/api/wiki/part
 * <p>
 * 解析器会将 BaseUrl 全部替换成您传入的 Url 地址
 *
 * @see UrlParser
 * ================================================
 */
public class AdvancedUrlParser implements UrlParser {

    private RetrofitUrlManager mRetrofitUrlManager;
    private Cache<String, String> mCache;

    @Override
    public void init(RetrofitUrlManager retrofitUrlManager) {
        this.mRetrofitUrlManager = retrofitUrlManager;
        this.mCache = new LruCache<>(100);
    }

    @Override
    public HttpUrl parseUrl(HttpUrl domainUrl, HttpUrl url) {
        if (null == domainUrl) return url;

        HttpUrl.Builder builder = url.newBuilder();

        if (TextUtils.isEmpty(mCache.get(getKey(domainUrl, url)))) {
            for (int i = 0; i < url.pathSize(); i++) {
                //当删除了上一个 index, PathSegment 的 item 会自动前进一位, 所以 remove(0) 就好
                builder.removePathSegment(0);
            }

            List<String> newPathSegments = new ArrayList<>();
            newPathSegments.addAll(domainUrl.encodedPathSegments());

            if (url.pathSize() > mRetrofitUrlManager.getPathSize()) {
                List<String> encodedPathSegments = url.encodedPathSegments();
                for (int i = mRetrofitUrlManager.getPathSize(); i < encodedPathSegments.size(); i++) {
                    newPathSegments.add(encodedPathSegments.get(i));
                }
            } else if (url.pathSize() < mRetrofitUrlManager.getPathSize()) {
                throw new IllegalArgumentException(String.format("Your final path is %s, but the baseUrl of your RetrofitUrlManager#startAdvancedModel is %s",
                        url.scheme() + "://" + url.host() + url.encodedPath(),
                        mRetrofitUrlManager.getBaseUrl().scheme() + "://"
                                + mRetrofitUrlManager.getBaseUrl().host()
                                + mRetrofitUrlManager.getBaseUrl().encodedPath()));
            }

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
        return domainUrl.encodedPath() + url.encodedPath() + mRetrofitUrlManager.getPathSize();
    }
}
