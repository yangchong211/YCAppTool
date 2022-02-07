package com.yc.toollib.crash;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
public final class MemoryUtils {

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
                final int pid = android.os.Process.myPid();
                ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                final ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
                am.getMemoryInfo(mi);
                //1. ram
                final RamMemoryInfo ramMemoryInfo = new RamMemoryInfo();
                ramMemoryInfo.availMem = mi.availMem;
                ramMemoryInfo.isLowMemory = mi.lowMemory;
                ramMemoryInfo.lowMemThreshold = mi.threshold ;
                ramMemoryInfo.totalMem = MemoryUtils.getRamTotalMemSync(context);
                //2. pss
                final PssInfo pssInfo = MemoryUtils.getAppPssInfo(context, pid);
                //3. dalvik heap
                final DalvikHeapMem dalvikHeapMem = MemoryUtils.getAppDalvikHeapMem();
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
     * 获取手机RAM的存储情况
     */
    public static void getSystemRam(final Context context, final OnGetRamMemoryInfoCallback onGetRamMemoryInfoCallback) {
        getRamTotalMem(context, new OnGetRamTotalMemCallback() {
            @Override
            public void onGetRamTotalMem(long totalMem) {
                ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                final ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
                am.getMemoryInfo(mi);
                RamMemoryInfo ramMemoryInfo = new RamMemoryInfo();
                ramMemoryInfo.availMem = mi.availMem;
                ramMemoryInfo.isLowMemory = mi.lowMemory;
                ramMemoryInfo.lowMemThreshold = mi.threshold;
                ramMemoryInfo.totalMem = totalMem;
                onGetRamMemoryInfoCallback.onGetRamMemoryInfo(ramMemoryInfo);
            }
        });
    }

    /**
     * 获取应用实际占用内存
     *
     * @return 应用pss信息KB
     */
    public static PssInfo getAppPssInfo(Context context, int pid) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        Debug.MemoryInfo memoryInfo = am.getProcessMemoryInfo(new int[]{pid})[0];
        PssInfo pssInfo = new PssInfo();
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
    public static DalvikHeapMem getAppDalvikHeapMem() {
        Runtime runtime = Runtime.getRuntime();
        DalvikHeapMem dalvikHeapMem = new DalvikHeapMem();
        //空闲内存
        dalvikHeapMem.freeMem = runtime.freeMemory();
        //最大内存
        dalvikHeapMem.maxMem = Runtime.getRuntime().maxMemory();
        //已用内存
        dalvikHeapMem.allocated = (Runtime.getRuntime().totalMemory() - runtime.freeMemory());
        return dalvikHeapMem;
    }

    /**
     * 获取应用能够获取的max dalvik堆内存大小
     * 和Runtime.getRuntime().maxMemory()一样
     *
     * @return 单位M
     */
    public static long getAppTotalDalvikHeapSize(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        return manager.getMemoryClass();
    }

    /**
     * Dalvik堆内存，只要App用到的内存都算（包括共享内存）
     */
    public static class DalvikHeapMem {
        public long freeMem;
        public long maxMem;
        public long allocated;
    }

    /**
     * 应用实际占用内存（共享按比例分配）
     */
    public static class PssInfo {
        public int totalPss;
        public int dalvikPss;
        public int nativePss;
        public int otherPss;
    }

    /**
     * 手机RAM内存信息
     * 物理内存信息
     */
    public static class RamMemoryInfo {
        //可用RAM
        public long availMem;
        //手机总RAM
        public long totalMem;
        //内存占用满的阀值，超过即认为低内存运行状态，可能会Kill process
        public long lowMemThreshold;
        //是否低内存状态运行
        public boolean isLowMemory;
    }

    /**
     * 内存相关的所有数据
     */
    public interface OnGetMemoryInfoCallback {
        void onGetMemoryInfo(String pkgName, int pid, RamMemoryInfo ramMemoryInfo, PssInfo pssInfo, DalvikHeapMem dalvikHeapMem);
    }

    public interface OnGetRamMemoryInfoCallback {
        void onGetRamMemoryInfo(RamMemoryInfo ramMemoryInfo);
    }

    private interface OnGetRamTotalMemCallback {
        //手机总RAM容量/KB
        void onGetRamTotalMem(long totalMem);
    }

    /**
     * 获取手机RAM容量/手机实际内存
     * 单位
     */
    private static void getRamTotalMem(final Context context, final OnGetRamTotalMemCallback onGetRamTotalMemCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final long totalRam = getRamTotalMemSync(context);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        onGetRamTotalMemCallback.onGetRamTotalMem(totalRam);
                    }
                });
            }
        }).start();
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
        } else if (sTotalMem.get() > 0L) {//如果已经从文件获取过值，则不需要再次获取
            return sTotalMem.get();
        } else {
            final long tm = getRamTotalMemByFile();
            sTotalMem.set(tm);
            return tm;
        }
    }

    private static AtomicLong sTotalMem = new AtomicLong(0L);

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
            long totalMemorySize = Integer.parseInt(subMemoryLine.replaceAll("\\D+", ""));
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

}
