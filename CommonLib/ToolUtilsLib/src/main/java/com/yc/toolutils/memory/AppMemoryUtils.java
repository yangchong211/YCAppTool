package com.yc.toolutils.memory;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Debug;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StatFs;
import android.text.TextUtils;
import android.text.format.Formatter;


import com.yc.appcommoninter.report.ExceptionReporter;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2018/5/6
 *     desc  : 内存相关工具类
 *     revise: 所有结果以KB为单位
 * </pre>
 */
public final class AppMemoryUtils {
    /**
     * 获取当前应用进程的pid
     */
    public static int getCurrentPid() {
        return android.os.Process.myPid();
    }

    /**
     * 获取总体内存使用情况
     */
    public static void getMemoryInfo(final Context context, final OnGetMemoryInfoCallback onGetMemoryInfoCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String pkgName = context.getPackageName();
                final int pid = getCurrentPid();
                ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                final ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
                am.getMemoryInfo(mi);
                //1. ram
                final RamMemoryInfo ramMemoryInfo = new RamMemoryInfo();
                // 获取内存剩余大小（以字节为单位）
                ramMemoryInfo.availMem = mi.availMem;
                //是否低内存状态运行
                ramMemoryInfo.isLowMemory = mi.lowMemory;
                // 获取内存阈值（以字节为单位）
                ramMemoryInfo.lowMemThreshold = mi.threshold ;
                //获取内存总大小（以字节为单位）
                ramMemoryInfo.totalMem = AppMemoryUtils.getRamTotalMemSync(context);



                //2. pss
                final PssInfo pssInfo = AppMemoryUtils.getAppPssInfo(context, pid);


                //3. dalvik heap
                final DalvikHeapMem dalvikHeapMem = AppMemoryUtils.getAppDalvikHeapMem();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        onGetMemoryInfoCallback.onGetMemoryInfo(pkgName, pid, ramMemoryInfo, pssInfo, dalvikHeapMem);
                    }
                });
            }
        }).start();
    }

    /**
     * 获取应用实际进程占用内存
     * 一般而言total是大于nativ+dalvik的，因为它包含了共享内存，理论上我们只关心native跟dalvik就行
     *
     * @return 应用pss信息KB
     */
    private static PssInfo getAppPssInfo(Context context, int pid) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        Debug.MemoryInfo memoryInfo = am.getProcessMemoryInfo(new int[]{pid})[0];
        PssInfo pssInfo = new PssInfo();
        //通过memoryInfo对象的getTotalPss()方法获取到进程的总内存大小。需要注意的是，内存大小以KB为单位。
        //返回总的PSS内存使用量(以kB为单位)
        pssInfo.totalPss = memoryInfo.getTotalPss();
        //dalvik堆的比例设置大小
        pssInfo.dalvikPss = memoryInfo.dalvikPss;
        //本机堆的比例设置大小
        pssInfo.nativePss = memoryInfo.nativePss;
        //比例设置大小为其他所有
        pssInfo.otherPss = memoryInfo.otherPss;
        return pssInfo;
    }

    /**
     * 获取应用dalvik内存信息
     *
     * @return dalvik堆内存KB
     */
    private static DalvikHeapMem getAppDalvikHeapMem() {
        Runtime runtime = Runtime.getRuntime();
        DalvikHeapMem dalvikHeapMem = new DalvikHeapMem();
        //空闲内存
        dalvikHeapMem.freeMem = runtime.freeMemory();
        //最大内存
        dalvikHeapMem.maxMem = Runtime.getRuntime().maxMemory();
        //activityManager.getMemoryClass() 获取应用能够获取的max dalvik堆内存大小
        //已用内存
        dalvikHeapMem.allocated = (Runtime.getRuntime().totalMemory() - runtime.freeMemory());
        return dalvikHeapMem;
    }

    /**
     * 内存相关的所有数据
     */
    public interface OnGetMemoryInfoCallback {
        void onGetMemoryInfo(String pkgName, int pid, RamMemoryInfo ramMemoryInfo,
                             PssInfo pssInfo, DalvikHeapMem dalvikHeapMem);
    }

    private interface OnGetRamTotalMemCallback {
        //手机总RAM容量/KB
        void onGetRamTotalMem(long totalMem);
    }

    /**
     * 同步获取系统的总ram大小
     */
    private static long getRamTotalMemSync(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            am.getMemoryInfo(mi);
            return mi.totalMem;
        } else if (sTotalMem.get() > 0L) {
            //如果已经从文件获取过值，则不需要再次获取
            return sTotalMem.get();
        } else {
            final long tm = getRamTotalMemByFile();
            sTotalMem.set(tm);
            return tm;
        }
    }

    private static final AtomicLong sTotalMem = new AtomicLong(0L);

    /**
     * 获取手机的RAM容量，其实和activityManager.getMemoryInfo(mi).totalMem效果一样，
     * 也就是说，在API16以上使用系统API获取，低版本采用这个文件读取方式
     *
     * @return 容量KB
     */
    private static long getRamTotalMemByFile() {
        final String dir = "/proc/meminfo";
        try {
            FileReader fr = new FileReader(dir);
            BufferedReader br = new BufferedReader(fr, 2048);
            String memoryLine = br.readLine();
            String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
            br.close();
            long totalMemorySize = Integer.parseInt(
                    subMemoryLine.replaceAll("\\D+", ""));
            return totalMemorySize;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0L;
    }



    /**
     * 格式化单位
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    /**
     * 获得机身可用内存
     *
     * @return
     */
    public static String getRomAvailableSize(Context context) {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(context, blockSize * availableBlocks);
    }

    /**
     * 获取机身控件
     * @param context       上下文
     * @return
     */
    public static String getRomSpace(Context context) {
        try {
            String free = getRomAvailableSize(context);
            String total = getRomTotalSize(context);
            return free + "/" + total;
        } catch (Exception e) {
            return "-/-";
        }
    }

    /**
     * 获得机身内存总大小
     *
     * @return
     */
    public static String getRomTotalSize(Context context) {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(context, blockSize * totalBlocks);
    }

    /**
     * 手机总内存
     * @param context
     * @return 手机总内存(兆)
     */
    public static long getTotalMemory(Context context) {
        // 系统内存信息文件
        String str1 = "/proc/meminfo";
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();
            // 读取meminfo第一行，系统总内存大小
            if (!TextUtils.isEmpty(str2)) {
                arrayOfString = str2.split("\\s+");
                // 获得系统总内存，单位是KB，乘以1024转换为Byte
                initial_memory = Integer.valueOf(arrayOfString[1]).intValue() / 1024;
            }
            localBufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Byte转换为KB或者MB，内存大小规格化
        return initial_memory;
    }


    /**
     * 通过ObjectOutputStream写入对象的方式获取对象类型签名和对象数据占用存储空间大小
     * 该大小与对象内存布局中占用的大小不同，可以用于预估对象内存布局占用的存储空间
     * @param object
     * @return
     */
    public static int getObjectSize(Serializable object){
        int objSize = 16;
        if(object == null){
            return objSize;
        }
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.flush();
            byte[] byteArray = baos.toByteArray();
            if(byteArray != null){
                objSize = byteArray.length;
            }
        }catch (IOException ioe){
            ExceptionReporter.report(ioe);
        }finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            }catch (Throwable t){
                ExceptionReporter.report(t);
            }
            try{
                if (oos != null) {
                    oos.close();
                }
            }catch (Throwable t){
                ExceptionReporter.report(t);
            }
        }
        return objSize;
    }
}
