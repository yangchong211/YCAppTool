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
public class HandlerUtils {

    private HandlerUtils() {
        throw new UnsupportedOperationException("不能直接创建");
    }

    public static class HandlerReference extends Handler {

        private final WeakReference<Object> mObject;
        private OnReceiveMessageListener mListener;


        public HandlerReference(Object object, OnReceiveMessageListener listener) {
            this.mObject = new WeakReference<>(object);
            this.mListener = listener;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Object object = mObject.get();
            if (object != null && mListener!=null){
                //处理逻辑
                mListener.handlerMessage(msg);
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

}
