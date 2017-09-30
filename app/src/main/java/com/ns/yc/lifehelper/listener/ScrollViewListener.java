package com.ns.yc.lifehelper.listener;

import android.widget.ScrollView;

/**
 * Created by PC on 2017/5/3.
 * ScrollView滑动监听事件接口
 */

public interface ScrollViewListener {

    /*** 在滑动的时候调用*/
    void onScrollChanged(ScrollView scrollView, int x, int y, int oldx, int oldy);
    /*** 在滑动的时候调用，scrollY为已滑动的距离*/
    void onScroll(int scrollY);

}
