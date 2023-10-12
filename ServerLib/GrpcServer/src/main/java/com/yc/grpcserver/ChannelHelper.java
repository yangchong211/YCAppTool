package com.yc.grpcserver;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.grpc.ConnectivityState;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public final class ChannelHelper {

    /**
     * 构建一条普通的Channel
     *
     * @param host 主机服务地址
     * @param port 端口
     * @return
     */
    public static ManagedChannel newChannel1(String host, int port) {
        //Channel通过ip和端口注册了一个与Server端连接的通道(Connection)。
        return ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
    }

    public static ManagedChannel newChannel2(String host, int port) {
        return GrpcChannelBuilder.build(host, port, null, false, null);
    }

    /**
     * 获取连接状态
     *
     * @param channel channel通道
     * @return
     */
    public static ConnectivityState getChannelState(ManagedChannel channel) {
        ConnectivityState state = channel.getState(true);
        return state;
    }

    /**
     * 监听连接状态
     *
     * @param channel channel通道
     */
    public static void notifyWhenStateChanged(ManagedChannel channel) {
        channel.notifyWhenStateChanged(channel.getState(true), () -> {
            Log.d("ChannelHelper", "通道的状态回调：" + getChannelState(channel).toString());
        });
    }

    /**
     * @return 是否存活
     */
    public static boolean isChannelActive(ManagedChannel managedChannel) {
        return managedChannel != null && !managedChannel.isShutdown();
    }

    /**
     * @return 是否重建
     */
    public static boolean isChannelCreate(ManagedChannel managedChannel) {
        return managedChannel != null && !managedChannel.isTerminated();
    }

    /**
     * 关闭Channel
     *
     * @param channel 端口
     * @return
     */
    public static boolean shutdown(ManagedChannel channel) {
        if (channel != null) {
            try {
                return channel.shutdown().awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return false;
    }

}
