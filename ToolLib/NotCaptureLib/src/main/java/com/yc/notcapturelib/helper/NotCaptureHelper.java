package com.yc.notcapturelib.helper;

import android.content.Context;

import com.yc.appencryptlib.Rc4EncryptUtils;
import com.yc.eventuploadlib.LoggerReporter;
import com.yc.networklib.net.AddressToolUtils;
import com.yc.notcapturelib.encrypt.EncryptDecryptInterceptor;
import com.yc.notcapturelib.ssl.HttpSslConfig;
import com.yc.notcapturelib.ssl.HttpSslFactory;
import com.yc.notcapturelib.utils.NotCaptureUtils;
import java.net.Proxy;
import okhttp3.OkHttpClient;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2020/11/30
 *    desc   : 代理工具类
 */
public final class NotCaptureHelper {

    private static volatile NotCaptureHelper notCaptureHelper;
    private CaptureConfig config;
    private EncryptDecryptListener encryptDecryptListener;
    private NotCaptureHelper(){
        LoggerReporter.report("NotCaptureHelper" , "init once");
        config = CaptureConfig.builder().build();
        encryptDecryptListener = new EncryptDecryptListener() {
            @Override
            public String encryptData(String key, String data) {
                LoggerReporter.report("NotCaptureHelper" , "decryptData data : " + data);
                String encryptString = Rc4EncryptUtils.encryptString(data, key);
                if (encryptString != null && encryptString.length()>0){
                    LoggerReporter.report("NotCaptureHelper" , "encryptData : " + encryptString);
                    return encryptString;
                }
                return data;
            }

            @Override
            public String decryptData(String key, String data) {
                LoggerReporter.report("NotCaptureHelper" , "decryptData data : " + data);
                String decryptString = Rc4EncryptUtils.decryptString(data, key);
                if (decryptString != null && decryptString.length()>0){
                    LoggerReporter.report("NotCaptureHelper" , "decryptData : " + decryptString);
                    return decryptString;
                }
                return data;
            }
        };
    }

    public static NotCaptureHelper getInstance(){
        if (notCaptureHelper == null){
            synchronized (NotCaptureHelper.class){
                if (notCaptureHelper == null){
                    notCaptureHelper = new NotCaptureHelper();
                }
            }
        }
        return notCaptureHelper;
    }

    public void setConfig(CaptureConfig config) {
        this.config = config;
        LoggerReporter.report("NotCaptureHelper" , "setConfig : " + config);
    }

    public CaptureConfig getConfig() {
        return config;
    }

    public EncryptDecryptListener getEncryptDecryptListener() {
        return encryptDecryptListener;
    }

    public void setEncryptDecryptListener(EncryptDecryptListener encryptDecryptListener) {
        this.encryptDecryptListener = encryptDecryptListener;
    }

    /**
     * 傻瓜式🤪式配置
     * @param context           上下文
     * @param builder           okHttpBuilder
     * @return
     */
    public OkHttpClient.Builder setOkHttp(Context context, OkHttpClient.Builder builder){
        //判断是否代理
        if (config.isProxy() && AddressToolUtils.isWifiProxy(context)){
            //基于抓包原理的基础上，直接使用okHttp禁止代理，经过测试，可以避免第三方工具(比如charles)抓包
            builder.proxy(Proxy.NO_PROXY);
            LoggerReporter.report("NotCaptureHelper" , "setOkHttp 设置避免代理");
        }
        //证书路径检验
        if (config.isCaVerify() && config.getCerPath() != null && config.getCerPath().length()>0){
            HttpSslConfig httpSslConfig = HttpSslFactory.generateSslConfig(NotCaptureUtils.generateSsl(config.getCerPath(),context));
            //设置ssl证书校验
            builder.sslSocketFactory(httpSslConfig.getSslSocketFactory(),httpSslConfig.getTrustManager());
            //自定义了HostnameVerifier。在握手期间，如果 URL 的主机名和服务器的标识主机名不匹配，则验证机制可以回调此接口的实现程序来确定是否应该允许此连接。
            builder.hostnameVerifier(HttpSslFactory.generateUnSafeHostnameVerifier());
            LoggerReporter.report("NotCaptureHelper" , "setOkHttp 设置证书校验和域名校验");
        }
        //设置数据加解密
        if (config.isEncrypt()){
            builder.addInterceptor(new EncryptDecryptInterceptor());
            LoggerReporter.report("NotCaptureHelper" , "setOkHttp 设置数据加解密");
        }
        return builder;
    }

}
