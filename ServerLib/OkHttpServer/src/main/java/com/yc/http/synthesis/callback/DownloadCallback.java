package com.yc.http.synthesis.callback;

import android.text.TextUtils;

import com.yc.http.synthesis.EasyLog;
import com.yc.http.synthesis.EasyUtils;
import com.yc.http.synthesis.config.IRequestInterceptor;
import com.yc.http.synthesis.exception.MD5Exception;
import com.yc.http.synthesis.exception.NullBodyException;
import com.yc.http.synthesis.lifecycle.HttpLifecycleManager;
import com.yc.http.synthesis.listener.OnDownloadListener;
import com.yc.http.synthesis.request.HttpRequest;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2019/11/25
 *    desc   : 下载接口回调
 */
public final class DownloadCallback extends BaseCallback {

    /** 请求配置 */
    private final HttpRequest<?> mHttpRequest;

    /** 文件 MD5 正则表达式 */
    private static final String FILE_MD5_REGEX = "^[\\w]{32}$";

    /** 保存的文件 */
    private File mFile;

    /** 校验的 MD5 */
    private String mMd5;

    /** 下载监听回调 */
    private OnDownloadListener mListener;

    /** 下载总字节 */
    private long mTotalByte;

    /** 已下载字节 */
    private long mDownloadByte;

    /** 下载进度 */
    private int mDownloadProgress;

    public DownloadCallback(HttpRequest<?> request) {
        super(request);
        mHttpRequest = request;
    }

    public DownloadCallback setFile(File file) {
        mFile = file;
        return this;
    }

    public DownloadCallback setMd5(String md5) {
        mMd5 = md5;
        return this;
    }

    public DownloadCallback setListener(OnDownloadListener listener) {
        mListener = listener;
        return this;
    }

    @Override
    protected void onStart(Call call) {
        EasyUtils.post(() -> {
            if (mListener == null || !HttpLifecycleManager.isLifecycleActive(mHttpRequest.getLifecycleOwner())) {
                return;
            }
            mListener.onStart(mFile);
        });
    }

    @Override
    protected void onResponse(Response response) throws Exception {
        // 打印请求耗时时间
        EasyLog.printLog(mHttpRequest, "RequestConsuming：" +
                (response.receivedResponseAtMillis() - response.sentRequestAtMillis()) + " ms");

        IRequestInterceptor interceptor = mHttpRequest.getRequestInterceptor();
        if (interceptor != null) {
            response = interceptor.interceptResponse(mHttpRequest, response);
        }

        // 如果没有指定文件的 md5 值
        if (mMd5 == null) {
            // 获取响应头中的文件 MD5 值
            String md5 = response.header("Content-MD5");
            // 这个 md5 值必须是文件的 md5 值
            if (!TextUtils.isEmpty(md5) && md5.matches(FILE_MD5_REGEX)) {
                mMd5 = md5;
            }
        }

        File parentFile = mFile.getParentFile();
        if (parentFile != null) {
            EasyUtils.createFolder(parentFile);
        }
        ResponseBody body = response.body();
        if (body == null) {
            throw new NullBodyException("The response body is empty");
        }

        mTotalByte = body.contentLength();
        if (mTotalByte < 0) {
            mTotalByte = 0;
        }

        // 如果这个文件已经下载过，并且经过校验 MD5 是同一个文件的话，就直接回调下载成功监听
        if (!TextUtils.isEmpty(mMd5) && mFile.isFile() &&
                mMd5.equalsIgnoreCase(EasyUtils.getFileMd5(EasyUtils.openFileInputStream(mFile)))) {
            EasyUtils.post(() -> {
                if (mListener == null || !HttpLifecycleManager.isLifecycleActive(mHttpRequest.getLifecycleOwner())) {
                    return;
                }
                mListener.onComplete(mFile, true);
                mListener.onEnd(mFile);
                // 文件已存在，跳过下载
                EasyLog.printLog(mHttpRequest, mFile.getPath() + " file already exists, skip download");
            });
            return;
        }

        int readLength;
        mDownloadByte = 0;
        byte[] bytes = new byte[8192];
        InputStream inputStream = body.byteStream();
        OutputStream outputStream = EasyUtils.openFileOutputStream(mFile);
        while ((readLength = inputStream.read(bytes)) != -1) {
            mDownloadByte += readLength;
            outputStream.write(bytes, 0, readLength);
            EasyUtils.post(() -> {
                if (mListener == null || !HttpLifecycleManager.isLifecycleActive(mHttpRequest.getLifecycleOwner())) {
                    return;
                }
                mListener.onByte(mFile, mTotalByte, mDownloadByte);
                int progress = EasyUtils.getProgressProgress(mTotalByte, mDownloadByte);
                // 只有下载进度发生改变的时候才回调此方法，避免引起不必要的 View 重绘
                if (progress != mDownloadProgress) {
                    mDownloadProgress = progress;
                    mListener.onProgress(mFile, mDownloadProgress);
                    EasyLog.printLog(mHttpRequest, mFile.getPath() +
                            ", downloaded: " + mDownloadByte + " / " + mTotalByte +
                            ", progress: " + progress + " %");
                }
            });
        }
        EasyUtils.closeStream(inputStream);
        EasyUtils.closeStream(outputStream);

        String md5 = EasyUtils.getFileMd5(EasyUtils.openFileInputStream(mFile));
        if (!TextUtils.isEmpty(mMd5) && !mMd5.equalsIgnoreCase(md5)) {
            // 文件 MD5 值校验失败
            throw new MD5Exception("MD5 verify failure", md5);
        }

        EasyUtils.post(() -> {
            if (mListener == null || !HttpLifecycleManager.isLifecycleActive(mHttpRequest.getLifecycleOwner())) {
                return;
            }
            mListener.onComplete(mFile, false);
            mListener.onEnd(mFile);
        });
    }

    @Override
    protected void onFailure(final Exception e) {
        EasyLog.printThrowable(mHttpRequest, e);
        // 打印错误堆栈
        final Exception finalException = mHttpRequest.getRequestHandler().requestFail(mHttpRequest, e);
        if (finalException != e) {
            EasyLog.printThrowable(mHttpRequest, finalException);
        }

        EasyUtils.post(() -> {
            if (mListener == null || !HttpLifecycleManager.isLifecycleActive(mHttpRequest.getLifecycleOwner())) {
                return;
            }
            mListener.onError(mFile, finalException);
            mListener.onEnd(mFile);
            EasyLog.printLog(mHttpRequest, mFile.getPath() + " download completed");
        });
    }
}