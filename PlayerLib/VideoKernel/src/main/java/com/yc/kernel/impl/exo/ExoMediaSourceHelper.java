package com.yc.kernel.impl.exo;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultLoadErrorHandlingPolicy;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : exo视频播放器帮助类，主要是设置媒体来源
 *     revise:
 * </pre>
 */
public final class ExoMediaSourceHelper {

    private static ExoMediaSourceHelper sInstance;
    private final String mUserAgent;
    private final Context mAppContext;
    private HttpDataSource.Factory mHttpDataSourceFactory;
    private Cache mCache;

    private ExoMediaSourceHelper(Context context) {
        if (context instanceof Application) {
            mAppContext = context;
        } else {
            mAppContext = context.getApplicationContext();
        }
        mUserAgent = Util.getUserAgent(mAppContext, mAppContext.getApplicationInfo().name);
    }

    public static ExoMediaSourceHelper getInstance(Context context) {
        if (sInstance == null) {
            synchronized (ExoMediaSourceHelper.class) {
                if (sInstance == null) {
                    sInstance = new ExoMediaSourceHelper(context);
                }
            }
        }
        return sInstance;
    }

    public MediaSource getMediaSource(String uri) {
        return getMediaSource(uri, null, false);
    }

    public MediaSource getMediaSource(String uri, Map<String, String> headers) {
        return getMediaSource(uri, headers, false);
    }

    public MediaSource getMediaSource(String uri, Map<String, String> headers, boolean isCache) {
        Uri contentUri = Uri.parse(uri);
        if ("rtmp".equals(contentUri.getScheme())) {
            RtmpDataSourceFactory rtmpDataSourceFactory = new RtmpDataSourceFactory(null);
            return new ProgressiveMediaSource.Factory(rtmpDataSourceFactory).createMediaSource(contentUri);
        }
        int contentType = inferContentType(uri);
        DataSource.Factory factory;
        if (isCache) {
            factory = getCacheDataSourceFactory();
        } else {
            factory = getDataSourceFactory();
        }
        if (mHttpDataSourceFactory != null) {
            setHeaders(headers);
        }
        //设置媒体来源
        //DefaultMediaSourceFactory还可以根据相应媒体项的属性创建更复杂的媒体源。
        //DashMediaSource对于DASH。
        //SsMediaSource对于平滑流。
        //HlsMediaSource对于HLS。
        //ProgressiveMediaSource对于常规媒体文件。
        //RtspMediaSource对于RTSP。

        //自定义媒体源创建
        switch (contentType) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(factory)
                        .setLoadErrorHandlingPolicy(mLoadErrorHandlingPolicy)
                        .createMediaSource(contentUri);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(factory)
                        .setLoadErrorHandlingPolicy(mLoadErrorHandlingPolicy)
                        .createMediaSource(contentUri);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(factory)
                        .setLoadErrorHandlingPolicy(mLoadErrorHandlingPolicy)
                        .createMediaSource(contentUri);
            default:
            case C.TYPE_OTHER:
                return new ProgressiveMediaSource.Factory(factory)
                        .setLoadErrorHandlingPolicy(mLoadErrorHandlingPolicy)
                        .createMediaSource(contentUri);
        }
    }

    private int inferContentType(String fileName) {
        fileName = Util.toLowerInvariant(fileName);
        if (fileName.contains(".mpd")) {
            return C.TYPE_DASH;
        } else if (fileName.contains(".m3u8")) {
            return C.TYPE_HLS;
        } else if (fileName.matches(".*\\.ism(l)?(/manifest(\\(.+\\))?)?")) {
            return C.TYPE_SS;
        } else {
            return C.TYPE_OTHER;
        }
    }

    private DataSource.Factory getCacheDataSourceFactory() {
        if (mCache == null) {
            mCache = newCache();
        }
        return new CacheDataSourceFactory(
                mCache,
                getDataSourceFactory(),
                CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);
    }

    private Cache newCache() {
        return new SimpleCache(
                //缓存目录
                new File(mAppContext.getExternalCacheDir(), "exo-video-cache"),
                //缓存大小，默认512M，使用LRU算法实现
                new LeastRecentlyUsedCacheEvictor(512 * 1024 * 1024),
                new ExoDatabaseProvider(mAppContext));
    }

    /**
     * 返回一个新的数据源工厂
     *
     * @return DefaultDataSourceFactory
     */
    private DataSource.Factory getDataSourceFactory() {
        DataSource.Factory httpDataSourceFactory = getHttpDataSourceFactory();
        return new DefaultDataSourceFactory(mAppContext, httpDataSourceFactory);
    }

    /**
     * 创建Android的内置网络堆栈
     * 如果 APK 大小是一个关键问题，或者如果媒体流只是应用程序功能的一小部分，则使用内置网络堆栈可能是可以接受的。
     *
     * @return 新的HttpDataSource factory.
     */
    private DataSource.Factory getHttpDataSourceFactory() {
        if (mHttpDataSourceFactory == null) {
            //创建Android的内置网络堆栈
            mHttpDataSourceFactory = new DefaultHttpDataSourceFactory(
                    mUserAgent,
                    null,
                    DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                    DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                    //http->https重定向支持
                    true);
        }
        return mHttpDataSourceFactory;
    }

    private void setHeaders(Map<String, String> headers) {
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                String key = header.getKey();
                String value = header.getValue();
                //如果发现用户通过header传递了UA，则强行将HttpDataSourceFactory里面的userAgent字段替换成用户的
                if (TextUtils.equals(key, "User-Agent")) {
                    if (!TextUtils.isEmpty(value)) {
                        try {
                            Field userAgentField = mHttpDataSourceFactory.getClass().getDeclaredField("userAgent");
                            userAgentField.setAccessible(true);
                            userAgentField.set(mHttpDataSourceFactory, value);
                        } catch (Exception e) {
                            //ignore
                        }
                    }
                } else {
                    mHttpDataSourceFactory.getDefaultRequestProperties().set(key, value);
                }
            }
        }
    }

    public void setCache(Cache cache) {
        this.mCache = cache;
    }

    /**
     * 自定义错误处理
     * 实现自定义LoadErrorHandlingPolicy允许应用自定义 ExoPlayer 对加载错误的反应方式。
     * 例如，应用程序可能希望快速失败而不是多次重试，或者可能希望自定义控制玩家在每次重试之间等待多长时间的回退逻辑。
     */
    private final LoadErrorHandlingPolicy mLoadErrorHandlingPolicy = new DefaultLoadErrorHandlingPolicy() {
        @Override
        public long getRetryDelayMsFor(int dataType, long loadDurationMs, IOException exception, int errorCount) {
            return super.getRetryDelayMsFor(dataType, loadDurationMs, exception, errorCount);
        }
    };

}
