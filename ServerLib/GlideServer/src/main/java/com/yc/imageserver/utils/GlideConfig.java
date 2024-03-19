package com.yc.imageserver.utils;

import android.content.Context;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.AppGlideModule;
import com.yc.toolutils.context.AppToolUtils;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/9
 *     desc  : 自定义glide缓存控制器
 *     revise: 暂时在调试阶段，先不要用到实际开发中
 * </pre>
 */
public class GlideConfig extends AppGlideModule {


    /**
     * 图片缓存最大容量，500M，根据自己的需求进行修改
     */
    public static final int GLIDE_CATCH_SIZE = 500 * 1000 * 1000;
    /**
     * 图片缓存子目录
     */
    public static final String GLIDE_CATCH_DIR = "image_catch";

    /**
     * 在创建Glide单例之后，但在创建任何单例之前，立即惰性地注册组件可以启动请求。
     * 此方法将在每个实现中调用一次，且仅调用一次
     * @param context                           上下文
     * @param glide                             glide
     * @param registry                          registry
     */
    @Override
    public void registerComponents(@NonNull Context context,
                                   @NonNull Glide glide, @NonNull Registry registry) {

    }

    /**
     * 设置缓存文件的路径
     * 如果有sd卡，就保存到sd卡目录下：/storage/emulated/0/cheoo/GlideDisk
     * 如果没有sd卡，就保存到手机存储目录下：data/data/cheoo/GlideDisk
     */
    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        super.applyOptions(context, builder);
        //路径：
        builder.setDiskCache(new DiskLruCacheFactory(GlideSaveUtils.getLocalFileSavePathDir(
                AppToolUtils.getApp(),"GlideDisk",null), GLIDE_CATCH_SIZE ));
    }

}
