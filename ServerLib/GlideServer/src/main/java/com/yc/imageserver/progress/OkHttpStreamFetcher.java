package com.yc.imageserver.progress;

import android.os.Build;
import androidx.annotation.NonNull;
import android.util.Log;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.HttpException;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.util.ContentLengthInputStream;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Synthetic;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/10/24
 *     desc  : glide加载进度工具
 *     revise: 替换通讯组件
 *             Glide内部HTTP通讯组件的底层实现是基于HttpUrlConnection来进行定制的。
 *             但是HttpUrlConnection的可扩展性比较有限，我们在它的基础之上无法实现监听下载进度的功能，
 *             因此今天的第一个大动作就是要将Glide中的HTTP通讯组件替换成OkHttp。
 * </pre>
 */
public class OkHttpStreamFetcher implements DataFetcher<InputStream>, okhttp3.Callback {

    private static final String TAG = "OkHttpFetcher";
    private final Call.Factory client;
    private final GlideUrl url;
    @Synthetic
    private InputStream stream;
    @Synthetic
    private ResponseBody responseBody;
    private volatile Call call;
    private DataCallback<? super InputStream> callback;

    public OkHttpStreamFetcher(Call.Factory client, GlideUrl url) {
        this.client = client;
        this.url = url;
    }

    @Override
    public void loadData(@NotNull Priority priority, @NotNull final DataFetcher.DataCallback<? super InputStream> callback) {
        String s = url.toStringUrl();
        Request.Builder requestBuilder = new Request.Builder()
                .url(s);
        for (Map.Entry<String, String> headerEntry : url.getHeaders().entrySet()) {
            String key = headerEntry.getKey();
            requestBuilder.addHeader(key, headerEntry.getValue());
        }
        Request request = requestBuilder.build();
        this.callback = callback;
        //创建一个请求call
        call = client.newCall(request);
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            //发起请求
            call.enqueue(this);
        } else {
            try {
                // Calling execute instead of enqueue is a workaround for #2355, where okhttp throws a
                // ClassCastException on O.
                onResponse(call, call.execute());
            } catch (IOException e) {
                onFailure(call, e);
            } catch (ClassCastException e) {
                // It's not clear that this catch is necessary, the error may only occur even on O if
                // enqueue is used.
                onFailure(call, new IOException("Workaround for framework bug on O", e));
            }
        }
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "OkHttp failed to obtain result", e);
        }
        callback.onLoadFailed(e);
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        responseBody = response.body();
        if (response.isSuccessful()) {
            long contentLength = Preconditions.checkNotNull(responseBody).contentLength();
            stream = ContentLengthInputStream.obtain(responseBody.byteStream(), contentLength);
            callback.onDataReady(stream);
        } else {
            callback.onLoadFailed(new HttpException(response.message(), response.code()));
        }
    }

    @Override
    public void cleanup() {
        try {
            if (stream != null) {
                stream.close();
            }
        } catch (IOException e) {
            // Ignored
        }
        if (responseBody != null) {
            responseBody.close();
        }
        callback = null;
    }

    @Override
    public void cancel() {
        Call local = call;
        if (local != null) {
            local.cancel();
        }
    }

    @NonNull
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.REMOTE;
    }
}

