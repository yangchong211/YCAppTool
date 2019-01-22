package com.ycbjie.library.web.cookie;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class CookieUtils {

    private static final String token = "";

    /**
     * 其中CookieManager是cookie的管理对象，主要实现对网页cookie的注入与清除等工作。
     * 注入字符串的形式是：key=value;domain=url的形式（其中url为需要注入cookie的url链接地址）
     * 客户端将cookie种入H5页面中，H5页面可以通过js代码实现对native种入cookie信息的读取操作
     */
    public static void synCookies(Context context, String url, String cookie) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        Uri uri = null;
        String domain = "";
        try {
            uri = Uri.parse(URLDecoder.decode(url, "utf-8"));
            domain = uri.getHost();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String cookieStr = cookieManager.getCookie(url);
        // 判断token是否发生变化，发生变化的话则更新cookie
        if (!TextUtils.isEmpty(cookieStr) && cookieStr.contains(cookie) &&
                !cookie.equals(token + "=")) {
            return;
        }
        // 更新domain(不再从UserInfo中获取，更改为从UrlInfo中获取)
        if (!TextUtils.isEmpty(URLConfig.getUrlInfo())) {
            domain = URLConfig.getUrlInfo();
        }
        List<String> uaList = new ArrayList<>();
        uaList.add("yc");
        String md = ";domain=" + domain;
        // 添加信息到Cookie中
        cookieManager.setCookie(url, cookie + ";" + md);
        if (uaList.size() > 0) {
            for (String coo : uaList) {
                cookieManager.setCookie(url, coo + md);
            }
        }
        CookieSyncManager.getInstance().sync();
    }

    /**
     * 移除cookie
     */
    public static void removeCookies(Context context) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeSessionCookie();
        CookieSyncManager.getInstance().sync();
    }

    private static class URLConfig{
        static String getUrlInfo() {
            return null;
        }
    }

}
