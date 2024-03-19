package com.yc.appgrpc;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;

import io.grpc.netty.NettyServerBuilder;
import io.grpc.stub.StreamObserver;

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


    public class GRPCServiceImpl extends GreeterGrpc.GreeterImplBase {
        private final String TAG = GRPCServiceImpl.class.toString();
        private Context context;
        public GRPCServiceImpl(Context context) {
            this.context = context;
        }

        // 使用广播的方法发送数据更新ui
        private void sendInfo(String info) {
            Intent intent = new Intent("main.info");
            intent.putExtra("info", info);
            context.sendBroadcast(intent);
        }

        @Override
        public void simpleHello(Request request, StreamObserver<Reply> responseObserver) {
            Log.d(TAG, "服务端调用simpleHello.");
            String info = "[客户端->服务端]" + request.getReqInfo();
            sendInfo(info);
            responseObserver.onNext(Reply.newBuilder().setRepInfo("simpleHello").build());
            responseObserver.onCompleted();
            super.simpleHello(request, responseObserver);
        }

        @Override
        public StreamObserver<Request> clientStream(StreamObserver<Reply> responseObserver) {
            StreamObserver<Request> streamObserver = new StreamObserver<Request>() {
                @Override
                public void onNext(Request value) {
                    Log.d(TAG, "clientStream onNext.");
                    String info = "[服务端->客户端]" + value.getReqInfo();
                    sendInfo(info);
                }
                @Override
                public void onError(Throwable t) {
                    Log.d(TAG, "clientStream onError.");
                }
                @Override
                public void onCompleted() {
                    Log.d(TAG, "clientStream onCompleted.");
                    // 接收完所有消息后给客户端发送消息
                    responseObserver.onNext(Reply.newBuilder().setRepInfo("clientStream").build());
                    responseObserver.onCompleted();
                }
            };
            return streamObserver;
        }

        @Override
        public void serverStream(Request request, StreamObserver<Reply> responseObserver) {
            String info = "[客户端->服务端]" + request.getReqInfo();
            sendInfo(info);
            responseObserver.onNext(Reply.newBuilder().setRepInfo("serverStream1").build());
            responseObserver.onNext(Reply.newBuilder().setRepInfo("serverStream2").build());
            responseObserver.onCompleted();
            super.serverStream(request, responseObserver);
        }

        @Override
        public StreamObserver<Request> bothFlowStream(StreamObserver<Reply> responseObserver) {
            StreamObserver<Request> streamObserver = new StreamObserver<Request>() {
                @Override
                public void onNext(Request value) {
                    Log.d(TAG, "bothFlowStream onNext.");
                    String info = "[客户端->服务端]" + value.getReqInfo();
                    sendInfo(info);
                }
                @Override
                public void onError(Throwable t) {
                    Log.d(TAG, "bothFlowStream onError.");
                }
                @Override
                public void onCompleted() {
                    Log.d(TAG, "bothFlowStream onCompleted.");
                    responseObserver.onNext(Reply.newBuilder().setRepInfo("bothFlowStream1").build());
                    responseObserver.onNext(Reply.newBuilder().setRepInfo("bothFlowStream2").build());
                    responseObserver.onCompleted();
                }
            };
            return streamObserver;
        }
    }

}
