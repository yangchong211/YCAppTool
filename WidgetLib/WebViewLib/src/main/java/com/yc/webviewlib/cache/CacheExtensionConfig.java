package com.yc.webviewlib.cache;

import android.text.TextUtils;

import java.util.HashSet;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2020/5/17
 *     desc  : 缓存配置，定义为final不可修改
 *     revise:
 * </pre>
 */
public final class CacheExtensionConfig {

    //单独webView实例的
    private final HashSet<String> statics = new HashSet<>(STATIC);
    private final HashSet<String> no_cache = new HashSet<>(NO_CACH);

    /**
     * 全局默认的
     */
    private static final HashSet<String> STATIC = new HashSet<String>() {
        {
            add("html");
            add("htm");
            add("js");
            add("ico");
            add("css");
            add("png");
            add("jpg");
            add("jpeg");
            add("gif");
            add("bmp");
            add("ttf");
            add("woff");
            add("woff2");
            add("otf");
            add("eot");
            add("svg");
            add("xml");
            add("swf");
            add("txt");
            add("text");
            add("conf");
            add("webp");
        }
    };

    /**
     * 下面这些是不用进行缓存的
     */
    private static final HashSet<String> NO_CACH = new HashSet<String>() {
        {
            add("mp4");
            add("mp3");
            add("ogg");
            add("avi");
            add("wmv");
            add("flv");
            add("rmvb");
            add("3gp");
        }
    };



    public static void addGlobalExtension(String extension) {
        add(STATIC, extension);
    }

    public static void removeGlobalExtension(String extension) {
        remove(STATIC, extension);
    }


    private static void add(HashSet<String> set, String extension) {
        if (TextUtils.isEmpty(extension)) {
            return;
        }
        set.add(extension.replace(".", "").toLowerCase().trim());
    }

    private static void remove(HashSet set, String extension) {
        if (TextUtils.isEmpty(extension)) {
            return;
        }
        set.remove(extension.replace(".", "").toLowerCase().trim());
    }

    /**
     * 是否是音视频内容，需要过滤
     * @param extension                             extension
     * @return
     */
    public boolean isMedia(String extension) {
        if (TextUtils.isEmpty(extension)) {
            return false;
        }
        if (NO_CACH.contains(extension)) {
            return true;
        }
        return no_cache.contains(extension.toLowerCase().trim());
    }

    /**
     * 是否可以缓存
     * @param extension                             extension
     * @return
     */
    public boolean canCache(String extension) {
        if (TextUtils.isEmpty(extension)) {
            return false;
        }
        extension = extension.toLowerCase().trim();
        if (STATIC.contains(extension)) {
            return true;
        }
        return statics.contains(extension);

    }


    public CacheExtensionConfig addExtension(String extension) {
        add(statics, extension);
        return this;
    }

    public CacheExtensionConfig removeExtension(String extension) {
        remove(statics, extension);
        return this;
    }


    public boolean isHtml(String extension) {
        if (TextUtils.isEmpty(extension)) {
            return false;
        }
        if (extension.toLowerCase().contains("html") || extension.toLowerCase().contains("htm")) {
            return true;
        }
        return false;
    }

    public void clearAll() {
        clearDiskExtension();
    }

    public void clearDiskExtension() {
        statics.clear();
    }

}
