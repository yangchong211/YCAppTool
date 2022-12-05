package com.yc.notcapturelib.ssl;

import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2020/11/30
 *    desc   : 信任所有证书，校验请求地址
 */
public class TrustAllCertsManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }


    public static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        TrustAllCertsManager trustAllCertsManager = new TrustAllCertsManager();
        SecureRandom secureRandom = new SecureRandom();
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{trustAllCertsManager}, secureRandom);
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }

    public static class TrustAllHostnameVerifier implements HostnameVerifier {
        private String serverUrl;

        public TrustAllHostnameVerifier(String hostname) {
            serverUrl = hostname;
        }

        @Override
        public boolean verify(String hostname, SSLSession session) {
            if (serverUrl.contains(hostname)) {
                return true;
            }
            return false;
        }
    }

    public static X509TrustManager createX509TrustManager() {
        TrustManagerFactory trustManagerFactory = null;
        X509TrustManager trustManager = null;
        String defaultAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        try {
            trustManagerFactory = TrustManagerFactory.getInstance(defaultAlgorithm);
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:"
                        + Arrays.toString(trustManagers));
            }
            trustManager = (X509TrustManager) trustManagers[0];
        } catch (NoSuchAlgorithmException e) {
        } catch (Exception e) {
        }
        return trustManager;
    }
}
