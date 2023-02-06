package com.yc.m3u8.inter;

import com.yc.m3u8.bean.M3u8;

/**
 * @deprecated v2版本过时了，请用
 * 监听器
 * Created by HDL on 2017/7/25.
 */

public abstract class M3U8Listener implements DownLoadListener {
    public void onM3U8Info(M3u8 m3U8) {
    }

    public void onDownloadingProgress(int total, int progress) {
    }

    /**
     * 当获取到单个文件大小的时候回调
     *
     * @param fileSize 单个文件大小
     */
    public void onLoadFileSizeForItem(long fileSize) {
    }

}
