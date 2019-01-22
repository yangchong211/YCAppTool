package com.ycbjie.library.web.js;

import android.content.Context;

import com.ycbjie.library.base.mvp.BaseActivity;
import com.ns.yc.ycutilslib.activityManager.AppManager;
import com.tencent.smtt.sdk.WebView;

/**
 * <pre>
 *     @author yangchong
 *     blog  :
 *     time  : 2018/05/30
 *     desc  : 所有js工具类
 *     revise:
 * </pre>
 */
public class JsUtils extends BaseAppToJsInterface {


    public JsUtils(Context context, WebView webView) {
        super(context, webView);
    }


    /**
     * 关闭当前activity
     */
    public void closeWindow(String callbackId) {
        BaseActivity activity = (BaseActivity) AppManager.getAppManager().currentActivity();
        if (activity != null) {
            activity.finish();
        }
    }


    /**
     * 返回上一页，如果是最后一页则关闭当前页面
     * @param callbackId
     */
    public void goBack(String callbackId){
        if(mWebView != null && mContext instanceof BaseActivity){
            if(mWebView.canGoBack()){
                mWebView.goBack();
            }else{
                ((BaseActivity)mContext).finish();
            }
        }else{
            if (mContext instanceof BaseActivity){
                ((BaseActivity) mContext).finish();
            }
        }
    }


}
