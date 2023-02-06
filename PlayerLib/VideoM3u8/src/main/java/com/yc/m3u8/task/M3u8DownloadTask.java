package com.yc.m3u8.task;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.yc.m3u8.manager.M3u8InfoManger;
import com.yc.m3u8.bean.M3u8;
import com.yc.m3u8.bean.M3u8Ts;
import com.yc.m3u8.inter.OnDownloadListener;
import com.yc.m3u8.inter.OnM3u8InfoListener;
import com.yc.m3u8.utils.M3u8FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : M3U8下载管理器
 *     revise:
 * </pre>
 */
public class M3u8DownloadTask {
    private OnDownloadListener onDownloadListener;
    private static final int WHAT_ON_ERROR = 1001;
    private static final int WHAT_ON_PROGRESS = 1002;
    private static final int WHAT_ON_SUCCESS = 1003;

    public M3u8DownloadTask(String taskId) {
        this.taskId = taskId;
        //需要加上当前时间作为文件夹（由于合并时是根据文件夹来合并的，合并之后需要删除所有的ts文件，这里用到了多线程，所以需要按文件夹来存ts）
        tempDir += File.separator + System.currentTimeMillis() / (1000 * 60 * 60 * 24) + "-" + taskId;
    }

    //临时下载目录
    private String tempDir = Environment.getExternalStorageDirectory().getPath() + File.separator + "m3u8temp";
    //最终文件保存的路径
    private String saveFilePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "11m3u8";
    //当前下载完成的文件个数
    private static int curTs = 0;
    //总文件的个数
    private static int totalTs = 0;
    //单个文件的大小
    private static long itemFileSize = 0;
    /**
     * 当前已经在下完成的大小
     */
    private long curLenght = 0;
    /**
     * 任务是否正在运行中
     */
    private boolean isRunning = false;
    /**
     * 任务id，用于断点续传.
     * 如果任务已经停止、下一次会根据此id来找到上一次已经下载完成的ts文件，开始下载之前，会判断是否已经下载过了，下载了就不再下载
     */
    private String taskId = "0";
    /**
     * 线程池最大线程数，默认为3
     */
    private int threadCount = 3;
    /**
     * 时候清楚临时目录，默认清除
     */
    private boolean isClearTempDir = true;
    /**
     * 读取超时时间
     */
    private int readTimeout = 30 * 60 * 1000;
    /**
     * 链接超时时间
     */
    private int connTimeout = 10 * 1000;
    /**
     * 定时任务
     */
    private Timer netSpeedTimer;
    private ExecutorService executor;//线程池
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_ON_ERROR:
                    onDownloadListener.onError((Throwable) msg.obj);
                    break;
                case WHAT_ON_PROGRESS:
                    onDownloadListener.onDownloading(itemFileSize, totalTs, curTs);
                    break;
                case WHAT_ON_SUCCESS:
                    if (netSpeedTimer != null) {
                        netSpeedTimer.cancel();
                    }
                    onDownloadListener.onSuccess();
                    break;
            }
        }
    };

    /**
     * 设置最大线程数
     *
     * @param threadCount
     */
    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    /**
     * 开始下载
     *
     * @param url
     * @param onDownloadListener
     */
    public void download(final String url, OnDownloadListener onDownloadListener) {
        this.onDownloadListener = onDownloadListener;
        if (!isRunning()) {
            getM3U8Info(url);
        } else {
            handlerError(new Throwable("Task running"));
        }
    }

    public long getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public long getConnTimeout() {
        return connTimeout;
    }

    public void setConnTimeout(int connTimeout) {
        this.connTimeout = connTimeout;
    }

    public boolean isClearTempDir() {
        return isClearTempDir;
    }

    public void setClearTempDir(boolean clearTempDir) {
        isClearTempDir = clearTempDir;
    }

    public String getTaskId() {
        return taskId;
    }

    /**
     * 获取任务是否正在执行
     *
     * @return
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * 先获取m3u8信息
     *
     * @param url
     */
    private void getM3U8Info(String url) {
        M3u8InfoManger.getInstance().getM3U8Info(url, new OnM3u8InfoListener() {
            @Override
            public void onSuccess(final M3u8 m3U8) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            startDownload(m3U8);
                            if (executor != null) {
                                executor.shutdown();//下载完成之后要关闭线程池
                            }
                            while (executor != null && !executor.isTerminated()) {
                                //等待中
                                Thread.sleep(100);
                            }
                            if (isRunning) {
                                String saveFileName = saveFilePath.substring(saveFilePath.lastIndexOf("/") + 1);
                                String tempSaveFile = tempDir + File.separator + saveFileName;//生成临时文件
                                M3u8FileUtils.merge(m3U8, tempSaveFile, tempDir);//合并ts
                                //移动到指定的目录
                                M3u8FileUtils.moveFile(tempSaveFile, saveFilePath);//移动到指定文件夹
                                if (isClearTempDir) {
                                    mHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            M3u8FileUtils.clearDir(new File(tempDir));//清空一下临时文件
                                        }
                                    }, 20 * 1000);//20s之后再删除
                                }
                                mHandler.sendEmptyMessage(WHAT_ON_SUCCESS);
                                isRunning = false;
                            }
                        } catch (InterruptedIOException e) {
//                    e.printStackTrace();
                            //被中断了，使用stop时会抛出这个，不需要处理
//                            handlerError(e);
                            return;
                        } catch (IOException e) {
//                    e.printStackTrace();
                            handlerError(e);
                            return;
                        } catch (InterruptedException e) {
//                            e.printStackTrace();
                            handlerError(e);
                        }
                    }
                }.start();
            }

            @Override
            public void onStart() {
                onDownloadListener.onStart();
                isRunning = true;
            }

            @Override
            public void onError(Throwable errorMsg) {
                handlerError(errorMsg);
            }
        });
    }

    /**
     * 开始下载
     *
     * @param m3U8
     */
    private void startDownload(final M3u8 m3U8) {
        if (m3U8 == null) {
            handlerError(new Throwable("M3U8 is null"));
            return;
        }
        final File dir = new File(tempDir);
        //没有就创建
        if (!dir.exists()) {
            dir.mkdirs();
        }/* else {
            //有就清空内容
            MUtils.clearDir(dir);
        }*/
        totalTs = m3U8.getTsList().size();
        if (executor != null && executor.isTerminated()) {
            executor.shutdownNow();
            executor = null;
        }
        executor = Executors.newFixedThreadPool(threadCount);
        final String basePath = m3U8.getBasepath();
        netSpeedTimer = new Timer();
        netSpeedTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                onDownloadListener.onProgress(curLenght);
            }
        }, 0, 1000);
        for (final M3u8Ts m3U8Ts : m3U8.getTsList()) {//循环下载
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    File file = new File(dir + File.separator + m3U8Ts.getFileName());
                    if (!file.exists()) {//下载过的就不管了
                        FileOutputStream fos = null;
                        InputStream inputStream = null;
                        try {
                            String urlPath;
                            if ("http".equals(m3U8Ts.getFile().substring(0, 4))) {
                                urlPath = m3U8Ts.getFile();
                            } else {
                                urlPath = basePath + m3U8Ts.getFile();
                            }
                            URL url = new URL(urlPath);

                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setConnectTimeout(connTimeout);
                            conn.setReadTimeout(readTimeout);
                            if (conn.getResponseCode() == 200) {
                                inputStream = conn.getInputStream();
                                fos = new FileOutputStream(file);//会自动创建文件
                                int len = 0;
                                byte[] buf = new byte[8 * 1024 * 1024];
                                while ((len = inputStream.read(buf)) != -1) {
                                    curLenght += len;
                                    fos.write(buf, 0, len);//写入流中
                                }
                            } else {
                                handlerError(new Throwable(String.valueOf(conn.getResponseCode())));
                            }
                        } catch (MalformedURLException e) {
//                            e.printStackTrace();
                            handlerError(e);
                        } catch (IOException e) {
//                            e.printStackTrace();
                            handlerError(e);
                        } finally {//关流
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (IOException e) {
//                                    e.printStackTrace();
                                }
                            }
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e) {
//                                    e.printStackTrace();
                                }
                            }
                        }
                        curTs++;
                        if (curTs == 3) {
                            itemFileSize = file.length();
                        }
                        mHandler.sendEmptyMessage(WHAT_ON_PROGRESS);
                    }
                }
            });
        }
    }

    public String getSaveFilePath() {
        return saveFilePath;
    }

    public void setSaveFilePath(String saveFilePath) {
        this.saveFilePath = saveFilePath;
    }

    /**
     * 通知异常
     *
     * @param e
     */
    private void handlerError(Throwable e) {
        if (!"Task running".equals(e.getMessage())) {
            stop();
        }
        //不提示被中断的情况
        if ("thread interrupted".equals(e.getMessage())) {
            return;
        }
        Message msg = mHandler.obtainMessage();
        msg.obj = e;
        msg.what = WHAT_ON_ERROR;
        mHandler.sendMessage(msg);
    }

    /**
     * 停止任务
     */
    public void stop() {
        if (netSpeedTimer != null) {
            netSpeedTimer.cancel();
            netSpeedTimer = null;
        }
        isRunning = false;
        if (executor != null) {
            executor.shutdownNow();
        }
    }

//    /**
//     * 获取当前下载速度
//     *
//     * @param max 最大值
//     * @return
//     */
//    public String getNetSpeed(int max) {
//        int speed = (int) (Math.random() * max + 1);
//        return speed + " kb/s";
//    }
}
