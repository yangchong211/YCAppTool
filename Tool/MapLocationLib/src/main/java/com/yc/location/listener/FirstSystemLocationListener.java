package com.yc.location.listener;

import android.location.Location;

/**
 * 监听获得到的第一个系统层的定位（如gps、flp、nlp等定位），为了及时通知获得到的第一个位置。
 */

public interface FirstSystemLocationListener {
    /**
     * 回调通知获得到的第一个位置
     * @param location
     */
    void onFirstLocation(Location location);
}
