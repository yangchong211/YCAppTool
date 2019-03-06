package com.ycbjie.library.utils.handler;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/03/22
 *     desc  : Handler
 *     revise: 弱引用–>随时可能会被垃圾回收器回收，不一定要等到虚拟机内存不足时才强制回收。
 * </pre>
 */
public class HandlerHolder extends Handler {

    private WeakReference<OnReceiveMessageListener> mListenerWeakReference;

    /**
     * @param listener 收到消息回调接口
     */
    public HandlerHolder(OnReceiveMessageListener listener) {
        mListenerWeakReference = new WeakReference<>(listener);
    }

    @Override
    public void handleMessage(Message msg) {
        if (mListenerWeakReference!=null && mListenerWeakReference.get()!=null){
            mListenerWeakReference.get().handlerMessage(msg);
        }
    }

    /**
     * 收到消息回调接口
     */
    public interface OnReceiveMessageListener {
        /**
         * 消息处理
         * @param msg           msg
         */
        void handlerMessage(Message msg);
    }
}
