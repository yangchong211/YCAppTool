package com.yc.webviewlib.cache;

import android.text.TextUtils;
import android.webkit.MimeTypeMap;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2020/5/17
 *     desc  : 根据url获取文件的拓展信息工具类
 *     revise:
 * </pre>
 */
public class MimeTypeMapUtils {

    /**
     * 根据url获取文件的拓展信息
     * @param url                               url链接
     * @return
     */
    public static String getFileExtensionFromUrl(String url) {
        url = url.toLowerCase();
        if (!TextUtils.isEmpty(url)) {
            int fragment = url.lastIndexOf('#');
            if (fragment > 0) {
                url = url.substring(0, fragment);
            }
            int query = url.lastIndexOf('?');
            if (query > 0) {
                url = url.substring(0, query);
            }
            int filenamePos = url.lastIndexOf('/');
            String filename = 0 <= filenamePos ? url.substring(filenamePos + 1) : url;
            // if the filename contains special characters, we don't
            // consider it valid for our matching purposes:
            if (!filename.isEmpty()) {
                int dotPos = filename.lastIndexOf('.');
                if (0 <= dotPos) {
                    return filename.substring(dotPos + 1);
                }
            }
        }
        return "";
    }

    /**
     * 根据url获取mine类型
     * 举个例子：
     * 文件名是picture.jpg那么getMimeType()返回image/jpeg
     * 文件名是yc.gif那么getMimeType()返回image/gif
     * 文件名是yc.txt那么getMimeType()返回text/plain
     * @param url                                 url链接
     * @return
     */
    public static String getMimeTypeFromUrl(String url) {
        return  MimeTypeMap.getSingleton().getMimeTypeFromExtension(getFileExtensionFromUrl(url));
    }

    public static String getMimeTypeFromExtension(String extension) {
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }
}
