package com.yc.appgrpc;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.yc.grpcserver.ChannelHelper;

import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;

public class GRPCClient {

    private final String TAG = GRPCClient.class.toString();
    private final String host = "localhost";
    private final int port = 55056;
    private Context context;
    private ManagedChannel managedChannel;
    private GreeterGrpc.GreeterStub stub;
    // 在构造函数中连接
    public GRPCClient(Context context) {
        this.context = context;
//        managedChannel = ManagedChannelBuilder.forAddress(host, port)
//                .usePlaintext()
//                .build();
        managedChannel = ChannelHelper.getInstance().newChannel2(host,port);
        stub = GreeterGrpc.newStub(managedChannel);
    }
    // 使用广播的方法发送数据更新ui
    private void sendInfo(String info) {
        Intent intent = new Intent("main.info");
        intent.putExtra("info", info);
        context.sendBroadcast(intent);
    }
    // 简单一元模式
    public void simpleHello() {
        // 构建简单的消息发送
        Request request = Request.newBuilder().setReqInfo("simpleHello").build();
        stub.simpleHello(request, new StreamObserver<Reply>() {
            @Override
            public void onNext(Reply value) {
                Log.d(TAG, "simpleHello onNext.");
                String info = "[服务端->客户端]" + value.getRepInfo();
                sendInfo(info);
            }
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "simpleHello onError.");
            }
            @Override
            public void onCompleted() {
                Log.d(TAG, "simpleHello onCompleted.");
            }
        });
    }
    // 客户端流模式
    public void clientStream() {
        StreamObserver<Request> requestStreamObserver = stub.clientStream(new StreamObserver<Reply>() {
            @Override
            public void onNext(Reply value) {
                Log.d(TAG, "clientStream onNext.");
                String info = "[服务端->客户端]" + value.getRepInfo();
                sendInfo(info);
            }
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "clientStream onError.");
            }
            @Override
            public void onCompleted() {
                Log.d(TAG, "clientStream onCompleted.");
            }
        });
        requestStreamObserver.onNext(Request.newBuilder().setReqInfo("clientStream1").build());
        requestStreamObserver.onNext(Request.newBuilder().setReqInfo("clientStream2").build());
        requestStreamObserver.onCompleted();
    }
    // 服务端流模式
    public void serverStream() {
        Request request = Request.newBuilder().setReqInfo("serverStream").build();
        stub.serverStream(request, new StreamObserver<Reply>() {
            @Override
            public void onNext(Reply value) {
                Log.d(TAG, "serverStream onNext.");
                String info = "[服务端->客户端]" + value.getRepInfo();
                sendInfo(info);
            }
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "serverStream onError.");
            }
            @Override
            public void onCompleted() {
                Log.d(TAG, "serverStream onCompleted.");
            }
        });
    }
    // 双向流模式
    public void bothFlowStream() {
        StreamObserver<Request> streamObserver = stub.bothFlowStream(new StreamObserver<Reply>() {
            @Override
            public void onNext(Reply value) {
                Log.d(TAG, "bothFlowStream onNext.");
                String info = "[服务端->客户端]" + value.getRepInfo();
                sendInfo(info);
            }
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "bothFlowStream onError.");
            }
            @Override
            public void onCompleted() {
                Log.d(TAG, "bothFlowStream onCompleted.");
            }
        });
        streamObserver.onNext(Request.newBuilder().setReqInfo("bothFlowStream1").build());
        streamObserver.onNext(Request.newBuilder().setReqInfo("bothFlowStream2").build());
        streamObserver.onCompleted();
    }

}
