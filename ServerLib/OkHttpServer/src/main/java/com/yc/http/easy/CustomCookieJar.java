package com.yc.http.easy;

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
     *
     * 将cookie从这个方法加载到一个HTTP请求到指定的url。这个方法从网络上返回的结果可能是一个空集合。
     * 简单的实现将返回尚未过期的已接受的cookie去进行匹配。（说白了就是加载url的时候在请求头带上cookie）。
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
     *
     * 根据这个jar的方法，可以将cookie从一个HTTP响应保存到这里。请注意，如果响应，此方法可能被称为第二次HTTP响应，包括一个追踪。
     * 对于这个隐蔽的HTTP特性，这里的cookie只包含其追踪的cookie。
     * 简单点理解就是如果我们使用了这个方法，就会进行追踪（说白了就是客户端请求成功以后，在响应头里面去存cookie）
     * @param url               url
     * @param cookies           cookies集合
     */
    @Override
    public void saveFromResponse(@NotNull HttpUrl url, @NotNull List<Cookie> cookies) {
        cookieStore.put(url, cookies);
        //保存cookie //也可以使用SP保存
    }
}
