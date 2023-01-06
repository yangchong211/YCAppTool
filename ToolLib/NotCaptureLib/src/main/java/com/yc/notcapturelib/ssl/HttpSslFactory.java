package com.yc.notcapturelib.ssl;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * @author yangchong
 * GitHub : https://github.com/yangchong211/YCAppTool
 * time   : 2020/11/30
 * desc   : Https 证书校验工厂
 */
public final class HttpSslFactory {

    private final static String KEYSTORE_TYPE = "BKS";
    private final static String PROTOCOL_TYPE = "TLS";
    private final static String CERTIFICATE_FORMAT = "X.509";

    /**
     * 生成信任任何证书的配置
     */
    public static HttpSslConfig generateSslConfig() {
        return generateSslConfigBase(null, null, null);
    }

    /**
     * https 单向认证
     */
    public static HttpSslConfig generateSslConfig(X509TrustManager trustManager) {
        return generateSslConfigBase(trustManager, null, null);
    }

    /**
     * https 单向认证
     */
    public static HttpSslConfig generateSslConfig(InputStream... certificates) {
        return generateSslConfigBase(null, null, null, certificates);
    }

    /**
     * https 双向认证
     */
    public static HttpSslConfig generateSslConfig(InputStream bksFile, String password,
                                                  InputStream... certificates) {
        return generateSslConfigBase(null, bksFile, password, certificates);
    }

    /**
     * https 双向认证
     */
    public static HttpSslConfig generateSslConfig(InputStream bksFile, String password,
                                                  X509TrustManager trustManager) {
        return generateSslConfigBase(trustManager, bksFile, password);
    }

    /**
     * 生成认证配置
     *
     * @param trustManager 可以额外配置信任服务端的证书策略，否则默认是按CA证书去验证的，若不是CA可信任的证书，则无法通过验证
     * @param bksFile      客户端使用 bks 证书校验服务端证书
     * @param password     客户端的 bks 证书密码
     * @param certificates 用含有服务端公钥的证书校验服务端证书
     */
    private static HttpSslConfig generateSslConfigBase(X509TrustManager trustManager,
                                                       InputStream bksFile,
                                                       String password,
                                                       InputStream... certificates) {
        try {
            KeyManager[] keyManagers = prepareKeyManager(bksFile, password);
            TrustManager[] trustManagers = prepareTrustManager(certificates);
            X509TrustManager manager;
            if (trustManager != null) {
                // 优先使用用户自定义的 TrustManager
                manager = trustManager;
            } else if (trustManagers != null) {
                // 然后使用默认的 TrustManager
                manager = chooseTrustManager(trustManagers);
            } else {
                // 否则使用不安全的 TrustManager
                manager = new UnSafeTrustManager();
            }
            // 创建 TLS 类型的 SsLContext 对象，使用我们的 TrustManager
            SSLContext sslContext = SSLContext.getInstance(PROTOCOL_TYPE);
            // 用上面得到的 TrustManagers 初始化 SsLContext，这样 SslContext 就会信任keyStore中的证书
            // 第一个参数是授权的密钥管理器，用来授权验证，比如授权自签名的证书验证。第二个是被授权的证书管理器，用来验证服务器端的证书
            sslContext.init(keyManagers, new TrustManager[]{manager}, null);
            // 通过 SslContext 获取 SSLSocketFactory 对象
            SSLSocketFactory socketFactory = sslContext.getSocketFactory();
            return new HttpSslConfig(socketFactory, manager);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new AssertionError(e);
        }
    }

    private static KeyManager[] prepareKeyManager(InputStream bksFile, String password) {
        try {
            if (bksFile == null || password == null) {
                return null;
            }
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
            keyStore.load(bksFile, password.toCharArray());
            String defaultAlgorithm = KeyManagerFactory.getDefaultAlgorithm();
            KeyManagerFactory factory = KeyManagerFactory.getInstance(defaultAlgorithm);
            factory.init(keyStore, password.toCharArray());
            return factory.getKeyManagers();
        } catch (IOException | CertificateException | UnrecoverableKeyException
                | NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过io流读取证书的数据
     * @param certificates          ca证书流对象
     * @return
     */
    private static TrustManager[] prepareTrustManager(InputStream... certificates) {
        if (certificates == null || certificates.length <= 0) {
            return null;
        }
        try {
            //获取X.509格式的内置证书
            CertificateFactory certificateFactory = CertificateFactory.getInstance(CERTIFICATE_FORMAT);
            // 创建一个默认类型的 KeyStore，存储我们信任的证书
            KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certStream : certificates) {
                String certificateAlias = Integer.toString(index++);
                // 证书工厂根据证书文件的流生成证书 Cert
                Certificate cert = certificateFactory.generateCertificate(certStream);
                // 将 Cert 作为可信证书放入到 KeyStore 中
                keyStore.setCertificateEntry(certificateAlias, cert);
                try {
                    if (certStream != null) {
                        certStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 我们创建一个默认类型的 TrustManagerFactory
            String defaultAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            // 通过TrustManager工厂类
            TrustManagerFactory factory = TrustManagerFactory.getInstance(defaultAlgorithm);
            // 用我们之前的 KeyStore 实例初始化 TrustManagerFactory，这样 tmf 就会信任 KeyStore 中的证书
            factory.init(keyStore);
            // 通过 tmf 获取 TrustManager 数组，TrustManager 也会信任 KeyStore 中的证书
            return factory.getTrustManagers();
        } catch (IOException | CertificateException | KeyStoreException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static X509TrustManager chooseTrustManager(TrustManager[] trustManagers) {
        for (TrustManager trustManager : trustManagers) {
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager) trustManager;
            }
        }
        return null;
    }

    public static HostnameVerifier generateUnSafeHostnameVerifier() {
        return new UnSafeHostnameVerifier();
    }
}