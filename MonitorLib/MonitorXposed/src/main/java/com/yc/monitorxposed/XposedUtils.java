package com.yc.monitorxposed;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.yc.appcontextlib.AppToolUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import dalvik.system.BaseDexClassLoader;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : Xposed环境 工具类
 *     revise:
 * </pre>
 */
public final class XposedUtils {

    private static final String XPOSED_HELPERS = "de.robv.android.xposed.XposedHelpers";
    private static final String XPOSED_BRIDGE = "de.robv.android.xposed.XposedBridge";
    private static final String XPOSED_INSTALLER = "de.robv.android.xposed.installer";

    /**
     * 第一种：获取当前设备所有运行的APP，根据安装包名对应用进行检测判断是否有Xposed环境。
     *
     * @return
     */
    public boolean isXposedExistsApk() {
        PackageManager packageManager = AppToolUtils.getApp().getPackageManager();
        List<ApplicationInfo> applicationInfoList = packageManager
                .getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo item : applicationInfoList) {
            if (item.packageName.equals(XPOSED_INSTALLER)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 第三种：通过ClassLoader检查是否已经加载了XposedBridge类和XposedHelpers类来检测
     *
     * @return
     */
    @Deprecated
    public boolean isXposedExists() {
        try {
            ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
            //调用 loadClass 加载类
            Class<?> aClass = systemClassLoader.loadClass(XPOSED_HELPERS);
            Object xpHelperObj = aClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        try {
            ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
            //调用 loadClass 加载类
            Class<?> aClass = systemClassLoader.loadClass(XPOSED_BRIDGE);
            Object xpBridgeObj = aClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 第二种：通过主动抛出异常，检查堆栈信息来判断是否存在XP框架
     *
     * @return
     */
    public static boolean isXposedExistByThrow() {
        try {
            throw new Exception("gg");
        } catch (Throwable e) {
            return isXposedExists(e);
        }
    }

    /**
     * 检查当前设备是否安装了xposed
     * @return
     */
    public static boolean hasXposed() {
        return isXposedExists(new Throwable());
    }


    /**
     * 第二种：判断是否有Xposed环境
     *
     * @param thr 异常
     * @return
     */
    public static boolean isXposedExists(Throwable thr) {
        StackTraceElement[] stackTraces = thr.getStackTrace();
        for (StackTraceElement stackTrace : stackTraces) {
            final String clazzName = stackTrace.getClassName();
            if (clazzName != null && clazzName.contains(XPOSED_BRIDGE)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 第三种：尝试关闭XP框架
     * 先通过isXposedExistByThrow判断有没有XP框架
     * 有的话先hookXP框架的全局变量disableHooks
     * <p>
     * 漏洞在，如果XP框架先hook了isXposedExistByThrow的返回值，那么后续就没法走了
     * 现在直接先hookXP框架的全局变量disableHooks
     *
     * 1.如果不想自己的APP被Xposed框架修改，可以在应用内部关闭Xposed框架Hook的总开关，使其无法对应用程序进行Hook。
     * 2.这个所谓的“总开关”实质上为XposedBridge.java文件中的disableHooks变量
     * 3.disableHooks变量默认为false，若该值为true则表示关闭了Xposed框架Hook的总开关。
     * 4.可以通过反射的方法去获取disableHooks变量
     *
     * @return 是否关闭成功的结果
     */
    public static boolean tryShutdownXposed() {
        Field xpdisabledHooks;
        try {
            ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
            Class<?> aClass = systemClassLoader.loadClass(XPOSED_BRIDGE);
            xpdisabledHooks = aClass.getDeclaredField("disableHooks");
            xpdisabledHooks.setAccessible(true);
            xpdisabledHooks.set(null, Boolean.TRUE);
            return true;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 第四种：获取DEX加载列表，判断其中是否包含XposedBridge.jar等字符串。
     *
     * @return
     */
    public static boolean isXposedByJar() {
        boolean z = false;
        try {
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            Class<?> cls = Class.forName("dalvik.system.DexPathList");
            Method method = Class.forName("dalvik.system.DexPathList$Element").getMethod("toString", new Class[0]);
            Field declaredField = cls.getDeclaredField("dexElements");
            declaredField.setAccessible(true);
            Field declaredField2 = BaseDexClassLoader.class.getDeclaredField("pathList");
            declaredField2.setAccessible(true);
            Object[] objArr = (Object[]) declaredField.get(declaredField2.get(classLoader));
            for (Object obj : objArr) {
                try {
                    String str2 = (String) method.invoke(obj, new Object[0]);
                    if (str2 != null && str2.contains("XposedBridge.jar")) {
                        z = true;
                    }
                } catch (Exception e2) {
                    z = false;
                }
            }
        }catch(Exception e3){
            z = false;
        }
        return z;
    }

}
