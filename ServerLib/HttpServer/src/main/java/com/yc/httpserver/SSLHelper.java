package com.yc.httpserver;

import android.content.Context;

import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class SSLHelper {

    private static String CLIENT_PRI_KEY = "client.bks";
    private static String TRUSTSTORE_PUB_KEY = "truststore.bks";
    private static String CLIENT_BKS_PASSWORD = "123456";
    private static String TRUSTSTORE_BKS_PASSWORD = "123456";
    private final static String KEYSTORE_TYPE = "BKS";
    private final static String PROTOCOL_TYPE = "TLS";
    private final static String CERTIFICATE_FORMAT = "X509";

    public static SSLSocketFactory getSSLCertifcation(Context context) {
        SSLSocketFactory sslSocketFactory = null;
        try {
            // 服务器端需要验证的客户端证书，其实就是客户端的keystore
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
            // 客户端信任的服务器端证书
            KeyStore trustStore = KeyStore.getInstance(KEYSTORE_TYPE);
            //读取证书
            InputStream ksIn = context.getAssets().open(CLIENT_PRI_KEY);
            InputStream tsIn = context.getAssets().open(TRUSTSTORE_PUB_KEY);
            //加载证书
            keyStore.load(ksIn, CLIENT_BKS_PASSWORD.toCharArray());
            trustStore.load(tsIn, TRUSTSTORE_BKS_PASSWORD.toCharArray());
            ksIn.close();
            tsIn.close();
            SSLContext sslContext = SSLContext.getInstance(PROTOCOL_TYPE);
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(CERTIFICATE_FORMAT);
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(CERTIFICATE_FORMAT);
            trustManagerFactory.init(trustStore);
            keyManagerFactory.init(keyStore, CLIENT_BKS_PASSWORD.toCharArray());
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslSocketFactory;
    }

}
