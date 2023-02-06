package com.yc.m3u8.manager;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.yc.m3u8.bean.M3u8;
import com.yc.m3u8.inter.OnM3u8InfoListener;
import com.yc.m3u8.utils.M3u8FileUtils;

import java.io.IOException;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 获取M3U8信息的管理器
 *     revise:
 * </pre>
 */
public class M3u8InfoManger {

    private static M3u8InfoManger mM3U8InfoManger;
    private OnM3u8InfoListener onM3U8InfoListener;
    private static final int WHAT_ON_ERROR = 1101;
    private static final int WHAT_ON_SUCCESS = 1102;

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_ON_ERROR:
                    onM3U8InfoListener.onError((Throwable) msg.obj);
                    break;
                case WHAT_ON_SUCCESS:
                    onM3U8InfoListener.onSuccess((M3u8) msg.obj);
                    break;
            }
        }
    };

    private M3u8InfoManger() {

    }

    public static M3u8InfoManger getInstance() {
        synchronized (M3u8InfoManger.class) {
            if (mM3U8InfoManger == null) {
                mM3U8InfoManger = new M3u8InfoManger();
            }
        }
        return mM3U8InfoManger;
    }

    /**
     * 获取m3u8信息
     *
     * @param url
     * @param onM3U8InfoListener
     */
    public synchronized void getM3U8Info(final String url, OnM3u8InfoListener onM3U8InfoListener) {
        this.onM3U8InfoListener = onM3U8InfoListener;
        onM3U8InfoListener.onStart();
        new Thread() {
            @Override
            public void run() {
                try {
//                    Log.e("hdltag", "run(M3U8InfoManger.java:62):" + url);
                    M3u8 m3u8 = M3u8FileUtils.parseIndex(url);
                    handlerSuccess(m3u8);
                } catch (IOException e) {
//                    e.printStackTrace();
                    handlerError(e);
                }
            }
        }.start();

    }

    /**
     * 通知异常
     *
     * @param e
     */
    private void handlerError(Throwable e) {
        Message msg = mHandler.obtainMessage();
        msg.obj = e;
        msg.what = WHAT_ON_ERROR;
        mHandler.sendMessage(msg);
    }

    /**
     * 通知成功
     *
     * @param m3u8
     */
    private void handlerSuccess(M3u8 m3u8) {
        Message msg = mHandler.obtainMessage();
        msg.obj = m3u8;
        msg.what = WHAT_ON_SUCCESS;
        mHandler.sendMessage(msg);
    }
}
