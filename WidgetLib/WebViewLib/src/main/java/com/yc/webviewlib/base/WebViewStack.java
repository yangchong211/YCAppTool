package com.yc.webviewlib.base;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.tencent.smtt.sdk.WebView;
import java.util.Stack;

public final class WebViewStack {

    /**
     * 记录上次出现重定向的时间.
     * 避免由于刷新造成循环重定向.
     */
    private long mLastRedirectTime = 0;
    /**
     * 记录重定向前的链接
     */
    private String mUrlBeforeRedirect;
    /**
     * 默认重定向间隔.
     * 避免由于刷新造成循环重定向.
     */
    private static final long DEFAULT_REDIRECT_INTERVAL = 3000;
    /**
     * URL栈
     */
    private final Stack<String> mUrlStack = new Stack<>();

    public Stack<String> getUrlStack() {
        return mUrlStack;
    }

    /**
     * 记录非重定向链接.
     * 并且控制相同链接链接不入栈
     *
     * @param url 链接
     */
    public void recordUrl(String url) {
        //判断当前url，是否和栈中栈顶部的url是否相同。如果不相同，则入栈操作
        if (!TextUtils.isEmpty(url) && !url.equals(getUrl())) {
            //如果重定向之前的链接不为空
            if (!TextUtils.isEmpty(mUrlBeforeRedirect)) {
                mUrlStack.push(mUrlBeforeRedirect);
                mUrlBeforeRedirect = null;
            }
        }
    }

    /**
     * 获取最后停留页面的链接.
     *
     * @return url
     */
    @Nullable
    public String getUrl() {
        //peek方法，查看此堆栈顶部的对象，而不将其从堆栈中删除。
        return mUrlStack.size() > 0 ? mUrlStack.peek() : null;
    }


    /**
     * 出栈操作
     *
     * @return
     */
    String popUrl() {
        //pop方法，移除此堆栈顶部的对象并将该对象作为此函数的值返回。
        return mUrlStack.size() > 0 ? mUrlStack.pop() : null;
    }

    /**
     * 是否可以回退操作
     *
     * @return 如果栈中数量大于2，则表示可以回退操作
     */
    public boolean pageCanGoBack() {
        return mUrlStack.size() >= 2;
    }

    /**
     * 回退操作
     *
     * @param webView webView
     * @return
     */
    public final boolean pageGoBack(@NonNull WebView webView) {
        //判断是否可以回退操作
        if (pageCanGoBack()) {
            //获取最后停留的页面url
            final String url = popBackUrl();
            //如果不为空
            if (!TextUtils.isEmpty(url)) {
                webView.loadUrl(url);
                return true;
            }
        }
        return false;
    }



    /**
     * 将最后停留的页面url弹出
     * 获取最后停留的页面url
     *
     * @return null 表示已经没有上一页了
     */
    @Nullable
    public final String popBackUrl() {
        if (mUrlStack.size() >= 2) {
            //pop current page url
            mUrlStack.pop();
            return mUrlStack.pop();
        }
        return null;
    }

    /**
     * 解决重定向
     *
     * @param view webView
     */
    public void resolveRedirect(WebView view) {
        //记录当前时间
        final long now = System.currentTimeMillis();
        //mLastRedirectTime 记录上次出现重定向的时间
        if (now - mLastRedirectTime > DEFAULT_REDIRECT_INTERVAL) {
            mLastRedirectTime = System.currentTimeMillis();
            view.reload();
        }
    }

    public void popUrlStack() {
        if (mUrlStack.size() > 0) {
            //从url栈中取出栈顶的链接
            mUrlBeforeRedirect = mUrlStack.pop();
        }
    }


}
