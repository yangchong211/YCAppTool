package com.ycbjie.library.utils;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.text.TextUtils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * <pre>
 *     @author yangchong
 *     blog  :
 *     time  : 2018/03/28
 *     desc  : SD卡工具类
 *     revise:
 * </pre>
 */
public final class SDUtils {


    /**
     * app保存路径
     */
    private final static String APP_ROOT_SAVE_PATH = "lifeHelper";
    private final static String property = File.separator;


    /**
     * 系统保存文件目录
     * @param name                  自己命名
     * @return                      路径
     */
    public static String getLocalRootSavePathDir(Context context , String name){
        //获得SDCard 的路径,storage/sdcard
        String sdPath = SDUtils.getSDCardPath();
        //判断 SD 卡是否可用
        if (!SDUtils.isSDCardEnable(context) || TextUtils.isEmpty(sdPath)) {
            //获取 SD 卡路径
            List<String> sdPathList = SDUtils.getSDCardPaths(context);
            if (sdPathList != null && sdPathList.size() > 0 && !TextUtils.isEmpty(sdPathList.get(0))) {
                sdPath = sdPathList.get(0);
            }
        }
        if (TextUtils.isEmpty(sdPath)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(sdPath);
        sb.append(property);
        sb.append(APP_ROOT_SAVE_PATH);
        //如果name不为空，那么通过该方法是获取到文件的路径
        //否则则是获取文件夹的路径
        if(name!=null && name.length()>0){
            sb.append(property);
            sb.append(name);
        }
        return sb.toString();
    }



    /**
     * 判断SDCard是否挂载
     * Environment.MEDIA_MOUNTED,表示SDCard已经挂载
     * Environment.getExternalStorageState()，获得当前SDCard的挂载状态
     */
    private static boolean isMounted() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }


    /**
     * 获得SDCard 的路径,storage/sdcard
     * @return          路径
     */
    public static String getSDCardPath() {
        String path = null;
        if (isMounted()) {
            path = Environment.getExternalStorageDirectory().getPath();
        }
        return path;
    }


    /**
     * 判断 SD 卡是否可用
     *
     * @return true : 可用<br>false : 不可用
     */
    public static boolean isSDCardEnable(Context context) {
        return !getSDCardPaths(context).isEmpty();
    }

    /**
     * 获取 SD 卡路径
     *
     * @return SD 卡路径
     */
    @SuppressWarnings("TryWithIdenticalCatches")
    public static List<String> getSDCardPaths(Context context) {
        StorageManager storageManager = (StorageManager) context.getApplicationContext()
                .getSystemService(Context.STORAGE_SERVICE);
        List<String> paths = new ArrayList<>();
        try {
            Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths");
            getVolumePathsMethod.setAccessible(true);
            Object invoke = getVolumePathsMethod.invoke(storageManager);
            paths = Arrays.asList((String[]) invoke);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return paths;
    }

}
