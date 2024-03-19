package com.yc.grpcserver;

import android.net.SSLCertificateSocketFactory;

import com.squareup.okhttp.ConnectionSpec;
import com.squareup.okhttp.TlsVersion;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.annotation.Nullable;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.security.auth.x500.X500Principal;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.okhttp.OkHttpChannelBuilder;

public class GrpcChannelBuilder {

    public static ManagedChannel build(String url, @Nullable String serverHostOverride,
                                       boolean useTls, @Nullable InputStream isCa) {
        ManagedChannelBuilder<?> channelBuilder = ManagedChannelBuilder.forTarget(url)
                //添加日志拦截器
                .intercept(new LoggingInterceptor());
        if (serverHostOverride != null) {
            channelBuilder.overrideAuthority(serverHostOverride);
        }
        if (useTls) {
            try {
                // 创建 SSLContext
                SSLContext sc = SSLContext.getInstance("TLSv1.2");
                sc.init(null, getTrustManagers(isCa), null);
                // 创建 SSLSocketFactory
                Tls12SocketFactory socketFactory = new Tls12SocketFactory(sc.getSocketFactory());
                // 通过 channel 设置 sslSocketFactory做ssl安全处理
                ConnectionSpec.Builder builder = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS);
                ConnectionSpec cs = builder.tlsVersions(TlsVersion.TLS_1_2).build();
                ((OkHttpChannelBuilder) channelBuilder).useTransportSecurity();
                ((OkHttpChannelBuilder) channelBuilder).connectionSpec(cs);

                ((OkHttpChannelBuilder) channelBuilder).sslSocketFactory(socketFactory);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            // 是否使用tls加密，true不使用
            channelBuilder.usePlaintext();
        }
        return channelBuilder.build();
    }


    /**
     * 用来生成ManagedChannel的
     *
     * @param host               ip地址
     * @param port               端口号
     * @param serverHostOverride 这个不知道是啥，是服务器端给我的
     * @param useTls             是否使用tls加密 ，true代表使用https加密，false代表不使用，
     * @param isCa               证书的流
     * @return
     */
    public static ManagedChannel build(String host, int port, @Nullable String serverHostOverride,
                                       boolean useTls, @Nullable InputStream isCa) {
        ManagedChannelBuilder<?> channelBuilder = ManagedChannelBuilder.forAddress(host, port)
                //添加日志拦截器
                .intercept(new LoggingInterceptor());
        if (serverHostOverride != null) {
            channelBuilder.overrideAuthority(serverHostOverride);
        }
        if (useTls) {
            try {
                SSLContext sc = SSLContext.getInstance("TLSv1.2");
                sc.init(null, getTrustManagers(isCa), null);
                ConnectionSpec.Builder builder = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS);
                ConnectionSpec cs = builder.tlsVersions(TlsVersion.TLS_1_2).build();
                ((OkHttpChannelBuilder) channelBuilder).useTransportSecurity();
                ((OkHttpChannelBuilder) channelBuilder).connectionSpec(cs);
                ((OkHttpChannelBuilder) channelBuilder).sslSocketFactory(new Tls12SocketFactory(sc.getSocketFactory()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            // 是否使用tls加密，true不使用
            channelBuilder.usePlaintext();
        }
        return channelBuilder.build();
    }

    /**
     * 用来生成ManagedChannel的
     *
     * @param url                     url域名
     * @param serverHostOverride      服务器端给的
     * @param useTls                  是否使用tls加密 ，true代表使用https加密，false代表不使用，
     * @param isCa                    证书的流
     * @param androidSocketFactoryTls 不知道是啥，传null
     * @return
     */
    public static ManagedChannel build(String url, @Nullable String serverHostOverride,
                                       boolean useTls, @Nullable InputStream isCa,
                                       @Nullable String androidSocketFactoryTls) {
        //构建Channel
        ManagedChannelBuilder<?> channelBuilder = ManagedChannelBuilder.forTarget(url)
                //添加日志拦截器
                .intercept(new LoggingInterceptor());
        if (serverHostOverride != null) {
            channelBuilder.overrideAuthority(serverHostOverride);
        }
        if (useTls) {
            try {
                SSLSocketFactory factory;
                if (androidSocketFactoryTls != null) {
                    factory = getSslCertificateSocketFactory(isCa, androidSocketFactoryTls);
                } else {
                    factory = getSslSocketFactory(isCa);
                }
                ((OkHttpChannelBuilder) channelBuilder).useTransportSecurity();
                ((OkHttpChannelBuilder) channelBuilder).sslSocketFactory(factory);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            // 是否使用tls加密，true不使用
            channelBuilder.usePlaintext();
        }
        return channelBuilder.build();
    }


    /**
     * 用来生成ManagedChannel的
     *
     * @param host                    ip地址
     * @param port                    端口号
     * @param serverHostOverride      服务器端给的
     * @param useTls                  是否使用tls加密 ，true代表使用https加密，false代表不使用，
     * @param isCa                    证书的流
     * @param androidSocketFactoryTls 不知道是啥，传null
     * @return
     */
    public static ManagedChannel build(String host, int port, @Nullable String serverHostOverride,
                                       boolean useTls, @Nullable InputStream isCa,
                                       @Nullable String androidSocketFactoryTls) {
        //构建Channel
        ManagedChannelBuilder<?> channelBuilder = ManagedChannelBuilder.forAddress(host, port)
                //添加日志拦截器
                .intercept(new LoggingInterceptor());
        if (serverHostOverride != null) {
            channelBuilder.overrideAuthority(serverHostOverride);
        }
        if (useTls) {
            try {
                SSLSocketFactory factory;
                if (androidSocketFactoryTls != null) {
                    factory = getSslCertificateSocketFactory(isCa, androidSocketFactoryTls);
                } else {
                    factory = getSslSocketFactory(isCa);
                }
                ((OkHttpChannelBuilder) channelBuilder).useTransportSecurity();
                ((OkHttpChannelBuilder) channelBuilder).sslSocketFactory(factory);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            // 是否使用tls加密，true不使用
            channelBuilder.usePlaintext();
        }
        return channelBuilder.build();
    }

    private static SSLSocketFactory getSslSocketFactory(@Nullable InputStream testCa)
            throws Exception {
        if (testCa == null) {
            return (SSLSocketFactory) SSLSocketFactory.getDefault();
        }
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, getTrustManagers(testCa), null);
        return context.getSocketFactory();
    }

    private static SSLCertificateSocketFactory getSslCertificateSocketFactory(
            @Nullable InputStream testCa, String androidSocketFactoryTls) throws Exception {
        SSLCertificateSocketFactory factory = (SSLCertificateSocketFactory)
                SSLCertificateSocketFactory.getDefault(5000 /* Timeout in ms*/);
        // Use HTTP/2.0
        byte[] h2 = "h2".getBytes();
        byte[][] protocols = new byte[][]{h2};
        if (androidSocketFactoryTls.equals("alpn")) {
            Method setAlpnProtocols =
                    factory.getClass().getDeclaredMethod("setAlpnProtocols", byte[][].class);
            setAlpnProtocols.invoke(factory, new Object[]{protocols});
        } else if (androidSocketFactoryTls.equals("npn")) {
            Method setNpnProtocols =
                    factory.getClass().getDeclaredMethod("setNpnProtocols", byte[][].class);
            setNpnProtocols.invoke(factory, new Object[]{protocols});
        } else {
            throw new RuntimeException("Unknown protocol: " + androidSocketFactoryTls);
        }

        if (testCa != null) {
            factory.setTrustManagers(getTrustManagers(testCa));
        }

        return factory;
    }

    protected static TrustManager[] getTrustManagers(InputStream testCa) throws Exception {
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) cf.generateCertificate(testCa);
        X500Principal principal = cert.getSubjectX500Principal();
        ks.setCertificateEntry(principal.getName("RFC2253"), cert);
        String defaultAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(defaultAlgorithm);
        trustManagerFactory.init(ks);
        return trustManagerFactory.getTrustManagers();
    }
}