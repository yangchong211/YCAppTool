package com.yc.http.synthesis;

import androidx.lifecycle.LifecycleOwner;

import com.yc.http.synthesis.request.DeleteRequest;
import com.yc.http.synthesis.request.DownloadRequest;
import com.yc.http.synthesis.request.GetRequest;
import com.yc.http.synthesis.request.HeadRequest;
import com.yc.http.synthesis.request.OptionsRequest;
import com.yc.http.synthesis.request.PatchRequest;
import com.yc.http.synthesis.request.PostRequest;
import com.yc.http.synthesis.request.PutRequest;
import com.yc.http.synthesis.request.TraceRequest;
import com.yc.http.synthesis.lifecycle.ActivityLifecycle;
import com.yc.http.synthesis.lifecycle.ApplicationLifecycle;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2019/05/19
 *    desc   : 网络请求类
 */
@SuppressWarnings("unused")
public final class EasyHttp {

    /**
     * Get 请求
     *
     * @param lifecycleOwner      请传入 AppCompatActivity 或者 AndroidX.Fragment 子类
     *                            如需传入其他对象请参考以下两个类
     *                            {@link ActivityLifecycle}
     *                            {@link ApplicationLifecycle}
     */
    public static GetRequest get(LifecycleOwner lifecycleOwner) {
        return new GetRequest(lifecycleOwner);
    }

    /**
     * Post 请求
     *
     * @param lifecycleOwner      请传入 AppCompatActivity 或者 AndroidX.Fragment 子类
     *                            如需传入其他对象请参考以下两个类
     *                            {@link ActivityLifecycle}
     *                            {@link ApplicationLifecycle}
     */
    public static PostRequest post(LifecycleOwner lifecycleOwner) {
        return new PostRequest(lifecycleOwner);
    }

    /**
     * Head 请求
     *
     * @param lifecycleOwner      请传入 AppCompatActivity 或者 AndroidX.Fragment 子类
     *                            如需传入其他对象请参考以下两个类
     *                            {@link ActivityLifecycle}
     *                            {@link ApplicationLifecycle}
     */
    public static HeadRequest head(LifecycleOwner lifecycleOwner) {
        return new HeadRequest(lifecycleOwner);
    }

    /**
     * Delete 请求
     *
     * @param lifecycleOwner      请传入 AppCompatActivity 或者 AndroidX.Fragment 子类
     *                            如需传入其他对象请参考以下两个类
     *                            {@link ActivityLifecycle}
     *                            {@link ApplicationLifecycle}
     */
    public static DeleteRequest delete(LifecycleOwner lifecycleOwner) {
        return new DeleteRequest(lifecycleOwner);
    }

    /**
     * Put 请求
     *
     * @param lifecycleOwner      请传入 AppCompatActivity 或者 AndroidX.Fragment 子类
     *                            如需传入其他对象请参考以下两个类
     *                            {@link ActivityLifecycle}
     *                            {@link ApplicationLifecycle}
     */
    public static PutRequest put(LifecycleOwner lifecycleOwner) {
        return new PutRequest(lifecycleOwner);
    }

    /**
     * Patch 请求
     *
     * @param lifecycleOwner      请传入 AppCompatActivity 或者 AndroidX.Fragment 子类
     *                            如需传入其他对象请参考以下两个类
     *                            {@link ActivityLifecycle}
     *                            {@link ApplicationLifecycle}
     */
    public static PatchRequest patch(LifecycleOwner lifecycleOwner) {
        return new PatchRequest(lifecycleOwner);
    }

    /**
     * Options 请求
     *
     * @param lifecycleOwner      请传入 AppCompatActivity 或者 AndroidX.Fragment 子类
     *                            如需传入其他对象请参考以下两个类
     *                            {@link ActivityLifecycle}
     *                            {@link ApplicationLifecycle}
     */
    public static OptionsRequest options(LifecycleOwner lifecycleOwner) {
        return new OptionsRequest(lifecycleOwner);
    }

    /**
     * Trace 请求
     *
     * @param lifecycleOwner      请传入 AppCompatActivity 或者 AndroidX.Fragment 子类
     *                            如需传入其他对象请参考以下两个类
     *                            {@link ActivityLifecycle}
     *                            {@link ApplicationLifecycle}
     */
    public static TraceRequest trace(LifecycleOwner lifecycleOwner) {
        return new TraceRequest(lifecycleOwner);
    }

    /**
     * 下载请求
     *
     * @param lifecycleOwner      请传入 AppCompatActivity 或者 AndroidX.Fragment 子类
     *                            如需传入其他对象请参考以下两个类
     *                            {@link ActivityLifecycle}
     *                            {@link ApplicationLifecycle}
     */
    public static DownloadRequest download(LifecycleOwner lifecycleOwner) {
        return new DownloadRequest(lifecycleOwner);
    }

    /**
     * 根据 TAG 取消请求任务
     */
    public static void cancel(Object tag) {
        cancel(EasyUtils.getObjectTag(tag));
    }

    public static void cancel(String tag) {
        if (tag == null) {
            return;
        }

        OkHttpClient client = EasyConfig.getInstance().getClient();

        // 清除排队等候的任务
        for (Call call : client.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }

        // 清除正在执行的任务
        for (Call call : client.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /**
     * 清除所有请求任务
     */
    public static void cancel() {
        OkHttpClient client = EasyConfig.getInstance().getClient();

        // 清除排队等候的任务
        for (Call call : client.dispatcher().queuedCalls()) {
            call.cancel();
        }

        // 清除正在执行的任务
        for (Call call : client.dispatcher().runningCalls()) {
            call.cancel();
        }
    }
}