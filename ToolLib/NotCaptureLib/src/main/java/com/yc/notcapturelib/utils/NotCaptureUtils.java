package com.yc.notcapturelib.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.InputStream;
import java.util.LinkedList;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2020/11/30
 *    desc   : 代理工具类
 */
public final class NotCaptureUtils {

    /**
     * 获取证书数据
     * @param fileName
     * @param context
     * @return
     */
    public static InputStream[] generateSsl(String fileName, Context context){
        LinkedList<InputStream> list = NotCaptureUtils.getFromAssets(fileName, context);
        //创建一个新的String类型数组
        InputStream[] arr = new InputStream[list.size()];
        //将LinkedList转换为字符串数组
        list.toArray(arr);
        return arr;
    }

    /**
     * 获取asset文件下的资源文件信息
     * @param fileName
     * @return
     */
    public static LinkedList<InputStream> getFromAssets(String fileName, Context context) {
        LinkedList<InputStream> inputStreams = new LinkedList<>();
        try {
            AssetManager assetManager = context.getApplicationContext().getAssets();
            if (assetManager == null){
                return inputStreams;
            }
            String[] list = assetManager.list(fileName);
            if (list != null && list.length > 0){
                for (int i= 0 ; i<list.length ; i++){
                    String cer = list[i];
                    InputStream inputStream = assetManager.open(fileName + cer);
                    inputStreams.add(inputStream);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStreams;
    }


}
