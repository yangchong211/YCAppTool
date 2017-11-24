package com.ns.yc.lifehelper.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.PowerManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.ns.yc.lifehelper.base.BaseApplication;

import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by PC on 2017/2/22.
 * 作者：PC
 */

public class AppUtil {


    /**
     * 实现文本复制功能
     * @param content 复制的文本
     */
    public static void copy(String content) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) BaseApplication.getInstance().getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    /**
     * 使用外部浏览器打开链接
     * @param context
     * @param content
     */
    public static void openLink(Context context, String content) {
        Uri issuesUrl = Uri.parse(content);
        Intent intent = new Intent(Intent.ACTION_VIEW, issuesUrl);
        context.startActivity(intent);
    }


    /**
     * 设置某个View的margin
     *
     * @param view   需要设置的view
     * @param isDp   需要设置的数值是否为DP
     * @param left   左边距
     * @param right  右边距
     * @param top    上边距
     * @param bottom 下边距
     * @return
     */
    public static ViewGroup.LayoutParams setViewMargin(View view, boolean isDp, int left, int right, int top, int bottom) {
        if (view == null) {
            return null;
        }

        int leftPx = left;
        int rightPx = right;
        int topPx = top;
        int bottomPx = bottom;
        ViewGroup.LayoutParams params = view.getLayoutParams();
        ViewGroup.MarginLayoutParams marginParams = null;
        //获取view的margin设置参数
        if (params instanceof ViewGroup.MarginLayoutParams) {
            marginParams = (ViewGroup.MarginLayoutParams) params;
        } else {
            //不存在时创建一个新的参数
            marginParams = new ViewGroup.MarginLayoutParams(params);
        }

        //根据DP与PX转换计算值
        if (isDp) {
            leftPx = dip2px(left);
            rightPx = dip2px(right);
            topPx = dip2px(top);
            bottomPx = dip2px(bottom);
        }
        //设置margin
        marginParams.setMargins(leftPx, topPx, rightPx, bottomPx);
        view.setLayoutParams(marginParams);
        view.requestLayout();
        return marginParams;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = BaseApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 创建缓存key
     * @param param
     * @return
     */
    public static String createCacheKey(Object... param) {
        String key = "";
        for (Object o : param) {
            key += "-" + o;
        }
        return key.replaceFirst("-","");
    }

    /**
     * 格式化
     * @param wordCount
     * @return
     */
    public static String formatWordCount(int wordCount) {
        if (wordCount / 10000 > 0) {
            return (int) ((wordCount / 10000f) + 0.5) + "万字";
        } else if (wordCount / 1000 > 0) {
            return (int) ((wordCount / 1000f) + 0.5) + "千字";
        } else {
            return wordCount + "字";
        }
    }


    /**
     * 判断本地是否存在某个APP
     * @param context       上下文
     * @param pkgName       应用的包名
     * @return              true：该app存在；false：该app不存在
     */
    public static boolean isPkgInstalled(Context context, String pkgName) {
        PackageInfo packageInfo ;
        try {
            synchronized (context){
                packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
            }
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断相对应的APP是否存在
     * @param context       上下文
     * @param packageName   (包名)(若想判断QQ，则改为com.tencent.mobileqq，若想判断微信，则改为com.tencent.mm)
     * @return              true：该app存在；false：该app不存在
     */
    public boolean isAvilible(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        //获取手机系统的所有APP包名，然后进行一一比较
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (((PackageInfo) pinfo.get(i)).packageName.equalsIgnoreCase(packageName)){
                return true;
            }
        }
        return false;
    }


    /**
     * 设置页面的透明度
     * 主要作用于：弹窗时设置宿主Activity的背景色
     * @param bgAlpha 1表示不透明
     */
    public static void setBackgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        Window window = activity.getWindow();
        if(window!=null){
            if (bgAlpha == 1) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
            }
            window.setAttributes(lp);
        }
    }

    /**
     * 设置页面的昏暗度
     * 主要作用于：弹窗时设置宿主Activity的背景色
     * @param bgDimAmount
     */
    public static void setBackgroundDimAmount(Activity activity, float bgDimAmount){
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.dimAmount = bgDimAmount;
        Window window = activity.getWindow();
        if(window!=null){
            if(bgDimAmount == 1){
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }else {
                window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
            window.setAttributes(lp);
        }
    }

    /**
     * 背景模糊
     * 主要作用于：activity页面，建议不要用这种方式，可以使用模糊视图自定义控件【毛玻璃效果】
     * @param
     */
    public static void setBackgroundDimAmount(Activity activity, View view, int bgAlpha){
        Window window = activity.getWindow();
        if(window!=null){
            window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
            view.getBackground().setAlpha(bgAlpha);     //0~255透明度值 ，0为完全透明，255为不透明
        }
    }


    /**
     * 判断某Activity是否挂掉，主要是用于弹窗
     * @param activity
     * @return
     */
    public static boolean isActivityLiving(Activity activity) {
        if (activity == null) {
            Log.d("wisely", "activity == null");
            return false;
        }
        if (activity.isFinishing()) {
            Log.d("wisely", "activity is finishing");
            return false;
        }
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {//android 4.2
            if (activity.isDestroyed()) {
                Log.d("wisely", "activity is destroy");
                return false;
            }
        }*/
        String name = activity.getClass().getName();
        Log.d("wisely",name+"---");
        Log.d("wisely", "activity is living");
        return true;
    }

    /**
     * 判断某Activity是否挂掉，主要是用于弹窗
     */
    private static boolean isActivityLiving(WeakReference<Activity> weakReference) {
        if(weakReference != null){
            Activity activity = weakReference.get();
            if (activity == null) {
                return false;
            }
            if (activity.isFinishing()) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 格式化毫秒值，如果这个毫秒值包含小时，则格式为时分秒，如:01:30:58，如果不包含小时，则格式化为分秒，如：30:58
     * @param duration
     * @return
     */
    public static CharSequence formatMillis(long duration) {
        Calendar calendar = Calendar.getInstance(); // 以当前系统时间创建一个日历
        calendar.clear();   // 清空时间，变成1970 年 1 月 1 日 00:00:00
        calendar.add(Calendar.MILLISECOND, (int) duration);// 1970 年 1 月 1 日 01:58:32
        boolean hasHour = duration / (1 * 60 * 60 * 1000) > 0;    // 判断毫秒值有没有包含小时
        CharSequence inFormat = hasHour ? "kk:mm:ss" : "mm:ss";   // 如果包含小时，则格式化为时:分:秒，否则格式化为分:秒
        return DateFormat.format(inFormat, calendar);
    }

    /**
     * 判断应用是否处于后台
     * @param context
     * @return
     */
    public static boolean isAppOnBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 吐司工具类    避免点击多次导致吐司多次，最后导致Toast就长时间关闭不掉了
     * @param context       注意：这里如果传入context会报内存泄漏
     * @param content
     */
    private static Toast toast;
    public static void showToast(Context context, String content) {
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }


    /**
     * 判断是否锁屏
     * @param context
     * @return
     */
    public static boolean isLockScreen(Context context){
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();       //如果为true，则表示屏幕“亮”了，否则屏幕“暗”了。
        if (isScreenOn){
            return false;
        } else {
            return true;
        }
    }

    public static String hashKey(String key) {
        String hashKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            hashKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            hashKey = String.valueOf(key.hashCode());
        }
        return hashKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

}
