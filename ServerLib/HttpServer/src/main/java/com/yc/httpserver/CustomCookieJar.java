package com.yc.httpserver;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     GitHub : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 解决cookie失效问题
 *     revise:
 * </pre>
 */
public class CustomCookieJar implements CookieJar {

    /**
     * Cookie我们可以简单的理解为存储的为键值对：key = value
     * 建立一个HashMap作为存储url为key，对应的url服务器返回给我们的cookie数组
     */
    private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

    /**
     * 根据url这个key来从cookieStore中获取去对应的所有的Cookie
     * @param url               url
     * @return
     */
    @NotNull
    @Override
    public List<Cookie> loadForRequest(@NotNull HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url);
        //取出cookie
        return cookies != null ? cookies : new ArrayList<Cookie>();
    }

    /**
     * 用来获取服务器端返回的Response中返回的Cookies(可能是很多个)，存储在cookieStore这个变量中
     * @param url               url
     * @param cookies           cookies集合
     */
    @Override
    public void saveFromResponse(@NotNull HttpUrl url, @NotNull List<Cookie> cookies) {
        cookieStore.put(url, cookies);
        //保存cookie //也可以使用SP保存
    }
}
