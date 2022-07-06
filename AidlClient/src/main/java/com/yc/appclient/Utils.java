package com.yc.appclient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Utils {


    public static String getAidlCheckAppInfoSign(){
        String appPackageName = AppUtils.getAppPackageName();
        //String appPackageName = "cn.ycbjie.ycaudioplayer";
        @SuppressLint("SimpleDateFormat")
        // 设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
        // new Date()为获取当前系统时间，也可使用当前时间戳
        String date = df.format(new Date());
        return encrypt(date+"_"+appPackageName);
    }

    /**
     * MD5加密
     * @param plaintext             明文
     * @return                      密文
     */
    private static String  encrypt(String plaintext) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            byte[] btInput = plaintext.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    public final static String SHA1 = "SHA1";
    /**
     * 返回一个签名的对应类型的字符串
     */
    public static String getSingInfo(Context context, String packageName, String type) {
        String tmp = null;
        Signature[] signs = getSignatures(context, packageName);
        assert signs != null;
        for (Signature sig : signs) {
            if (SHA1.equals(type)) {
                tmp = getSignatureString(sig, SHA1);
                break;
            }
        }
        return tmp;
    }

    /**
     * 返回对应包的签名信息
     * @return
     */
    @SuppressLint("PackageManagerGetSignatures")
    private static Signature[] getSignatures(Context context, String packageName) {
        PackageInfo packageInfo ;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);
            return packageInfo.signatures;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取相应的类型的字符串（把签名的byte[]信息转换成16进制）
     * @return
     */
    private static String getSignatureString(Signature sig, String type) {
        byte[] hexBytes = sig.toByteArray();
        String fingerprint = "error!";
        try {
            MessageDigest digest = MessageDigest.getInstance(type);
            if (digest != null) {
                byte[] digestBytes = digest.digest(hexBytes);
                StringBuilder sb = new StringBuilder();
                for (byte digestByte : digestBytes) {
                    sb.append((Integer.toHexString((digestByte & 0xFF) | 0x100)).substring(1, 3));
                }
                fingerprint = sb.toString();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return fingerprint;
    }



    public static String getSign(String packageName){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm");//设置日期格式
        String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
        String dataMd5= Utils.md5(date+"_"+packageName);
        return dataMd5;
    }

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 ;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


}
