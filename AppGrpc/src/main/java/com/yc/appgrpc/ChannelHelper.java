package com.yc.appgrpc;

import java.util.concurrent.TimeUnit;

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
    public static ManagedChannel newChannel(String host, int port) {
        return ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
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
