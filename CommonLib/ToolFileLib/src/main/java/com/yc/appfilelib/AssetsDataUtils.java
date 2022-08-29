package com.yc.appfilelib;

import android.content.Context;

import java.io.BufferedReader;
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
            InputStream inputStream = context.getAssets().open(fileName);
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
}
