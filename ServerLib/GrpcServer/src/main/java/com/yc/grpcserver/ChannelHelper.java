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

    public static ManagedChannel newChannel3(String url) {
        return GrpcChannelBuilder.build(url, null, false, null);
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
        Log.d("ChannelHelper",  "通道的状态：" + channel.getState(true).toString());
        channel.notifyWhenStateChanged(channel.getState(true), () -> {
            ConnectivityState state = channel.getState(true);
            Log.d("ChannelHelper", "通道的状态回调：" + state.toString());
        });
    }

    /**
     * @return 是否存活
     */
    public static boolean isChannelActive(ManagedChannel managedChannel) {
        //返回channel是否是关闭。关闭的channel立即取消任何新的调用，但是将继续有一些正在处理的调用。
        return managedChannel != null && !managedChannel.isShutdown();
    }

    /**
     * @return 返回channel是否是结束
     */
    public static boolean isChannelTerminated(ManagedChannel managedChannel) {
        //返回channel是否是结束。结束的 channel 没有运行中的调用，并且相关的资源已经释放（像TCP连接）。
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
                //等待 channel 变成结束，如果超时时间达到则放弃。
                return channel.shutdown().awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return false;
    }

    /**
     * 关闭Channel
     *
     * @param channel 端口
     * @return
     */
    public static boolean release(ManagedChannel channel) {
        if (channel != null) {
            //发起一个有组织的关闭，期间已经存在的调用将继续，而新的调用将被立即取消。
            ManagedChannel shutdown = channel.shutdown();
            //发起一个强制的关闭，期间已经存在的调用和新的调用都将被取消。
            //虽然是强制，关闭过程依然不是即可生效;如果在这个方法返回后立即调用 isTerminated() 方法，将可能返回 false 。
            //ManagedChannel managedChannel = channel.shutdownNow();
            //返回channel是否是关闭。关闭的channel立即取消任何新的调用，但是将继续有一些正在处理的调用。
            return shutdown.isShutdown();
        }
        return false;
    }
}
