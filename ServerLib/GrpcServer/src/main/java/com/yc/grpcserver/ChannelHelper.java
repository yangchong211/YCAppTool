package com.yc.grpcserver;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.grpc.Channel;
import io.grpc.ClientInterceptor;
import io.grpc.ClientInterceptors;
import io.grpc.ConnectivityState;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;

public final class ChannelHelper {

    private static volatile ChannelHelper instance;
    private final AtomicBoolean isInit = new AtomicBoolean(false);
    private static final String TAG = "gRPC";
    //复用的通道
    private ManagedChannel managedChannel = null;
    //复用的GRPC容器
    private Channel channel = null;

    //锁判断通道是否准备好了
    private AtomicBoolean ready = new AtomicBoolean(false);
    private List<String> serverList = new ArrayList<>();

    public static ChannelHelper getInstance() {
        if (instance == null) {
            synchronized (ChannelHelper.class) {
                if (instance == null) {
                    instance = new ChannelHelper();
                }
            }
        }
        return instance;
    }

    private ChannelHelper() {
        if (isInit.get()) {
            Log.d(TAG , "已经初始化了");
        } else {
            init();
        }
    }

    private void init() {
        isInit.set(true);
    }

    /**
     * 刷新通道
     */
    public synchronized void refreshServer(String server) {
        //传的IP地址非法，则使用旧的通道
        if (!isServerRight(server)) {
            return;
        }
        //锁住，保证通道没创建好之前不给用
        ready.set(false);
        //先释放，在重建
        release();
        //如果是空的，在获取一次
        managedChannel = createManagedChannel(server);
        channel = createChannel(managedChannel);
        ready.set(true);
        notifyWhenStateChanged(managedChannel);
    }

    /**
     * 构建一条普通的Channel
     *
     * @param host 主机服务地址
     * @param port 端口
     * @return
     */
    public ManagedChannel newChannel1(String host, int port) {
        //Channel通过ip和端口注册了一个与Server端连接的通道(Connection)。
        return ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
    }

    public ManagedChannel newChannel2(String host, int port) {
        return GrpcChannelBuilder.build(host, port, null, false, null);
    }

    public ManagedChannel newChannel3(String url) {
        return GrpcChannelBuilder.build(url, null, false, null);
    }


    /**
     * 获取连接状态
     *
     * @param channel channel通道
     * @return
     */
    public ConnectivityState getChannelState(ManagedChannel channel) {
        ConnectivityState state = channel.getState(true);
        return state;
    }

    /**
     * 监听连接状态
     *
     * @param channel channel通道
     */
    public void notifyWhenStateChanged(ManagedChannel channel) {
        Log.d("ChannelHelper",  "通道的状态：" + channel.getState(true).toString());
        channel.notifyWhenStateChanged(channel.getState(true), () -> {
            ConnectivityState state = channel.getState(true);
            Log.d("ChannelHelper", "通道的状态回调：" + state.toString());
        });
    }

    /**
     * @return 是否存活
     */
    public boolean isChannelActive(ManagedChannel managedChannel) {
        //返回channel是否是关闭。关闭的channel立即取消任何新的调用，但是将继续有一些正在处理的调用。
        return managedChannel != null && !managedChannel.isShutdown();
    }

    /**
     * @return 返回channel是否是结束
     */
    public boolean isChannelTerminated(ManagedChannel managedChannel) {
        //返回channel是否是结束。结束的 channel 没有运行中的调用，并且相关的资源已经释放（像TCP连接）。
        return managedChannel != null && !managedChannel.isTerminated();
    }

    /**
     * 关闭Channel
     *
     * @param channel 端口
     * @return
     */
    public boolean shutdown(ManagedChannel channel) {
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
     * @return
     */
    public boolean release() {
        if (managedChannel != null) {
            //发起一个有组织的关闭，期间已经存在的调用将继续，而新的调用将被立即取消。
            ManagedChannel shutdown = managedChannel.shutdown();
            //发起一个强制的关闭，期间已经存在的调用和新的调用都将被取消。
            //虽然是强制，关闭过程依然不是即可生效;如果在这个方法返回后立即调用 isTerminated() 方法，将可能返回 false 。
            //ManagedChannel managedChannel = channel.shutdownNow();
            //返回channel是否是关闭。关闭的channel立即取消任何新的调用，但是将继续有一些正在处理的调用。
            boolean shutdownShutdown = shutdown.isShutdown();
            managedChannel = null;
            return shutdownShutdown;
        }
        return false;
    }

    /**
     * 服务器地址是否合法
     *
     * @param server 服务器地址
     * @return 是否合法
     */
    private boolean isServerRight(String server) {
        //IP地址和端口非法
        //host+port 比如：43.129.34.130:5001
        //target，比如：palm-platform.bbri.io:5000
        //rpc两种网络请求方式，都适合
        return !TextUtils.isEmpty(server) && server.contains(":");
    }

    /**
     * 获取复用的通道
     *
     * @param server 服务器地址，118.89.74.37:5000
     * @return 通道
     */
    public synchronized static ManagedChannel createManagedChannel(String server) {
        ManagedChannel managedChannel = null;
        InputStream certInputString = null;
        try {
            String[] tmp = server.split(":");
            String host = "";
            //需要做处理，不然会崩溃
            int port = 0;
            if (tmp.length >= 2) {
                host = tmp[0];
                //需要做处理，不然会崩溃
                if (TextUtils.isDigitsOnly(tmp[1])) {
                    //纯数字判断
                    port = Integer.parseInt(tmp[1]);
                }
            }
            String serverHostOverride = "POSAPI-GO";
            boolean isIp = GrpcToolUtils.isIP(host);
            if (isIp) {
                managedChannel = GrpcChannelBuilder
                        .build(host, port, serverHostOverride, true, certInputString, null);
            } else {
                managedChannel = GrpcChannelBuilder
                        .build(server, serverHostOverride, true, certInputString, null);
            }
            Log.v("weixiao_network", "获取复用的通道 " + server + " host " + host + " , port " + port);
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("weixiao_network", "获取复用的通道异常 " + e.getMessage());
        }
        return managedChannel;
    }

    /**
     * 创建通道相关
     *
     * @param managedChannel 服务器IP和端口
     */
    public synchronized static Channel createChannel(ManagedChannel managedChannel) {
        if (managedChannel != null) {
            //Metadata是特定RPC请求的信息(比如authentication details)以列表信息的键值对，键是strings,values通常也是strings(但也可以是binary data)。
            Metadata headers = new Metadata();
            HashMap<Metadata.Key<String> , String> mfs = new HashMap<>();
            Set<Metadata.Key<String>> keys = mfs.keySet();
            for (Metadata.Key<String> stringKey : keys) {
                String value = mfs.get(stringKey);
                if (value != null) {
                    headers.put(stringKey, value);
                }
                Log.v("weixiao_network", "设置Header " + value);
            }
            ClientInterceptor interceptor = MetadataUtils.newAttachHeadersInterceptor(headers);
            return ClientInterceptors.intercept(managedChannel, interceptor);
        }
        return null;
    }

}
