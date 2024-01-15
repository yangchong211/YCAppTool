package com.yc.mediascanner;

import android.content.ContentResolver;

import androidx.annotation.NonNull;

/**
 * 扫描器。
 *
 * @param <T> 媒体文件对应的实体类型
 * @see MediaStoreHelper#scanAudio(ContentResolver, MediaStoreHelper.Decoder)
 * @see MediaStoreHelper#scanVideo(ContentResolver, MediaStoreHelper.Decoder)
 * @see MediaStoreHelper#scanImages(ContentResolver, MediaStoreHelper.Decoder)
 */
public interface Scanner<T> {

    int MIN_UPDATE_THRESHOLD = 200;

    /**
     * 设置 ContentResolver.query 方法的 projection 部分参数。
     */
    Scanner<T> projection(String[] projection);

    /**
     * 设置 ContentResolver.query 方法的 selection 部分参数。
     */
    Scanner<T> selection(String selection);

    /**
     * 设置 ContentResolver.query 方法的 selectionArgs 部分参数。
     */
    Scanner<T> selectionArgs(String[] args);

    /**
     * 设置 ContentResolver.query 方法的 sortOrder 部分参数。
     */
    Scanner<T> sortOrder(String sortOrder);

    /**
     * 设置更新 UI 刷新的阈值时间（单位：毫秒），避免 UI 刷新速度跟不上数据流的速度。如果两个数据的发送时间
     * 间隔小于 threshold 值，本次 UI 刷新将被忽略。
     *
     * @param threshold UI 刷新的阈值，不能小于 {@link #MIN_UPDATE_THRESHOLD}
     */
    Scanner<T> updateThreshold(int threshold);

    /**
     * 取消扫描。
     */
    void cancel();

    /**
     * 开始扫描。
     *
     * @param callback 回调接口，不能为 null
     */
    void scan(@NonNull OnScanCallback<T> callback);
}
