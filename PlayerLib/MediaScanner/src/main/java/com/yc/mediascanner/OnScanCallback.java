package com.yc.mediascanner;

import java.util.List;

/**
 * 扫描器回调接口。
 *
 * @param <T> 媒体文件对应的实体类型
 */
public interface OnScanCallback<T> {
    /**
     * 开始扫描。
     */
    void onStartScan();

    /**
     * 更新扫描进度。
     *
     * @param progress 当前扫描进度
     * @param max      最大扫描进度
     * @param item     当前扫描到的媒体文件对应的实体对象
     */
    void onUpdateProgress(int progress, int max, T item);

    /**
     * 接收扫描。
     *
     * @param items 所有扫描到的媒体文件
     */
    void onFinished(List<T> items);
}

