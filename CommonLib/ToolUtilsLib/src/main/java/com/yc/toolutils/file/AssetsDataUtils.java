package com.yc.toolutils.file;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/11/21
 *     desc  : assets工具类
 *     revise:
 * </pre>
 */
public final class AssetsDataUtils {

    /**
     * 获取asset文件下的资源文件信息
     * @param fileName
     * @return
     */
    public static String getFromAssets(String fileName, Context context) {
        try {
            InputStream inputStream = context.getApplicationContext().getAssets().open(fileName);
            InputStreamReader inputReader = new InputStreamReader(inputStream);
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            StringBuilder result = new StringBuilder();
            while ((line = bufReader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取asset文件下的资源文件信息
     * @param fileName          文件名称
     * @return
     */
    public static String readFromAssets(String fileName, Context context) {
        //通过设备管理对象 获取Asset的资源路径
        AssetManager assetManager = context.getApplicationContext().getAssets();
        InputStream inputStream = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        StringBuilder sb =  new StringBuilder();
        try{
            inputStream = assetManager.open(fileName);
            isr = new InputStreamReader(inputStream);
            br = new BufferedReader(isr);
            sb.append(br.readLine());
            String line;
            while((line = br.readLine()) != null){
                sb.append("\n").append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
