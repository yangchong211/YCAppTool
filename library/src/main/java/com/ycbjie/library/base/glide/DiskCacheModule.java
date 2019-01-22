package com.ycbjie.library.base.glide;

import android.content.Context;
import android.support.annotation.NonNull;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.AppGlideModule;
import com.ycbjie.library.utils.SDUtils;


@GlideModule
public class DiskCacheModule extends AppGlideModule {

    private static final int diskCacheSizeBytes = 1024 * 1024 * 500; // 200 MB

    @Override
    public boolean isManifestParsingEnabled() {
        return super.isManifestParsingEnabled();
    }

    /**
     * 设置缓存文件的路径
     * 如果有sd卡，就保存到sd卡目录下：/storage/emulated/0/lifeHelper/GlideDisk
     * 如果没有sd卡，就保存到手机存储目录下：data/data/lifeHelper/GlideDisk
     */
    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        super.applyOptions(context, builder);
        //路径：
        builder.setDiskCache(new DiskLruCacheFactory(SDUtils.getLocalRootSavePathDir(
                Utils.getApp(),"GlideDisk"), diskCacheSizeBytes ));
    }



}
