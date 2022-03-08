package com.yc.ipc.test.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

public class MessengerService extends Service {

    private static final String TAG = MessengerService.class.getSimpleName();
    private Messenger mMessenger = new Messenger(new MessengerHandler());

    /**
     * 服务端创建一个 Service 来处理客户端请求，同时通过一个 Handler 对象来实例化一个 Messenger 对象，
     * 然后在 Service 的 onBind 中返回这个 Messenger 对象底层的 Binder 即可
     */

    private class MessengerHandler extends Handler {

        /**
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case Constants.MSG_FROM_CLIENT:
                    Log.d(TAG, "receive msg from client: msg = [" + msg.getData().getString(Constants.MSG_KEY) + "]");
                    Messenger client = msg.replyTo;
                    Message replyMsg = Message.obtain(null, Constants.MSG_FROM_SERVICE);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.MSG_KEY, "我已经收到你的消息，稍后回复你！");
                    replyMsg.setData(bundle);
                    try {
                        client.send(replyMsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
