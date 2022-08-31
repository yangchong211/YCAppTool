package com.yc.appmediastore;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

import java.io.File;

public final class FileMimeType {

    public static String getMimeType(Context context, Uri uri) {
        String uriPath = AppFileUriUtils.uri2String(context, uri);
        return getMimeType(uriPath);
    }


    /**
     * 根据文件后缀名获得对应的MIME类型
     *
     * @param filePath
     * @return
     */
    public static String getMimeType(String filePath) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String mime = "*/*";
        if (filePath != null) {
            try {
                mmr.setDataSource(filePath);
                mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
            } catch (IllegalStateException e) {
                return mime;
            } catch (IllegalArgumentException e) {
                return mime;
            } catch (RuntimeException e) {
                return mime;
            }
        }
        return mime;
    }
}
