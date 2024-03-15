package com.yc.appcommoninter.report;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 异常上报的类
 * 1、HandlerThread将loop转到子线程中处理，说白了就是将分担MainLooper的工作量，降低了主线程的压力，使主界面更流畅。
 * 2、但是由于每一个任务都将以队列的方式逐个被执行到，一旦队列中有某个任务执行时间过长，那么就会导致后续的任务都会被延迟处理。
 * 3、HandlerThread拥有自己的消息队列，它不会干扰或阻塞UI线程。
 * 4、虽然对于网络IO操作，HandlerThread并不适合，因为它只有一个线程，还得排队一个一个等着，但是我们刚好就是需要这样的一个场合去上传异常日志，避免抢占CPU。
 * 5、我们给HandlerThread设置一个较低优先级，让它能够空闲的时候或者CPU低负荷的时候工作，CPU高负荷的时候尽量让出，保证主线程流畅
 */
public class ExceptionManager extends HandlerThread {

    private static final String TAG = ExceptionManager.class.getSimpleName();
    //volatile关键字禁止指令重排
    private static volatile ExceptionManager singleton;
    //消息队列
    private Handler handler;
    //分隔符
    private final String SEP = "|";

    private ExceptionManager(String name) {
        super(name, android.os.Process.THREAD_PRIORITY_BACKGROUND);
        startHandlerThread();
    }

    public static ExceptionManager getInstance() {
        if (singleton == null) {
            synchronized (ExceptionManager.class) {
                if (singleton == null) {
                    singleton = new ExceptionManager("exception-handler-thread");
                }
            }
        }
        return singleton;
    }

    /**
     * 启动线程
     */
    private void startHandlerThread() {
        LoggerReporter.report(TAG, "startHandle");
        //开启一个线程，必须执行start才能初始化looper
        start();
        //在这个线程中创建一个handler对象
        handler = new Handler(getLooper());
    }

    /**
     * 发送异常上传日志
     *
     * @param type 异常类型
     */
    public void sendException(String type, String content) {
        sendException(type, content, "");
    }

    /**
     * 发送异常上传日志
     *
     * @param type 异常类型
     * @param content 异常内容
     */
    public void sendException(String type, String content, String userinfoMsg) {
        LoggerReporter.report(TAG, "sendException-type:" + type + " content:" + content);
        //基本信息
        String baseMsg = getBaseMsg(userinfoMsg);
        //上报的消息
        ExceptionBean exceptionBean = new ExceptionBean(type, content + baseMsg);
        LoggerReporter.report(TAG, "checkUnUploadException");
        handler.post(() -> {
            try {
                ExceptionReporter.report(exceptionBean.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 退出要清除
     */
    public void release() {
        LoggerReporter.report(TAG, "release");
        if (singleton != null) {
            singleton.quit();
        }
        singleton = null;
    }

    /**
     * 搜集基本信息上报
     *
     * @return 基本信息
     */
    @SuppressLint("SimpleDateFormat")
    private String getBaseMsg(String userinfoMsg) {
        StringBuilder builder = new StringBuilder();
        builder.append(SEP);
        //用户信息
        if (!TextUtils.isEmpty(userinfoMsg)) {
            builder.append(userinfoMsg);
            builder.append(SEP);
        }
        //上报时间
        String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
        builder.append(nowTime);
        return builder.toString();
    }

    /**
     * 异常类的Bean
     */
    private static class ExceptionBean {

        public String type;
        public String content;

        ExceptionBean(String type, String content) {
            this.type = type;
            this.content = content;
        }

        @Override
        public String toString() {
            return "ExceptionBean{" +
                    "type='" + type + '\'' +
                    ", content='" + content + '\'' +
                    '}';
        }
    }
}
