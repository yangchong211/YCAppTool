package com.yc.compress.image;

import android.text.TextUtils;

/**
 * @author yangchong
 * time  : 2018/11/9
 * GitHub : https://github.com/yangchong211/YCCommonLib
 * desc  : 图片类型工具类
 * revise:
 */
public final class PicMimeTypeUtils {

    public final static String JPEG = ".jpeg";
    public final static String JPG = ".jpg";
    public final static String PNG = ".png";
    public final static String WEBP = ".webp";
    public final static String GIF = ".gif";
    public final static String BMP = ".bmp";
    public final static String MIME_TYPE_PREFIX_IMAGE = "image";
    private final static String MIME_TYPE_PNG = "image/png";
    public final static String MIME_TYPE_JPEG = "image/jpeg";
    private final static String MIME_TYPE_JPG = "image/jpg";
    private final static String MIME_TYPE_BMP = "image/bmp";
    private final static String MIME_TYPE_XMS_BMP = "image/x-ms-bmp";
    private final static String MIME_TYPE_WAP_BMP = "image/vnd.wap.wbmp";
    private final static String MIME_TYPE_GIF = "image/gif";
    private final static String MIME_TYPE_GIF2 = "image/GIF";
    private final static String MIME_TYPE_WEBP = "image/webp";

    /**
     * isGif
     *
     * @param mimeType
     * @return
     */
    public static boolean isHasGif(String mimeType) {
        return mimeType != null && (mimeType.equals(MIME_TYPE_GIF) || mimeType.equals(MIME_TYPE_GIF2));
    }

    /**
     * isGif
     *
     * @param url
     * @return
     */
    public static boolean isUrlHasGif(String url) {
        return url.toLowerCase().endsWith(GIF);
    }

    /**
     * is has image
     *
     * @param url
     * @return
     */
    public static boolean isUrlHasImage(String url) {
        return url.toLowerCase().endsWith(JPG)
                || url.toLowerCase().endsWith(JPEG)
                || url.toLowerCase().endsWith(PNG)
                || url.toLowerCase().endsWith(BMP);
    }

    /**
     * isWebp
     *
     * @param mimeType
     * @return
     */
    public static boolean isHasWebp(String mimeType) {
        return mimeType != null && mimeType.equalsIgnoreCase(MIME_TYPE_WEBP);
    }

    /**
     * isWebp
     *
     * @param url
     * @return
     */
    public static boolean isUrlHasWebp(String url) {
        return url.toLowerCase().endsWith(WEBP);
    }

    /**
     * isImage
     *
     * @param mimeType
     * @return
     */
    public static boolean isHasImage(String mimeType) {
        return mimeType != null && mimeType.startsWith(MIME_TYPE_PREFIX_IMAGE);
    }

    /**
     * isHasBmp
     *
     * @param mimeType
     * @return
     */
    public static boolean isHasBmp(String mimeType) {
        if (TextUtils.isEmpty(mimeType)) {
            return false;
        }
        return mimeType.startsWith(PicMimeTypeUtils.ofBMP())
                || mimeType.startsWith(PicMimeTypeUtils.ofXmsBMP())
                || mimeType.startsWith(PicMimeTypeUtils.ofWapBMP());
    }

    /**
     * Determine if it is JPG.
     *
     * @param mimeType image file mimeType
     */
    public static boolean isJPEG(String mimeType) {
        if (TextUtils.isEmpty(mimeType)) {
            return false;
        }
        return mimeType.startsWith(MIME_TYPE_JPEG) || mimeType.startsWith(MIME_TYPE_JPG);
    }

    /**
     * Determine if it is JPG.
     *
     * @param mimeType image file mimeType
     */
    public static boolean isJPG(String mimeType) {
        if (TextUtils.isEmpty(mimeType)) {
            return false;
        }
        return mimeType.startsWith(MIME_TYPE_JPG);
    }


    /**
     * is Network image
     *
     * @param path
     * @return
     */
    public static boolean isHasHttp(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        return path.startsWith("http") || path.startsWith("https");
    }


    /**
     * Get image suffix
     *
     * @param mineType
     * @return
     */
    public static String getLastImgSuffix(String mineType) {
        try {
            return mineType.substring(mineType.lastIndexOf("/")).replace("/", ".");
        } catch (Exception e) {
            e.printStackTrace();
            return JPG;
        }
    }

    /**
     * Get url to file name
     *
     * @param path
     * @return
     */
    public static String getUrlToFileName(String path) {
        String result = "";
        try {
            int lastIndexOf = path.lastIndexOf("/");
            if (lastIndexOf != -1) {
                result = path.substring(lastIndexOf + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * is content://
     *
     * @param url
     * @return
     */
    public static boolean isContent(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        return url.startsWith("content://");
    }


    public static String ofPNG() {
        return MIME_TYPE_PNG;
    }

    public static String ofJPEG() {
        return MIME_TYPE_JPEG;
    }

    public static String ofBMP() {
        return MIME_TYPE_BMP;
    }

    public static String ofXmsBMP() {
        return MIME_TYPE_XMS_BMP;
    }

    public static String ofWapBMP() {
        return MIME_TYPE_WAP_BMP;
    }

    public static String ofGIF() {
        return MIME_TYPE_GIF;
    }

    public static String ofWEBP() {
        return MIME_TYPE_WEBP;
    }


}
