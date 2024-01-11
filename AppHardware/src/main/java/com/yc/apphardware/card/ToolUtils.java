package com.yc.apphardware.card;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;

import com.yc.appcontextlib.AppToolUtils;
import com.yc.apphardware.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UnknownFormatConversionException;

/**
 * @author: Administrator
 * @date: 2023/10/10
 */

public class ToolUtils {

    //播放beep
    private static MediaPlayer mediaPlayer;
    public static void playBeep() {
        if(mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(AppToolUtils.getApp(), R.raw.beep);
        }
        if(mediaPlayer != null) {
            mediaPlayer.setLooping(false);
            mediaPlayer.start();
        }
    }
    //释放MediaPlayer资源
    public static void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public static void toast(String format, Object... args) {
        if (null != args && args.length > 0) {
            try {
                format = String.format(format, args);
            } catch (UnknownFormatConversionException ignored) {

            }
        }
        final String msg = format;
        new Handler(Looper.getMainLooper()).post(() ->  toast(msg));
    }

    //生成随机数
    public static String generateNonce(int len) {
        String validChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom secureRandom = new SecureRandom();

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < len; i++) {
            int index = secureRandom.nextInt(validChars.length());
            sb.append(validChars.charAt(index));
        }
        return sb.toString();
    }

    //bytes转字符
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    //隐藏字串
    public static String stringMask(String string) {
        if (string == null || string.length() == 1) {
            return string;
        }

        int middleIndex = string.length() / 2;
        if(middleIndex <= 3) {
            return string.substring(0, middleIndex) + "*" + (string.length() == 2 ? "" : string.substring(middleIndex + 1));
        } else {
            return string.substring(0, 3) + " * " + string.substring(string.length() - 2);
        }
    }


    //按字典排序
    public static String generateSignature(HashMap<String, String> params) {
        //使用TreeMap对参数按字典排序
        Map<String, String> sortedParams = new TreeMap<>(params);

        //将排序后的参数拼接成字符串
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
            sb.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    //MD5加密并转大写
    public static String encryptToMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());

            // 转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                hexString.append(String.format("%02x", b));
            }
            // 转换为大写
            return hexString.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // 处理异常，比如输出日志或抛出自定义异常
        }
        return null;
    }
}