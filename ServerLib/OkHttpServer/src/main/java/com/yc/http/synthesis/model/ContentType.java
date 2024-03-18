package com.yc.http.synthesis.model;

import android.text.TextUtils;

import java.net.FileNameMap;
import java.net.URLConnection;

import okhttp3.MediaType;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2021/03/31
 *    desc   : RequestBody 包装类（用于获取上传进度）
 */
public final class ContentType {

    /** 字节流 */
    public static final MediaType STREAM = MediaType.parse("application/octet-stream");

    /** Json */
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /** 纯文本 */
    public static final MediaType TEXT = MediaType.parse("text/plain; charset=utf-8");

    /**
     * 根据文件名获取 MIME 类型
     */
    public static MediaType guessMimeType(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return STREAM;
        }
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        // 解决文件名中含有#号异常的问题
        fileName = fileName.replace("#", "");
        String contentType = fileNameMap.getContentTypeFor(fileName);
        if (contentType == null) {
            return STREAM;
        }
        MediaType type = MediaType.parse(contentType);
        if (type == null) {
            type = STREAM;
        }
        return type;
    }
}