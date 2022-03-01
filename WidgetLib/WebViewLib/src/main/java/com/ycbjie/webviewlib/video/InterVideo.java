package com.ycbjie.webviewlib.video;



import android.view.View;

import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/10
 *     desc  : 全频播放视频接口
 *     revise:
 * </pre>
 */
public interface InterVideo {

    /**
     * 通知应用当前页进入了全屏模式，此时应用必须显示一个包含网页内容的自定义View
     * 播放网络视频时全屏会被调用的方法，播放视频切换为横屏
     * @param view                              view
     * @param callback                          callback
     */
    void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback callback);

    /**
     * 通知应用当前页退出了全屏模式，此时应用必须隐藏之前显示的自定义View
     * 视频播放退出全屏会被调用的
     */
    void onHideCustomView();

    /**
     * 当全屏的视频正在缓冲时，此方法返回一个占位视图(比如旋转的菊花)。
     * 视频加载时进程loading
     * @return                                  view
     */
    View getVideoLoadingProgressView();

    /**
     * 视频切换状态
     * @return                                  布尔值
     */
    boolean isVideoState();

}
