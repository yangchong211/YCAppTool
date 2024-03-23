package com.yc.imageserver.progress;

import android.content.Context;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.yc.toolutils.system.AppLogUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;

import okhttp3.OkHttpClient;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/10/24
 *     desc  : glide加载进度工具
 *     revise: 自定义ImageGlideModule，需要在布局中注册
 *             参考博客：https://blog.csdn.net/guolin_blog/article/details/78357251
 *             下版本准备设置缓存，设置拦截监听等功能，第一版先不做
 * </pre>
 */
//@GlideModule
public class ImageGlideModule extends AppGlideModule {

    /*
     * AppGlideModule的实现必须使用@GlideModule注解标记。
     * 如果注解不存在，该module将不会被Glide发现。在这个注解类中，我们一般是替换Glide初始化的时候添加的默认组件。
     */

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
    public void registerComponents(@NotNull Context context, @NotNull Glide glide, @NotNull Registry registry) {
        //添加拦截器到Glide
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //添加拦截器
        builder.addInterceptor(new ProgressInterceptor());
        //build
        builder.build();
        AppLogUtils.d("加载图片进度值------registerComponents--");
        //原来的是  new OkHttpUrlLoader.Factory()；
        //在registerComponents()方法中将我们刚刚创建的OkHttpUrlLoader和OkHttpStreamFetcher注册到Glide当中，
        //将原来的HTTP通讯组件给替换掉
        OkHttpUrlLoader.Factory factory = new OkHttpUrlLoader.Factory();
        registry.replace(GlideUrl.class, InputStream.class, factory);
    }


    /**
     * 主要职责就是配置对大部分应用程序合理的默认选项
     * 设置缓存文件的路径
     * 如果有sd卡，就保存到sd卡目录下：/storage/emulated/0/cheoo/GlideDisk
     * 如果没有sd卡，就保存到手机存储目录下：data/data/cheoo/GlideDisk
     */
    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        super.applyOptions(context, builder);
        AppLogUtils.d("加载图片进度值------applyOptions--");
        //路径：
        File cacheDir = context.getCacheDir();
        StringBuilder sb = new StringBuilder();
        sb.append(cacheDir.getPath());
        sb.append(File.separator);
        sb.append("GlideDisk");
        AppLogUtils.d("加载图片进度值------file--"+sb.toString());
        builder.setDiskCache(new DiskLruCacheFactory(sb.toString(), GLIDE_CATCH_SIZE ));
    }

    /**
     * 完全禁用清单解析
     * @return
     */
    @Override
    public boolean isManifestParsingEnabled() {
        return super.isManifestParsingEnabled();
        //return false;
    }

    /**
     * 为了让Glide能够识别我们自定义的ImageGlideModule，还得在AndroidManifest.xml文件当中加入如下配置才行：
     * <meta-data
     *             android:name="com.cheoo.app.utils.glide.progress.ImageGlideModule"
     *             android:value="AppGlideModule" />
     */

}
