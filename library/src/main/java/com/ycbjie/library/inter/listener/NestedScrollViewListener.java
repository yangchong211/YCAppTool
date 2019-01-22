package com.ycbjie.library.inter.listener;

import android.support.v4.widget.NestedScrollView;

/**
 * Created by PC on 2017/5/3.
 * ScrollView滑动监听事件接口
 */

public interface NestedScrollViewListener {

    /*** 在滑动的时候调用*/
    void onScrollChanged(NestedScrollView scrollView, int x, int y, int oldx, int oldy);
    /*** 在滑动的时候调用，scrollY为已滑动的距离*/
    void onScroll(int scrollY);

}
