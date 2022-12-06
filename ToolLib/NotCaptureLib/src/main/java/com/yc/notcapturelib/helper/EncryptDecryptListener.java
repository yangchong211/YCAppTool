package com.yc.notcapturelib.helper;

/**
 * @author yangchong
 * GitHub : https://github.com/yangchong211/YCAppTool
 * time   : 2020/11/30
 * desc   : 加密和解密自定义接口，外部开发者可以自由实现自己的加密和解密方式
 */
public interface EncryptDecryptListener {

    /**
     * 加密数据
     *
     * @param key  key
     * @param data 加密内容
     * @return
     */
    String encryptData(String key, String data);

    /**
     * 解密数据
     *
     * @param key  key
     * @param data 加密内容
     * @return
     */
    String decryptData(String key, String data);
}
