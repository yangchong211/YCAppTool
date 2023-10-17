package com.yc.appgrpc;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;

import io.grpc.netty.NettyServerBuilder;

public class GRPCServer {

    private final String TAG = GRPCServer.class.toString();
    private final int port = 55056;
    private final Context context;

    public GRPCServer(Context context) {
        this.context = context;
        start();
        Log.d(TAG, "服务端启动");
        sendInfo("服务端启动");
    }

    // 使用广播的方法发送数据更新ui
    private void sendInfo(String info) {
        Intent intent = new Intent("main.info");
        intent.putExtra("info", info);
        context.sendBroadcast(intent);
    }

    private void start() {
        try {
            NettyServerBuilder.forPort(port)
                    .addService(new GRPCServiceImpl(context))
                    .build()
                    .start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
