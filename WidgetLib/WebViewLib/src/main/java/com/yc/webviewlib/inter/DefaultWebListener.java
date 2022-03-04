package com.yc.webviewlib.inter;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/10
 *     desc  : web的接口回调接口空实现
 *     revise: demo地址：https://github.com/yangchong211/YCWebView
 * </pre>
 */
public class DefaultWebListener implements InterWebListener{

    /**
     * 隐藏进度条
     * 这个方法会执行多次，是2次。一次是onPageFinished调用，一次是onProgressChanged进度达到85会调用
     * 主要用于隐藏加载进度条或者隐藏加载loading
     */
    @Override
    public void hindProgressBar() {

    }

    /**
     * 展示异常页面
     * @param type                           异常类型
     */
    @Override
    public void showErrorView(int type) {

    }

    /**
     * 进度条变化时调用，这里添加注解限定符，必须是在0到100之间
     * @param newProgress                   进度0-100
     */
    @Override
    public void startProgress(int newProgress) {

    }

    /**
     * 获取加载网页的标题
     * @param title                         title标题
     */
    @Override
    public void showTitle(String title) {

    }

    /**
     * 加载完成
     * 这里可以注入外部js
     * @param url                           连接
     */
    @Override
    public void onPageFinished(String url) {

    }


}
