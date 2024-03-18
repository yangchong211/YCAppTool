package com.yc.imageserver.progress;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/10/24
 *     desc  : glide加载进度工具
 *     revise: 自定义OkHttpUrlLoader
 *
 * </pre>
 */
public class OkHttpUrlLoader implements ModelLoader<GlideUrl, InputStream> {

    private final Call.Factory client;

    // Public API.
    @SuppressWarnings("WeakerAccess")
    public OkHttpUrlLoader(Call.Factory client) {
        this.client = client;
    }

    @Override
    public boolean handles(@NotNull GlideUrl url) {
        return true;
    }

    @Override
    public LoadData<InputStream> buildLoadData(@NotNull GlideUrl model, int width, int height, @NotNull Options options) {
        OkHttpStreamFetcher okHttpStreamFetcher = new OkHttpStreamFetcher(client, model);
        return new LoadData<>(model, okHttpStreamFetcher);
    }

    /**
     * The default factory for {@link OkHttpUrlLoader}s.
     */
    @SuppressWarnings("WeakerAccess")
    public static class Factory implements ModelLoaderFactory<GlideUrl, InputStream> {

        private static volatile Call.Factory internalClient;
        private final Call.Factory client;

        private static Call.Factory getInternalClient() {
            if (internalClient == null) {
                synchronized (Factory.class) {
                    if (internalClient == null) {
                        internalClient = new OkHttpClient();
                    }
                }
            }
            return internalClient;
        }

        /**
         * Constructor for a new Factory that runs requests using a static singleton client.
         */
        public Factory() {
            //获取 OkHttpClient 对象
            this(getInternalClient());
        }

        /**
         * Constructor for a new Factory that runs requests using given client.
         *
         * @param client this is typically an instance of {@code OkHttpClient}.
         */
        public Factory(Call.Factory client) {
            this.client = client;
        }

        @NotNull
        @Override
        public ModelLoader<GlideUrl, InputStream> build(@NotNull MultiModelLoaderFactory multiFactory) {
            return new OkHttpUrlLoader(client);
        }

        @Override
        public void teardown() {

        }
    }
}
