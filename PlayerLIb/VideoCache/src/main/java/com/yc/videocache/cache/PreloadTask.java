package com.yc.videocache.cache;

import com.yc.videocache.HttpProxyCacheServer;
import com.yc.videocache.Logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;

public class PreloadTask implements Runnable {

    /**
     * 原始地址
     */
    public String mRawUrl;

    /**
     * 列表中的位置
     */
    public int mPosition;

    /**
     * VideoCache服务器
     */
    public HttpProxyCacheServer mCacheServer;

    /**
     * 是否被取消
     */
    private boolean mIsCanceled;

    /**
     * 是否正在预加载
     */
    private boolean mIsExecuted;

    @Override
    public void run() {
        if (!mIsCanceled) {
            startPreload();
        }
        mIsExecuted = false;
        mIsCanceled = false;
    }

    /**
     * 开始预加载
     */
    private void startPreload() {
        Logger.info("开始预加载：" + mPosition);
        HttpURLConnection connection = null;
        try {
            //重点内容
            //获取HttpProxyCacheServer的代理地址
            //urlPath指的是网络上的视频路径，返回的proxyUrl是一个代理路径
            //得到这个代理路径后，接下来就只需要将这个路径设置给播放器就完成了。
            String proxyUrl = mCacheServer.getProxyUrl(mRawUrl);

            URL url = new URL(proxyUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5_000);
            connection.setReadTimeout(5_000);
            InputStream in = new BufferedInputStream(connection.getInputStream());
            int length;
            int read = -1;
            byte[] bytes = new byte[8 * 1024];
            while ((length = in.read(bytes)) != -1) {
                read += length;
                //预加载完成或者取消预加载
                if (mIsCanceled || read >= PreloadManager.PRELOAD_LENGTH) {
                    Logger.info("结束预加载：" + mPosition);
                    break;
                }
            }
            if (read == -1) {
                //这种情况一般是预加载出错了，删掉缓存
                Logger.info("预加载失败：" +  mPosition);
                File cacheFile = mCacheServer.getCacheFile(mRawUrl);
                if (cacheFile.exists()) {
                    cacheFile.delete();
                }
            }
        } catch (Exception e) {
            Logger.info("异常结束预加载：" + mPosition);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * 将预加载任务提交到线程池，准备执行
     */
    public void executeOn(ExecutorService executorService) {
        if (mIsExecuted) {
            return;
        }
        mIsExecuted = true;
        executorService.submit(this);
        //executorService.execute(this);
    }

    /**
     * 取消预加载任务
     */
    public void cancel() {
        if (mIsExecuted) {
            mIsCanceled = true;
        }
    }
}
