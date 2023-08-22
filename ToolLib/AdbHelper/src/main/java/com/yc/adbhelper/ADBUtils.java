package com.yc.adbhelper;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.text.format.DateUtils;

import androidx.annotation.IntRange;
import androidx.annotation.RequiresPermission;

import com.yc.appcontextlib.AppToolUtils;
import com.yc.toolutils.AppActivityUtils;
import com.yc.toolutils.AppLogUtils;
import com.yc.toolutils.AppTimeUtils;
import com.yc.toolutils.CollectionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ADBUtils {

    private ADBUtils() {

    }

    // 日志 TAG
    private static final String TAG = ADBUtils.class.getSimpleName();

    /**
     * 判断设备是否 root
     * @return {@code true} yes, {@code false} no
     */
    public static boolean isDeviceRooted() {
        String su = "su";
        String[] locations = {
                "/system/bin/", "/system/xbin/", "/sbin/",
                "/system/sd/xbin/", "/system/bin/failsafe/",
                "/data/local/xbin/", "/data/local/bin/", "/data/local/"
        };
        for (String location : locations) {
            if (new File(location + su).exists()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 请求 Root 权限
     * @return {@code true} success, {@code false} fail
     */
    public static boolean requestRoot() {
        return ShellUtils.execCmd("exit", true).isSuccess();
    }

    /**
     * 判断 APP 是否授权 Root 权限
     * @return {@code true} yes, {@code false} no
     */
    public static boolean isGrantedRoot() {
        return ShellUtils.execCmd("exit", true).isSuccess2();
    }

    // ==========
    // = 应用管理 =
    // ==========

    // ==========
    // = 应用列表 =
    // ==========

    /**
     * 获取 APP 列表 ( 包名 )
     * @param type options
     * @return 对应选项的应用包名列表
     */
    public static List<String> getAppList(final String type) {
        // adb shell pm list packages [options]
        String typeStr = TextUtils.isEmpty(type) ? "" : " " + type;
        // 执行 shell
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                "pm list packages" + typeStr, false
        );
        if (result.isSuccess3()) {
            try {
                String[] arrays = result.successMsg.split(DevFinal.SYMBOL.NEW_LINE);
                return Arrays.asList(arrays);
            } catch (Exception e) {
                AppLogUtils.e(TAG,  "getAppList type: " + e.getMessage());
            }
        }
        return null;
    }

    /**
     * 获取 APP 安装列表 ( 包名 )
     * @return APP 安装列表 ( 包名 )
     */
    public static List<String> getInstallAppList() {
        return getAppList(null);
    }

    /**
     * 获取用户安装的应用列表 ( 包名 )
     * @return 用户安装的应用列表 ( 包名 )
     */
    public static List<String> getUserAppList() {
        return getAppList("-3");
    }

    /**
     * 获取系统应用列表 ( 包名 )
     * @return 系统应用列表 ( 包名 )
     */
    public static List<String> getSystemAppList() {
        return getAppList("-s");
    }

    /**
     * 获取启用的应用列表 ( 包名 )
     * @return 启用的应用列表 ( 包名 )
     */
    public static List<String> getEnableAppList() {
        return getAppList("-e");
    }

    /**
     * 获取禁用的应用列表 ( 包名 )
     * @return 禁用的应用列表 ( 包名 )
     */
    public static List<String> getDisableAppList() {
        return getAppList("-d");
    }

    /**
     * 获取包名包含字符串 xxx 的应用列表
     * @param filter 过滤获取字符串
     * @return 包名包含字符串 xxx 的应用列表
     */
    public static List<String> getAppListToFilter(final String filter) {
        if (TextUtils.isEmpty(filter)) return null;
        return getAppList("| grep " + filter.trim());
    }

    /**
     * 判断是否安装应用
     * @param packageName 应用包名
     * @return {@code true} yes, {@code false} no
     */
    public static boolean isInstalledApp(final String packageName) {
        if (TextUtils.isEmpty(packageName)) return false;
        return ShellUtils.execCmd(
                "pm path " + packageName, false
        ).isSuccess3();
    }

    /**
     * 查看应用安装路径
     * @param packageName 应用包名
     * @return 应用安装路径
     */
    public static String getAppInstallPath(final String packageName) {
        if (TextUtils.isEmpty(packageName)) return null;
        // 执行 shell
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                "pm path " + packageName, false
        );
        if (result.isSuccess3()) {
            return result.successMsg;
        }
        return null;
    }

    /**
     * 清除应用数据与缓存 ( 相当于在设置里的应用信息界面点击了「清除缓存」和「清除数据」 )
     * @param packageName 应用包名
     * @return {@code true} success, {@code false} fail
     */
    public static boolean clearAppDataCache(final String packageName) {
        if (TextUtils.isEmpty(packageName)) return false;
        // adb shell pm clear <packageName>
        String cmd = "pm clear %s";
        // 执行 shell
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                String.format(cmd, packageName), true
        );
        return result.isSuccess4("success");
    }

    // ==========
    // = 应用信息 =
    // ==========

    /**
     * 查看应用详细信息
     * <pre>
     *     输出中包含很多信息, 包括 Activity Resolver Table、Registered ContentProviders、
     *     包名、userId、安装后的文件资源代码等路径、版本信息、权限信息和授予状态、签名版本信息等
     * </pre>
     * @param packageName 应用包名
     * @return 应用详细信息
     */
    public static String getAppMessage(final String packageName) {
        if (TextUtils.isEmpty(packageName)) return null;
        // 执行 shell
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                "dumpsys package " + packageName, true
        );
        if (result.isSuccess3()) {
            return result.successMsg;
        }
        return null;
    }

    /**
     * 获取 APP versionCode
     * @param packageName 应用包名
     * @return versionCode
     */
    public static int getVersionCode(final String packageName) {
        if (TextUtils.isEmpty(packageName)) return 0;
        try {
            // 执行 shell
            ShellUtils.CommandResult result = ShellUtils.execCmd(
                    "dumpsys package " + packageName + " | grep version",
                    true
            );
            if (result.isSuccess3()) {
                String[] arrays = result.successMsg.split(DevFinal.REGEX.SPACE);
                for (String value : arrays) {
                    if (!TextUtils.isEmpty(value)) {
                        try {
                            String[] splitArray = value.split("=");
                            if (splitArray.length == 2) {
                                if ("versionCode".equalsIgnoreCase(splitArray[0])) {
                                    return Integer.parseInt(splitArray[1]);
                                }
                            }
                        } catch (Exception ignored) {
                        }
                    }
                }
            }
        } catch (Exception e) {
            AppLogUtils.e(TAG,  "getVersionCode" + e.getMessage());
        }
        return 0;
    }

    /**
     * 获取 APP versionName
     * @param packageName 应用包名
     * @return versionName
     */
    public static String getVersionName(final String packageName) {
        if (TextUtils.isEmpty(packageName)) return null;
        try {
            // 执行 shell
            ShellUtils.CommandResult result = ShellUtils.execCmd(
                    "dumpsys package " + packageName + " | grep version",
                    true
            );
            if (result.isSuccess3()) {
                String[] arrays = result.successMsg.split(DevFinal.REGEX.SPACE);
                for (String value : arrays) {
                    if (!TextUtils.isEmpty(value)) {
                        try {
                            String[] splitArray = value.split("=");
                            if (splitArray.length == 2) {
                                if ("versionName".equalsIgnoreCase(splitArray[0])) {
                                    return splitArray[1];
                                }
                            }
                        } catch (Exception ignored) {
                        }
                    }
                }
            }
        } catch (Exception e) {
            AppLogUtils.e(TAG,  "getVersionName" + e.getMessage());
        }
        return null;
    }

    // ============
    // = 安装、卸载 =
    // ============

    /**
     * 安装应用
     * @param filePath 文件路径
     * @return {@code true} success, {@code false} fail
     */
    public static boolean installApp(final String filePath) {
        return installApp(filePath, "-rtsd");
    }

    /**
     * 安装应用
     * <pre>
     *     -l 将应用安装到保护目录 /mnt/asec
     *     -r 允许覆盖安装
     *     -t 允许安装 AndroidManifest.xml 里 application 指定 android:testOnly="true" 的应用
     *     -s 将应用安装到 sdcard
     *     -d 允许降级覆盖安装
     *     -g 授予所有运行时权限
     * </pre>
     * @param filePath 文件路径
     * @param params   安装选项
     * @return {@code true} success, {@code false} fail
     */
    public static boolean installApp(
            final String filePath,
            final String params
    ) {
        if (TextUtils.isEmpty(params)) return false;
        boolean isRoot = isDeviceRooted();
        // adb install [-lrtsdg] <path_to_apk>
        String cmd = "adb install %s %s";
        // 执行 shell
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                String.format(cmd, params, filePath), isRoot
        );
        // 判断是否成功
        return result.isSuccess4("success");
    }

    /**
     * 静默安装应用
     * @param filePath 文件路径
     * @return {@code true} success, {@code false} fail
     */
    public static boolean installAppSilent(final String filePath) {
        return installAppSilent(FileUtils.getFileByPath(filePath), null);
    }

    /**
     * 静默安装应用
     * @param file 文件
     * @return {@code true} success, {@code false} fail
     */
    public static boolean installAppSilent(final File file) {
        return installAppSilent(file, null);
    }

    /**
     * 静默安装应用
     * @param filePath 文件路径
     * @param params   安装选项
     * @return {@code true} success, {@code false} fail
     */
    public static boolean installAppSilent(
            final String filePath,
            final String params
    ) {
        return installAppSilent(
                FileUtils.getFileByPath(filePath),
                params, isDeviceRooted()
        );
    }

    /**
     * 静默安装应用
     * @param file   文件
     * @param params 安装选项
     * @return {@code true} success, {@code false} fail
     */
    public static boolean installAppSilent(
            final File file,
            final String params
    ) {
        return installAppSilent(file, params, isDeviceRooted());
    }

    /**
     * 静默安装应用
     * @param file     文件
     * @param params   安装选项
     * @param isRooted 是否 root
     * @return {@code true} success, {@code false} fail
     */
    public static boolean installAppSilent(
            final File file,
            final String params,
            final boolean isRooted
    ) {
        if (!FileUtils.isFileExists(file)) return false;
        String                   filePath = '"' + file.getAbsolutePath() + '"';
        String                   command  = "LD_LIBRARY_PATH=/vendor/lib*:/system/lib* pm install " + (params == null ? "" : params + " ") + filePath;
        ShellUtils.CommandResult result   = ShellUtils.execCmd(command, isRooted);
        return result.isSuccess4("success");
    }

    // =

    /**
     * 卸载应用
     * @param packageName 应用包名
     * @return {@code true} success, {@code false} fail
     */
    public static boolean uninstallApp(final String packageName) {
        return uninstallApp(packageName, false);
    }

    /**
     * 卸载应用
     * @param packageName 应用包名
     * @param isKeepData  -k 参数可选, 表示卸载应用但保留数据和缓存目录
     * @return {@code true} success, {@code false} fail
     */
    public static boolean uninstallApp(
            final String packageName,
            final boolean isKeepData
    ) {
        if (TextUtils.isEmpty(packageName)) return false;
        boolean isRoot = isDeviceRooted();
        // adb uninstall [-k] <packageName>
        String cmd = "adb uninstall ";
        if (isKeepData) {
            cmd += " -k ";
        }
        cmd += packageName;
        // 执行 shell
        ShellUtils.CommandResult result = ShellUtils.execCmd(cmd, isRoot);
        // 判断是否成功
        return result.isSuccess4("success");
    }

    /**
     * 静默卸载应用
     * @param packageName 应用包名
     * @return {@code true} success, {@code false} fail
     */
    public static boolean uninstallAppSilent(final String packageName) {
        return uninstallAppSilent(packageName, false, isDeviceRooted());
    }

    /**
     * 静默卸载应用
     * @param packageName 应用包名
     * @param isKeepData  -k 参数可选, 表示卸载应用但保留数据和缓存目录
     * @return {@code true} success, {@code false} fail
     */
    public static boolean uninstallAppSilent(
            final String packageName,
            final boolean isKeepData
    ) {
        return uninstallAppSilent(packageName, isKeepData, isDeviceRooted());
    }

    /**
     * 静默卸载应用
     * @param packageName 应用包名
     * @param isKeepData  -k 参数可选, 表示卸载应用但保留数据和缓存目录
     * @param isRooted    是否 root
     * @return {@code true} success, {@code false} fail
     */
    public static boolean uninstallAppSilent(
            final String packageName,
            final boolean isKeepData,
            final boolean isRooted
    ) {
        if (TextUtils.isEmpty(packageName)) return false;
        String                   command = "LD_LIBRARY_PATH=/vendor/lib*:/system/lib* pm uninstall " + (isKeepData ? "-k " : "") + packageName;
        ShellUtils.CommandResult result  = ShellUtils.execCmd(command, isRooted);
        return result.isSuccess4("success");
    }

    // ===========
    // = dumpsys =
    // ===========

    /**
     * 获取对应包名应用启动的 Activity
     * <pre>
     *     android.intent.category.LAUNCHER (android.intent.action.MAIN)
     * </pre>
     * @param packageName 应用包名
     * @return package.xx.Activity.className
     */
    public static String getActivityToLauncher(final String packageName) {
        if (TextUtils.isEmpty(packageName)) return null;
        String cmd = "dumpsys package %s";
        // 执行 shell
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                String.format(cmd, packageName), true
        );
        if (result.isSuccess3()) {
            String mainStr = "android.intent.action.MAIN:";
            int    start   = result.successMsg.indexOf(mainStr);
            // 防止都为 null
            if (start != -1) {
                try {
                    // 进行裁剪字符串
                    String subData = result.successMsg.substring(start + mainStr.length());
                    // 进行拆分
                    String[] arrays = subData.split(DevFinal.SYMBOL.NEW_LINE);
                    for (String value : arrays) {
                        if (!TextUtils.isEmpty(value)) {
                            // 存在包名才处理
                            if (value.contains(packageName)) {
                                String[] splitArrays = value.split(DevFinal.REGEX.SPACE);
                                for (String itemValue : splitArrays) {
                                    if (!TextUtils.isEmpty(itemValue)) {
                                        // 属于 packageName/ 前缀的
                                        if (itemValue.contains(packageName + "/")) {
                                            // 防止属于 packageName/.xx.Main_Activity
                                            if (itemValue.contains("/.")) {
                                                // packageName/.xx.Main_Activity
                                                // packageName/packageName.xx.Main_Activity
                                                itemValue = itemValue.replace(
                                                        "/", "/" + packageName
                                                );
                                            }
                                            return itemValue;
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    // =================
    // = 获取当前 Window =
    // =================

    /**
     * 获取当前显示的 Window
     * <pre>
     *     adb shell dumpsys window -h
     * </pre>
     * @return package.xx.Activity.className
     */
    public static String getWindowCurrent() {
        String cmd = "dumpsys window w | grep \\/  |  grep name=";
        // 执行 shell
        ShellUtils.CommandResult result = ShellUtils.execCmd(cmd, true);
        if (result.isSuccess3()) {
            try {
                String   nameStr = "name=";
                String[] arrays  = result.successMsg.split(DevFinal.SYMBOL.NEW_LINE);
                for (String value : arrays) {
                    if (!TextUtils.isEmpty(value)) {
                        int start = value.indexOf(nameStr);
                        if (start != -1) {
                            try {
                                String subData = value.substring(start + nameStr.length());
                                if (subData.indexOf(')') != -1) {
                                    return subData.substring(0, subData.length() - 1);
                                }
                                return subData;
                            } catch (Exception ignored) {
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取当前显示的 Window
     * @return package/package.xx.Activity.className
     */
    public static String getWindowCurrent2() {
        String cmd = "dumpsys window windows | grep Current";
        // 执行 shell
        ShellUtils.CommandResult result = ShellUtils.execCmd(cmd, true);
        if (result.isSuccess3()) {
            try {
                // 拆分换行, 并循环
                String[] arrays = result.successMsg.split(DevFinal.SYMBOL.NEW_LINE);
                for (String value : arrays) {
                    if (!TextUtils.isEmpty(value)) {
                        String[] splitArrays = value.split(DevFinal.REGEX.SPACE);
                        if (splitArrays.length != 0) {
                            for (String itemValue : splitArrays) {
                                if (!TextUtils.isEmpty(itemValue)) {
                                    int start     = itemValue.indexOf('/');
                                    int lastIndex = itemValue.lastIndexOf('}');
                                    if (start != -1 && lastIndex != -1) {
                                        // 获取裁剪数据
                                        String strData = itemValue.substring(0, lastIndex);
                                        // 防止属于 packageName/.xx.Main_Activity
                                        if (strData.contains("/.")) {
                                            // packageName/.xx.Main_Activity
                                            // packageName/packageName.xx.Main_Activity
                                            strData = strData.replace(
                                                    "/", "/" + itemValue.substring(0, start)
                                            );
                                        }
                                        return strData;
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取对应包名显示的 Window
     * @param packageName 应用包名
     * @return package/package.xx.Activity.className
     */
    public static String getWindowCurrentToPackage(final String packageName) {
        if (TextUtils.isEmpty(packageName)) return null;
        String cmd = "dumpsys window windows | grep %s";
        // 执行 shell
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                String.format(cmd, packageName), true
        );
        if (result.isSuccess3()) {
            try {
                // 拆分换行, 并循环
                String[] arrays = result.successMsg.split(DevFinal.SYMBOL.NEW_LINE);
                for (String value : arrays) {
                    if (!TextUtils.isEmpty(value)) {
                        String[] splitArrays = value.split(DevFinal.REGEX.SPACE);
                        if (splitArrays.length != 0) {
                            for (String itemValue : splitArrays) {
                                if (!TextUtils.isEmpty(itemValue)) {
                                    int start     = itemValue.indexOf('/');
                                    int lastIndex = itemValue.lastIndexOf('}');
                                    if (start != -1 && lastIndex != -1
                                            && itemValue.indexOf(packageName) == 0) {
                                        // 获取裁剪数据
                                        String strData = itemValue.substring(0, lastIndex);
                                        // 防止属于 packageName/.xx.Main_Activity
                                        if (strData.contains("/.")) {
                                            // packageName/.xx.Main_Activity
                                            // packageName/packageName.xx.Main_Activity
                                            strData = strData.replace(
                                                    "/", "/" + packageName
                                            );
                                        }
                                        return strData;
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // ===================
    // = 获取当前 Activity =
    // ===================

    /**
     * 获取当前显示的 Activity
     * @return package.xx.Activity.className
     */
    public static String getActivityCurrent() {
        String cmd = "dumpsys activity activities | grep mFocusedActivity";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            cmd = "dumpsys activity activities | grep mResumedActivity";
        }
        // 执行 shell
        ShellUtils.CommandResult result = ShellUtils.execCmd(cmd, true);
        if (result.isSuccess3()) {
            try {
                // 拆分换行, 并循环
                String[] arrays = result.successMsg.split(DevFinal.SYMBOL.NEW_LINE);
                for (String value : arrays) {
                    if (!TextUtils.isEmpty(value)) {
                        String[] splitArrays = value.split(DevFinal.REGEX.SPACE);
                        if (splitArrays.length != 0) {
                            for (String itemValue : splitArrays) {
                                if (!TextUtils.isEmpty(itemValue)) {
                                    int start = itemValue.indexOf('/');
                                    if (start != -1) {
                                        // 获取裁剪数据
                                        String strData = itemValue;
                                        // 防止属于 packageName/.xx.Main_Activity
                                        if (strData.contains("/.")) {
                                            // packageName/.xx.Main_Activity
                                            // packageName/packageName.xx.Main_Activity
                                            strData = strData.replace(
                                                    "/", "/" + itemValue.substring(0, start)
                                            );
                                        }
                                        return strData;
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取 Activity 栈
     * @return 当前全部 Activity 栈信息
     */
    public static String getActivitys() {
        return getActivitys(null);
    }

    /**
     * 获取 Activity 栈
     * @param append 追加筛选条件
     * @return 当前全部 Activity 栈信息
     */
    public static String getActivitys(final String append) {
        String cmd = "dumpsys activity activities";
        if (!TextUtils.isEmpty(append)) {
            cmd += " " + append.trim();
        }
        // 执行 shell
        ShellUtils.CommandResult result = ShellUtils.execCmd(cmd, true);
        if (result.isSuccess3()) {
            return result.successMsg;
        }
        return null;
    }

    /**
     * 获取对应包名的 Activity 栈
     * @param packageName 应用包名
     * @return 对应包名的 Activity 栈信息
     */
    public static String getActivitysToPackage(final String packageName) {
        if (TextUtils.isEmpty(packageName)) return null;
        return getActivitys("| grep " + packageName);
    }

    /**
     * 获取对应包名的 Activity 栈 ( 最新的 Activity 越靠后 )
     * @param packageName 应用包名
     * @return 对应包名的 Activity 栈信息集合
     */
    public static List<String> getActivitysToPackageLists(final String packageName) {
        // 获取对应包名的 Activity 数据结果
        String result = getActivitysToPackage(packageName);
        // 防止数据为 null
        if (!TextUtils.isEmpty(result)) {
            try {
                List<String> lists  = new ArrayList<>();
                String[]     arrays = result.split(DevFinal.SYMBOL.NEW_LINE);
                // 拆分后, 数据长度
                int splitLength = arrays.length;
                // 获取 Activity 栈字符串
                String activities = null;
                // 判断最后一行是否符合条件
                if (arrays[splitLength - 1].contains("Activities=")) {
                    activities = arrays[splitLength - 1];
                } else {
                    for (String value : arrays) {
                        if (value.contains("Activities=")) {
                            activities = value;
                            break;
                        }
                    }
                }
                // 进行特殊处理 Activities=[ActivityRecord{xx},ActivityRecord{xx}];
                int startIndex = activities.indexOf("Activities=[");
                activities = activities.substring(
                        startIndex + "Activities=[".length(),
                        activities.length() - 1
                );
                // 再次进行拆分
                String[] activityArrays = activities.split("ActivityRecord");
                for (String value : activityArrays) {
                    try {
                        String[] splitArrays = value.split(DevFinal.REGEX.SPACE);
                        if (splitArrays.length != 0) {
                            for (String itemValue : splitArrays) {
                                int start = itemValue.indexOf(packageName + "/");
                                if (start != -1) {
                                    // 获取裁剪数据
                                    String strData = itemValue;
                                    // 防止属于 packageName/.xx.XxxActivity
                                    if (strData.contains("/.")) {
                                        // packageName/.xx.XxxActivity
                                        // packageName/packageName.xx.XxxActivity
                                        strData = strData.replace(
                                                "/", "/" + itemValue.substring(0, start)
                                        );
                                    }
                                    // 保存数据
                                    lists.add(strData);
                                }
                            }
                        }
                    } catch (Exception ignored) {
                    }
                }
                return lists;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // =

    /**
     * 判断 Activity 栈顶是否重复
     * @param packageName 应用包名
     * @param activity    Activity Name
     * @return {@code true} yes, {@code false} no
     */
    public static boolean isActivityTopRepeat(
            final String packageName,
            final String activity
    ) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        } else if (TextUtils.isEmpty(activity)) {
            return false;
        }
        // 获取
        List<String> lists = getActivitysToPackageLists(packageName);
        // 数据长度
        int length = lists == null ? 0 : lists.size();
        // 防止数据为 null
        if (length >= 2) { // 两个页面以上, 才能够判断是否重复
            try {
                String value = lists.get(length - 1);
                if (value != null && value.endsWith(activity)) {
                    // 倒序遍历, 越后面是 Activity 栈顶
                    for (int i = length - 2; i >= 0; i--) {
                        String data = lists.get(i);
                        // 判断是否该页面结尾
                        if (data.endsWith(activity)) {
                            return true;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 判断 Activity 栈顶是否重复
     * @param packageName 应用包名
     * @param activitys   Activity Name 集合
     * @return {@code true} yes, {@code false} no
     */
    public static boolean isActivityTopRepeat(
            final String packageName,
            final List<String> activitys
    ) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        } else if (activitys == null || activitys.size() == 0) {
            return false;
        }
        // 获取
        List<String> lists = getActivitysToPackageLists(packageName);
        // 数据长度
        int length = lists == null ? 0 : lists.size();
        // 防止数据为 null
        if (length >= 2) { // 两个页面以上, 才能够判断是否重复
            // 循环判断
            for (String activity : activitys) {
                try {
                    String value = lists.get(length - 1);
                    if (value != null && value.endsWith(activity)) {
                        // 倒序遍历, 越后面是 Activity 栈顶
                        for (int i = length - 2; i >= 0; i--) {
                            String data = lists.get(i);
                            // 判断是否该页面结尾
                            if (data.endsWith(activity)) {
                                return true;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    // =

    /**
     * 获取 Activity 栈顶重复总数
     * @param packageName 应用包名
     * @param activity    Activity Name
     * @return 指定 Activity 在栈顶重复总数
     */
    public static int getActivityTopRepeatCount(
            final String packageName,
            final String activity
    ) {
        if (TextUtils.isEmpty(packageName)) {
            return 0;
        } else if (TextUtils.isEmpty(activity)) {
            return 0;
        }
        // 重复数量
        int number = 0;
        // 获取
        List<String> lists = getActivitysToPackageLists(packageName);
        // 数据长度
        int length = lists == null ? 0 : lists.size();
        // 防止数据为 null
        if (length >= 2) { // 两个页面以上, 才能够判断是否重复
            try {
                String value = lists.get(length - 1);
                if (value != null && value.endsWith(activity)) {
                    // 倒序遍历, 越后面是 Activity 栈顶
                    for (int i = length - 2; i >= 0; i--) {
                        String data = lists.get(i);
                        // 判断是否该页面结尾
                        if (data.endsWith(activity)) {
                            number++;
                        } else {
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return number;
    }

    /**
     * 获取 Activity 栈顶重复总数
     * @param packageName 应用包名
     * @param activitys   Activity Name 集合
     * @return 指定 Activity 在栈顶重复总数
     */
    public static int getActivityTopRepeatCount(
            final String packageName,
            final List<String> activitys
    ) {
        if (TextUtils.isEmpty(packageName)) {
            return 0;
        } else if (activitys == null || activitys.size() == 0) {
            return 0;
        }
        // 获取
        List<String> lists = getActivitysToPackageLists(packageName);
        // 数据长度
        int length = lists == null ? 0 : lists.size();
        // 防止数据为 null
        if (length >= 2) { // 两个页面以上, 才能够判断是否重复
            // 循环判断
            for (String activity : activitys) {
                try {
                    // 重复数量
                    int number = 0;
                    // 判断是否对应页面结尾
                    String value = lists.get(length - 1);
                    if (value != null && value.endsWith(activity)) {
                        // 倒序遍历, 越后面是 Activity 栈顶
                        for (int i = length - 2; i >= 0; i--) {
                            String data = lists.get(i);
                            // 判断是否该页面结尾
                            if (data.endsWith(activity)) {
                                number++;
                            } else {
                                break;
                            }
                        }
                        // 进行判断处理
                        return number;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    // =====================
    // = 正在运行的 Services =
    // =====================

    /**
     * 查看正在运行的 Services
     * @return 运行中的 Services 信息
     */
    public static String getServices() {
        return getServices(null);
    }

    /**
     * 查看正在运行的 Services
     * @param packageName 应用包名, 参数不是必须的, 指定 <packageName> 表示查看与某个包名相关的 Services,
     *                    不指定表示查看所有 Services, <packageName> 不一定要给出完整的包名,
     *                    比如运行 adb shell dumpsys activity services org.mazhuang
     *                    那么包名 org.mazhuang.demo1、org.mazhuang.demo2 和 org.mazhuang123 等相关的 Services 都会列出来
     * @return 运行中的 Services 信息
     */
    public static String getServices(final String packageName) {
        String                   cmd    = "dumpsys activity services" + ((TextUtils.isEmpty(packageName) ? "" : " " + packageName));
        ShellUtils.CommandResult result = ShellUtils.execCmd(cmd, true);
        if (result.isSuccess3()) {
            return result.successMsg;
        }
        return null;
    }

    // ======
    // = am =
    // ======

    /**
     * 启动自身应用
     * @return {@code true} success, {@code false} fail
     */
    public static boolean startSelfApp() {
        return startSelfApp(false);
    }

    /**
     * 启动自身应用
     * @param closeActivity 是否关闭 Activity 所属的 APP 进程后再启动 Activity
     * @return {@code true} success, {@code false} fail
     */
    public static boolean startSelfApp(final boolean closeActivity) {
        try {
            // 获取包名
            String packageName = AppToolUtils.getApp().getPackageName();
            // 获取 Launcher Activity
            String activity = AppActivityUtils.getLauncherActivity();
            // 跳转应用启动页 ( 启动应用 )
            return startActivity(packageName + "/" + activity, closeActivity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 跳转页面 Activity
     * @param packageAndLauncher package/package.xx.Activity.className
     * @param closeActivity      是否关闭 Activity 所属的 APP 进程后再启动 Activity
     * @return {@code true} success, {@code false} fail
     */
    public static boolean startActivity(
            final String packageAndLauncher,
            final boolean closeActivity
    ) {
        return startActivity(packageAndLauncher, null, closeActivity);
    }

    /**
     * 跳转页面 Activity
     * @param packageAndLauncher package/package.xx.Activity.className
     * @param append             追加的信息, 例如传递参数等
     * @param closeActivity      是否关闭 Activity 所属的 APP 进程后再启动 Activity
     * @return {@code true} success, {@code false} fail
     */
    public static boolean startActivity(
            final String packageAndLauncher,
            final String append,
            final boolean closeActivity
    ) {
        if (TextUtils.isEmpty(packageAndLauncher)) return false;
        try {
            // am start [options] <INTENT>
            String cmd = "am start %s";
            if (closeActivity) {
                cmd = String.format(cmd, "-S " + packageAndLauncher);
            } else {
                cmd = String.format(cmd, packageAndLauncher);
            }
            // 判断是否追加
            if (!TextUtils.isEmpty(append)) {
                cmd += " " + append.trim();
            }
            // 执行 shell
            ShellUtils.CommandResult result = ShellUtils.execCmd(cmd, true);
            return result.isSuccess2();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 启动服务
     * @param packageAndService package/package.xx.Service.className
     * @return {@code true} success, {@code false} fail
     */
    public static boolean startService(final String packageAndService) {
        return startService(packageAndService, null);
    }

    /**
     * 启动服务
     * @param packageAndService package/package.xx.Service.className
     * @param append            追加的信息, 例如传递参数等
     * @return {@code true} success, {@code false} fail
     */
    public static boolean startService(
            final String packageAndService,
            final String append
    ) {
        if (TextUtils.isEmpty(packageAndService)) return false;
        try {
            // am startservice [options] <INTENT>
            String cmd = "am startservice %s";
            // 进行格式化
            cmd = String.format(cmd, packageAndService);
            // 判断是否追加
            if (!TextUtils.isEmpty(append)) {
                cmd += " " + append.trim();
            }
            // 执行 shell
            ShellUtils.CommandResult result = ShellUtils.execCmd(cmd, true);
            return result.isSuccess2();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 停止服务
     * @param packageAndService package/package.xx.Service.className
     * @return {@code true} success, {@code false} fail
     */
    public static boolean stopService(final String packageAndService) {
        return stopService(packageAndService, null);
    }

    /**
     * 停止服务
     * @param packageAndService package/package.xx.Service.className
     * @param append            追加的信息, 例如传递参数等
     * @return {@code true} success, {@code false} fail
     */
    public static boolean stopService(
            final String packageAndService,
            final String append
    ) {
        if (TextUtils.isEmpty(packageAndService)) return false;
        try {
            // am stopservice [options] <INTENT>
            String cmd = "am stopservice %s";
            // 进行格式化
            cmd = String.format(cmd, packageAndService);
            // 判断是否追加
            if (!TextUtils.isEmpty(append)) {
                cmd += " " + append.trim();
            }
            // 执行 shell
            ShellUtils.CommandResult result = ShellUtils.execCmd(cmd, true);
            return result.isSuccess3();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 发送广播 ( 向所有组件发送 )
     * <pre>
     *     向所有组件广播 BOOT_COMPLETED
     *     adb shell am broadcast -a android.intent.action.BOOT_COMPLETED
     * </pre>
     * @param broadcast 广播 INTENT
     * @return {@code true} success, {@code false} fail
     */
    public static boolean sendBroadcastToAll(final String broadcast) {
        if (TextUtils.isEmpty(broadcast)) return false;
        try {
            // am broadcast [options] <INTENT>
            String cmd = "am broadcast -a %s";
            // 执行 shell
            ShellUtils.CommandResult result = ShellUtils.execCmd(
                    String.format(cmd, broadcast), true
            );
            return result.isSuccess3();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 发送广播
     * <pre>
     *     只向 org.mazhuang.boottimemeasure/.BootCompletedReceiver 广播 BOOT_COMPLETED
     *     adb shell am broadcast -a android.intent.action.BOOT_COMPLETED -n org.mazhuang.boottimemeasure/.BootCompletedReceiver
     * </pre>
     * @param packageAndBroadcast package/package.xx.Receiver.className
     * @param broadcast           广播 INTENT
     * @return {@code true} success, {@code false} fail
     */
    public static boolean sendBroadcast(
            final String packageAndBroadcast,
            final String broadcast
    ) {
        if (TextUtils.isEmpty(packageAndBroadcast)) return false;
        if (TextUtils.isEmpty(broadcast)) return false;
        try {
            // am broadcast [options] <INTENT>
            String cmd = "am broadcast -a %s -n %s";
            // 执行 shell
            ShellUtils.CommandResult result = ShellUtils.execCmd(
                    String.format(cmd, broadcast, packageAndBroadcast),
                    true
            );
            return result.isSuccess3();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // =

    /**
     * 销毁进程
     * @param packageName 应用包名
     * @return {@code true} success, {@code false} fail
     */
    public static boolean kill(final String packageName) {
        if (TextUtils.isEmpty(packageName)) return false;
        try {
            String cmd = "am force-stop %s";
            // 执行 shell
            ShellUtils.CommandResult result = ShellUtils.execCmd(
                    String.format(cmd, packageName), true
            );
            return result.isSuccess2();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 收紧内存
     * @param pid   进程 ID
     * @param level HIDDEN、RUNNING_MODERATE、BACKGROUND、RUNNING_LOW、MODERATE、RUNNING_CRITICAL、COMPLETE
     * @return {@code true} success, {@code false} fail
     */
    public static boolean sendTrimMemory(
            final int pid,
            final String level
    ) {
        if (TextUtils.isEmpty(level)) return false;
        try {
            String cmd = "am send-trim-memory %s %s";
            // 执行 shell
            ShellUtils.CommandResult result = ShellUtils.execCmd(
                    String.format(cmd, pid, level), true
            );
            return result.isSuccess2();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

//    // ==========
//    // = 文件管理 =
//    // ==========
//
//    /**
//     * 复制设备里的文件到电脑
//     * @param remote 设备里的文件路径
//     * @param local  电脑上的目录
//     * @return {@code true} success, {@code false} fail
//     */
//    public static boolean pull(final String remote, final String local) {
//        if (TextUtils.isEmpty(remote)) return false;
//        try {
//            // adb pull <设备里的文件路径> [电脑上的目录]
//            String cmd = "adb pull %s";
//            // 判断是否存到默认地址
//            if (!TextUtils.isEmpty(local)) {
//                cmd += " " + local;
//            }
//            // 执行 shell
//            ShellUtils.CommandResult result = ShellUtils.execCmd(String.format(cmd, remote), true);
//            return result.isSuccess2();
//        } catch (Exception e) {
//            LogPrintUtils.eTag(TAG, e, "pull");
//        }
//        return false;
//    }
//
//    /**
//     * 复制电脑里的文件到设备
//     * @param local  电脑上的文件路径
//     * @param remote 设备里的目录
//     * @return {@code true} success, {@code false} fail
//     */
//    public static boolean push(final String local, final String remote) {
//        if (TextUtils.isEmpty(local)) return false;
//        if (TextUtils.isEmpty(remote)) return false;
//        try {
//            // adb push <电脑上的文件路径> <设备里的目录>
//            String cmd = "adb push %s %s";
//            // 执行 shell
//            ShellUtils.CommandResult result = ShellUtils.execCmd(String.format(cmd, local, remote), true);
//            return result.isSuccess2();
//        } catch (Exception e) {
//            LogPrintUtils.eTag(TAG, e, "push");
//        }
//        return false;
//    }

    // =========
    // = Input =
    // =========

    // ==============================
    // = tap ( 模拟 touch 屏幕的事件 ) =
    // ==============================

    /**
     * 点击某个区域
     * @param x X 轴坐标
     * @param y Y 轴坐标
     * @return {@code true} success, {@code false} fail
     */
    public static boolean tap(
            final float x,
            final float y
    ) {
        try {
            // input [touchscreen|touchpad|touchnavigation] tap <x> <y>
            // input [ 屏幕、触摸板、导航键 ] tap
            String cmd = "input touchscreen tap %s %s";
            // 执行 shell
            ShellUtils.CommandResult result = ShellUtils.execCmd(
                    String.format(cmd, (int) x, (int) y), true
            );
            return result.isSuccess2();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ====================
    // = swipe ( 滑动事件 ) =
    // ====================

    /**
     * 按压某个区域 ( 点击 )
     * @param x X 轴坐标
     * @param y Y 轴坐标
     * @return {@code true} success, {@code false} fail
     */
    public static boolean swipeClick(
            final float x,
            final float y
    ) {
        return swipe(x, y, x, y, 100L);
    }

    /**
     * 按压某个区域 time 大于一定时间变成长按
     * @param x      X 轴坐标
     * @param y      Y 轴坐标
     * @param millis 按压时间
     * @return {@code true} success, {@code false} fail
     */
    public static boolean swipeClick(
            final float x,
            final float y,
            final long millis
    ) {
        return swipe(x, y, x, y, millis);
    }

    /**
     * 滑动到某个区域
     * @param x      X 轴坐标
     * @param y      Y 轴坐标
     * @param toX    滑动到 X 轴坐标
     * @param toY    滑动到 Y 轴坐标
     * @param millis 滑动时间 ( 毫秒 )
     * @return {@code true} success, {@code false} fail
     */
    public static boolean swipe(
            final float x,
            final float y,
            final float toX,
            final float toY,
            final long millis
    ) {
        try {
            // input [touchscreen|touchpad|touchnavigation] swipe <x1> <y1> <x2> <y2> [duration(ms)]
            String cmd = "input touchscreen swipe %s %s %s %s %s";
            // 执行 shell
            ShellUtils.CommandResult result = ShellUtils.execCmd(
                    String.format(
                            cmd, (int) x, (int) y,
                            (int) toX, (int) toY, millis
                    ), true
            );
            return result.isSuccess2();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ===================
    // = text ( 模拟输入 ) =
    // ===================

    /**
     * 输入文本 ( 不支持中文 )
     * @param txt 文本内容
     * @return {@code true} success, {@code false} fail
     */
    public static boolean text(final String txt) {
        if (TextUtils.isEmpty(txt)) return false;
        try {
            // input text <string>
            String cmd = "input text %s";
            // 执行 shell
            ShellUtils.CommandResult result = ShellUtils.execCmd(
                    String.format(cmd, txt), true
            ); // false 可以执行
            return result.isSuccess2();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // =======================
    // = keyevent ( 按键操作 ) =
    // =======================

    /**
     * 触发某些按键
     * @param keyCode KeyEvent.xxx 如: KeyEvent.KEYCODE_BACK ( 返回键 )
     * @return {@code true} success, {@code false} fail
     */
    public static boolean keyevent(final int keyCode) {
        try {
            // input keyevent <key code number or name>
            String cmd = "input keyevent %s";
            // 执行 shell
            ShellUtils.CommandResult result = ShellUtils.execCmd(
                    String.format(cmd, keyCode), true
            ); // false 可以执行
            return result.isSuccess2();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==========
    // = 实用功能 =
    // ==========

    /**
     * 屏幕截图
     * @param path 存储路径
     * @return {@code true} success, {@code false} fail
     */
    public static boolean screencap(final String path) {
        return screencap(path, 0);
    }

    /**
     * 屏幕截图
     * @param path      存储路径
     * @param displayId -d display-id 指定截图的显示屏编号 ( 有多显示屏的情况下 ) 默认 0
     * @return {@code true} success, {@code false} fail
     */
    public static boolean screencap(
            final String path,
            final int displayId
    ) {
        if (TextUtils.isEmpty(path)) return false;
        try {
            String cmd = "screencap -p -d %s %s";
            // 执行 shell
            ShellUtils.CommandResult result = ShellUtils.execCmd(
                    String.format(
                            cmd, Math.max(displayId, 0), path
                    ), true
            );
            return result.isSuccess2();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 录制屏幕 ( 以 mp4 格式保存 )
     * @param path 存储路径
     * @return {@code true} success, {@code false} fail
     */
    public static boolean screenrecord(final String path) {
        return screenrecord(path, null, -1, -1);
    }

    /**
     * 录制屏幕 ( 以 mp4 格式保存 )
     * @param path 存储路径
     * @param time 录制时长, 单位秒 ( 默认 / 最长 180 秒 )
     * @return {@code true} success, {@code false} fail
     */
    public static boolean screenrecord(
            final String path,
            final int time
    ) {
        return screenrecord(path, null, -1, time);
    }

    /**
     * 录制屏幕 ( 以 mp4 格式保存到 )
     * @param path 存储路径
     * @param size 视频的尺寸, 比如 1280x720, 默认是屏幕分辨率
     * @param time 录制时长, 单位秒 ( 默认 / 最长 180 秒 )
     * @return {@code true} success, {@code false} fail
     */
    public static boolean screenrecord(
            final String path,
            final String size,
            final int time
    ) {
        return screenrecord(path, size, -1, time);
    }

    /**
     * 录制屏幕 ( 以 mp4 格式保存到 )
     * @param path    存储路径
     * @param size    视频的尺寸, 比如 1280x720, 默认是屏幕分辨率
     * @param bitRate 视频的比特率, 默认是 4Mbps
     * @param time    录制时长, 单位秒 ( 默认 / 最长 180 秒 )
     * @return {@code true} success, {@code false} fail
     */
    public static boolean screenrecord(
            final String path,
            final String size,
            final int bitRate,
            final int time
    ) {
        if (TextUtils.isEmpty(path)) return false;
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("screenrecord");
            if (!TextUtils.isEmpty(size)) {
                builder.append(" --size ").append(size);
            }
            if (bitRate > 0) {
                builder.append(" --bit-rate ").append(bitRate);
            }
            if (time > 0) {
                builder.append(" --time-limit ").append(time);
            }
            builder.append(" ").append(path);
            // 执行 shell
            ShellUtils.CommandResult result = ShellUtils.execCmd(
                    builder.toString(), true
            );
            return result.isSuccess2();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 查看连接过的 Wifi 密码
     * @return 连接过的 Wifi 密码
     */
    public static String wifiConf() {
        try {
            String cmd = "cat /data/misc/wifi/*.conf";
            // 执行 shell
            ShellUtils.CommandResult result = ShellUtils.execCmd(cmd, true);
            if (result.isSuccess3()) {
                return result.successMsg;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 开启 / 关闭 Wifi
     * @param open 是否开启
     * @return {@code true} success, {@code false} fail
     */
    public static boolean wifiSwitch(final boolean open) {
        String cmd = "svc wifi %s";
        // 执行 shell
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                String.format(cmd, open ? "enable" : "disable"), true
        );
        return result.isSuccess2();
    }

    /**
     * 设置系统时间
     * @param time yyyyMMdd.HHmmss 20160823.131500
     *             表示将系统日期和时间更改为 2016 年 08 月 23 日 13 点 15 分 00 秒
     * @return {@code true} success, {@code false} fail
     */
    public static boolean setSystemTime(final String time) {
        if (TextUtils.isEmpty(time)) return false;
        try {
            String cmd = "date -s %s";
            // 执行 shell
            ShellUtils.CommandResult result = ShellUtils.execCmd(
                    String.format(cmd, time), true
            );
            return result.isSuccess2();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 设置系统时间
     * @param time MMddHHmmyyyy.ss 082313152016.00
     *             表示将系统日期和时间更改为 2016 年 08 月 23 日 13 点 15 分 00 秒
     * @return {@code true} success, {@code false} fail
     */
    public static boolean setSystemTime2(final String time) {
        if (TextUtils.isEmpty(time)) return false;
        try {
            String cmd = "date %s";
            // 执行 shell
            ShellUtils.CommandResult result = ShellUtils.execCmd(
                    String.format(cmd, time), true
            );
            return result.isSuccess2();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 设置系统时间
     * @param millis 时间毫秒转换 MMddHHmmyyyy.ss
     * @return {@code true} success, {@code false} fail
     */
    public static boolean setSystemTime2(final long millis) {
        if (millis < 0) return false;
        try {
            String cmd = "date %s";
            // 执行 shell
            ShellUtils.CommandResult result = ShellUtils.execCmd(
                    String.format(cmd, AppTimeUtils.millis2String(millis)), true
            );
            return result.isSuccess2();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // =============
    // = 刷机相关命令 =
    // =============

    /**
     * 重启引导到 recovery ( 需要 root 权限 )
     * @return {@code true} success, {@code false} fail
     */
    public static boolean rebootToRecovery() {
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                "reboot recovery", true
        );
        return result.isSuccess2();
    }

    /**
     * 重启引导到 bootloader ( 需要 root 权限 )
     * @return {@code true} success, {@code false} fail
     */
    public static boolean rebootToBootloader() {
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                "reboot bootloader", true
        );
        return result.isSuccess2();
    }

    // ==========
    // = 滑动方法 =
    // ==========

    /**
     * 发送事件滑动
     * @param x      X 轴坐标
     * @param y      Y 轴坐标
     * @param toX    滑动到 X 轴坐标
     * @param toY    滑动到 Y 轴坐标
     * @param number 循环次数
     */
    public static void sendEventSlide(
            final float x,
            final float y,
            final float toX,
            final float toY,
            final int number
    ) {
        List<String> lists = new ArrayList<>();
        // = 开头 =
        lists.add("sendevent /dev/input/event1 3 57 109");
        lists.add("sendevent /dev/input/event1 3 53 " + x);
        lists.add("sendevent /dev/input/event1 3 54 " + y);
        // 发送 touch 事件 ( 必须使用 0 0 0 配对 )
        lists.add("sendevent /dev/input/event1 1 330 1");
        lists.add("sendevent /dev/input/event1 0 0 0");

        // 判断方向 ( 手势是否从左到右, View 往左滑, 手势操作往右滑 )
        boolean isLeftToRight = toX > x;
        // 判断方向 ( 手势是否从上到下, View 往上滑, 手势操作往下滑 )
        boolean isTopToBottom = toY > y;

        // 计算差数
        float diffX = isLeftToRight ? (toX - x) : (x - toX);
        float diffY = isTopToBottom ? (toY - y) : (y - toY);

        if (!isLeftToRight) {
            diffX = -diffX;
        }

        if (!isTopToBottom) {
            diffY = -diffY;
        }

        // 平均值
        float averageX = diffX / number;
        float averageY = diffY / number;
        // 上次位置
        int oldX = (int) x;
        int oldY = (int) y;

        // 循环处理
        for (int i = 0; i <= number; i++) {
            if (averageX != 0F) {
                // 进行判断处理
                int calcX = (int) (x + averageX * i);
                if (oldX != calcX) {
                    oldX = calcX;
                    lists.add("sendevent /dev/input/event1 3 53 " + calcX);
                }
            }

            if (averageY != 0F) {
                // 进行判断处理
                int calcY = (int) (y + averageY * i);
                if (oldY != calcY) {
                    oldY = calcY;
                    lists.add("sendevent /dev/input/event1 3 54 " + calcY);
                }
            }
            // 每次操作结束发送
            lists.add("sendevent /dev/input/event1 0 0 0");
        }
        // = 结尾 =
        lists.add("sendevent /dev/input/event1 3 57 4294967295");
        // 释放 touch 事件 ( 必须使用 0 0 0 配对 )
        lists.add("sendevent /dev/input/event1 1 330 0");
        lists.add("sendevent /dev/input/event1 0 0 0");

        // 执行 shell
        ShellUtils.execCmd(lists, true);
    }

    // =============
    // = 查看设备信息 =
    // =============

    /**
     * 获取 SDK 版本
     * @return SDK 版本
     */
    public static String getSDKVersion() {
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                "getprop ro.build.version.sdk", false
        );
        if (result.isSuccess3()) {
            return result.successMsg;
        }
        return null;
    }

    /**
     * 获取 Android 系统版本
     * @return Android 系统版本
     */
    public static String getAndroidVersion() {
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                "getprop ro.build.version.release", false
        );
        if (result.isSuccess3()) {
            return result.successMsg;
        }
        return null;
    }

    /**
     * 获取设备型号 ( 如 RedmiNote4X )
     * @return 设备型号
     */
    public static String getModel() {
        // android.os.Build 内部有信息 android.os.Build.MODEL
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                "getprop ro.product.model", false
        );
        if (result.isSuccess3()) {
            return result.successMsg;
        }
        return null;
    }

    /**
     * 获取设备品牌
     * @return 设备品牌
     */
    public static String getBrand() {
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                "getprop ro.product.brand", false
        );
        if (result.isSuccess3()) {
            return result.successMsg;
        }
        return null;
    }

    /**
     * 获取设备名
     * @return 设备名
     */
    public static String getDeviceName() {
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                "getprop ro.product.name", false
        );
        if (result.isSuccess3()) {
            return result.successMsg;
        }
        return null;
    }

    /**
     * 获取 CPU 支持的 abi 列表
     * @return CPU 支持的 abi 列表
     */
    public static String getCpuAbiList() {
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                "cat /system/build.prop | grep ro.product.cpu.abi",
                false
        );
        if (result.isSuccess3()) {
            return result.successMsg;
        }
        return null;
    }

    /**
     * 获取每个应用程序的内存上限
     * @return 每个应用程序的内存上限
     */
    public static String getAppHeapsize() {
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                "getprop dalvik.vm.heapsize", false
        );
        if (result.isSuccess3()) {
            return result.successMsg;
        }
        return null;
    }

    /**
     * 获取电池状况
     * @return 电池状况
     */
    public static String getBattery() {
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                "dumpsys battery", true
        );
        if (result.isSuccess3()) { // scale 代表最大电量, level 代表当前电量
            return result.successMsg;
        }
        return null;
    }

    /**
     * 获取屏幕密度
     * @return 屏幕密度
     */
    public static String getDensity() {
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                "getprop ro.sf.lcd_density", false
        );
        if (result.isSuccess3()) {
            return result.successMsg;
        }
        return null;
    }

    /**
     * 获取屏幕分辨率
     * @return 屏幕分辨率
     */
    public static String getScreenSize() {
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                "wm size", true
        );
        if (result.isSuccess3()) {
            // 正常返回 Physical size: 1080 x 1920
            // 如果使用命令修改过, 那输出可能是
            // Physical size: 1080 x 1920
            // Override size: 480 x 1024
            // 表明设备的屏幕分辨率原本是 1080px * 1920px, 当前被修改为 480px * 1024px
            return result.successMsg;
        }
        return null;
    }

    /**
     * 获取显示屏参数
     * @return 显示屏参数
     */
    public static String getDisplays() {
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                "dumpsys window displays", true
        );
        if (result.isSuccess3()) {
            return result.successMsg;
        }
        return null;
    }

    /**
     * 获取 Android id
     * @return Android id
     */
    public static String getAndroidId() {
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                "settings get secure android_id", true
        );
        if (result.isSuccess3()) {
            return result.successMsg;
        }
        return null;
    }

    /**
     * 获取 IMEI 码
     * @return IMEI 码
     */
    public static String getIMEI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ShellUtils.CommandResult result = ShellUtils.execCmd(
                    "service call iphonesubinfo 1", true
            );
            if (result.isSuccess3()) {
                try {
                    StringBuilder builder = new StringBuilder();
                    String        subStr  = result.successMsg.replaceAll("\\.", "");
                    subStr = subStr.substring(subStr.indexOf('\'') + 1, subStr.indexOf("')"));
                    // 添加数据
                    builder.append(subStr.substring(0, subStr.indexOf('\'')));
                    // 从指定索引开始
                    int index = subStr.indexOf("'", builder.length() + 1);
                    // 再次裁剪
                    subStr = subStr.substring(index + 1);
                    // 添加数据
                    builder.append(subStr.substring(0, subStr.indexOf("'")));
                    // 从指定索引开始
                    index = subStr.indexOf("'", builder.length() + 1);
                    // 再次裁剪
                    subStr = subStr.substring(index + 1);
                    // 最后进行添加
                    builder.append(subStr.split(DevFinal.REGEX.SPACE)[0]);
                    // 返回对应的数据
                    return builder.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            // 在 Android 4.4 及以下版本可通过如下命令获取 IMEI
            ShellUtils.CommandResult result = ShellUtils.execCmd(
                    "dumpsys iphonesubinfo", true
            );
            if (result.isSuccess3()) { // 返回值中的 Device ID 就是 IMEI
                try {
                    String[] arrays = result.successMsg.split(DevFinal.SYMBOL.NEW_LINE);
                    for (String value : arrays) {
                        if (!TextUtils.isEmpty(value)) {
                            if (value.toLowerCase().contains("device")) {
                                // 进行拆分
                                String[] splitArray = value.split(DevFinal.REGEX.SPACE);
                                return splitArray[splitArray.length - 1];
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 获取 IP 地址
     * @return IP 地址
     */
    public static String getIPAddress() {
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                "ifconfig | grep Mask", false
        );
        if (result.isSuccess3()) {
            return result.successMsg;
        } else { // 如果设备连着 Wifi, 可以使用如下命令来查看局域网 IP
            result = ShellUtils.execCmd("ifconfig wlan0", false);
            if (result.isSuccess3()) {
                return result.successMsg;
            } else {
                // 可以看到网络连接名称、启用状态、IP 地址和 Mac 地址等信息
                result = ShellUtils.execCmd("netcfg", false);
                if (result.isSuccess3()) {
                    return result.successMsg;
                }
            }
        }
        return null;
    }

    /**
     * 获取 Mac 地址
     * @return Mac 地址
     */
    public static String getMac() {
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                "cat /sys/class/net/wlan0/address", false
        );
        if (result.isSuccess3()) {
            return result.successMsg;
        }
        return null;
    }

    /**
     * 获取 CPU 信息
     * @return CPU 信息
     */
    public static String getCPU() {
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                "cat /proc/cpuinfo", false
        );
        if (result.isSuccess3()) {
            return result.successMsg;
        }
        return null;
    }

    /**
     * 获取内存信息
     * @return 内存信息
     */
    public static String getMemInfo() {
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                "cat /proc/meminfo", false
        );
        if (result.isSuccess3()) {
            return result.successMsg;
        }
        return null;
    }

    // ==========
    // = 修改设置 =
    // ==========

    /**
     * 设置屏幕大小
     * @param width  屏幕宽度
     * @param height 屏幕高度
     * @return {@code true} success, {@code false} fail
     */
    public static boolean setScreenSize(
            final int width,
            final int height
    ) {
        String cmd = "wm size %sx%s";
        // 执行 shell
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                String.format(cmd, width, height), true
        );
        return result.isSuccess2();
    }

    /**
     * 恢复原分辨率命令
     * @return {@code true} success, {@code false} fail
     */
    public static boolean resetScreen() {
        // 执行 shell
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                "wm size reset", true
        );
        return result.isSuccess2();
    }

    /**
     * 设置屏幕密度
     * @param density 屏幕密度
     * @return {@code true} success, {@code false} fail
     */
    public static boolean setDensity(final int density) {
        // 执行 shell
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                "wm density " + density, true
        );
        return result.isSuccess2();
    }

    /**
     * 恢复原屏幕密度
     * @return {@code true} success, {@code false} fail
     */
    public static boolean resetDensity() {
        // 执行 shell
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                "wm density reset", true
        );
        return result.isSuccess2();
    }

    /**
     * 显示区域 ( 设置留白边距 )
     * @param left   left padding
     * @param top    top padding
     * @param right  right padding
     * @param bottom bottom padding
     * @return {@code true} success, {@code false} fail
     */
    public static boolean setOverscan(
            final int left,
            final int top,
            final int right,
            final int bottom
    ) {
        String cmd = "wm overscan %s,%s,%s,%s";
        // 执行 shell
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                String.format(cmd, left, top, right, bottom), true
        );
        return result.isSuccess2();
    }

    /**
     * 恢复原显示区域
     * @return {@code true} success, {@code false} fail
     */
    public static boolean resetOverscan() {
        // 执行 shell
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                "wm overscan reset", true
        );
        return result.isSuccess2();
    }

    /**
     * 获取亮度是否为自动获取 ( 自动调节亮度 )
     * @return 1 开启、0 未开启、-1 未知
     */
    public static int getScreenBrightnessMode() {
        // 执行 shell
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                "settings get system screen_brightness_mode", true
        );
        if (result.isSuccess3()) {
            try {
                return Integer.parseInt(result.successMsg);
            } catch (Exception ignored) {
            }
        }
        return -1;
    }

    /**
     * 设置亮度是否为自动获取 ( 自动调节亮度 )
     * @param isAuto 是否自动调节
     * @return {@code true} success, {@code false} fail
     */
    public static boolean setScreenBrightnessMode(final boolean isAuto) {
        // 执行 shell
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                "settings put system screen_brightness_mode " + (isAuto ? 1 : 0),
                true
        );
        return result.isSuccess3();
    }

    /**
     * 获取屏幕亮度值
     * @return 屏幕亮度值
     */
    public static String getScreenBrightness() {
        // 执行 shell
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                "settings get system screen_brightness",
                true
        );
        if (result.isSuccess3()) {
            String suc = result.successMsg;
            if (suc.startsWith("\"")) {
                suc = suc.substring(1);
            }
            if (suc.endsWith("\"")) {
                suc = suc.substring(0, suc.length() - 1);
            }
            return suc;
        }
        return null;
    }

    /**
     * 更改屏幕亮度值 ( 亮度值在 0-255 之间 )
     * @param brightness 亮度值
     * @return {@code true} success, {@code false} fail
     */
    public static boolean setScreenBrightness(@IntRange(from = 0, to = 255) final int brightness) {
        if (brightness < 0) {
            return false;
        } else if (brightness > 255) {
            return false;
        }
        // 执行 shell
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                "settings put system screen_brightness " + brightness,
                true
        );
        return result.isSuccess2();
    }

    /**
     * 获取自动锁屏休眠时间 ( 单位毫秒 )
     * @return 自动锁屏休眠时间
     */
    public static String getScreenOffTimeout() {
        // 执行 shell
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                "settings get system screen_off_timeout",
                true
        );
        if (result.isSuccess3()) {
            return result.successMsg;
        }
        return null;
    }

    /**
     * 设置自动锁屏休眠时间 ( 单位毫秒 )
     * <pre>
     *     设置永不休眠 Integer.MAX_VALUE
     * </pre>
     * @param millis 休眠时间 ( 单位毫秒 )
     * @return {@code true} success, {@code false} fail
     */
    public static boolean setScreenOffTimeout(final long millis) {
        if (millis <= 0) {
            return false;
        }
        // 执行 shell
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                "settings put system screen_off_timeout " + millis,
                true
        );
        return result.isSuccess2();
    }

    /**
     * 获取日期时间选项中通过网络获取时间的状态
     * @return 1 允许、0 不允许、-1 未知
     */
    public static int getGlobalAutoTime() {
        // 执行 shell
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                "settings get global auto_time",
                true
        );
        if (result.isSuccess3()) {
            try {
                return Integer.parseInt(result.successMsg);
            } catch (Exception ignored) {
            }
        }
        return -1;
    }

    /**
     * 修改日期时间选项中通过网络获取时间的状态, 设置是否开启
     * @param isOpen 是否设置通过网络获取时间
     * @return {@code true} success, {@code false} fail
     */
    public static boolean setGlobalAutoTime(final boolean isOpen) {
        // 执行 shell
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                "settings put global auto_time " + (isOpen ? 1 : 0),
                true
        );
        return result.isSuccess3();
    }

    /**
     * 关闭 USB 调试模式
     * @return {@code true} success, {@code false} fail
     */
    public static boolean disableADB() {
        // 执行 shell
        ShellUtils.CommandResult result = ShellUtils.execCmd(
                "settings put global adb_enabled 0",
                true
        );
        return result.isSuccess2();
    }

    /**
     * 允许访问非 SDK API
     * <pre>
     *     不需要设备获得 Root 权限
     * </pre>
     * @return 执行结果
     */
    public static int putHiddenApi() {
        String[] cmds = new String[2];
        cmds[0] = "settings put global hidden_api_policy_pre_p_apps 1";
        cmds[1] = "settings put global hidden_api_policy_p_apps 1";
        // 执行 shell
        return ShellUtils.execCmd(cmds, true).result;
    }

    /**
     * 禁止访问非 SDK API
     * <pre>
     *     不需要设备获得 Root 权限
     * </pre>
     * @return 执行结果
     */
    public static int deleteHiddenApi() {
        String[] cmds = new String[2];
        cmds[0] = "settings delete global hidden_api_policy_pre_p_apps";
        cmds[1] = "settings delete global hidden_api_policy_p_apps";
        // 执行 shell
        return ShellUtils.execCmd(cmds, true).result;
    }

    /**
     * 开启无障碍辅助功能
     * @param packageName              应用包名
     * @param accessibilityServiceName 无障碍服务名
     * @return {@code true} success, {@code false} fail
     */
    public static boolean openAccessibility(
            final String packageName,
            final String accessibilityServiceName
    ) {
        if (TextUtils.isEmpty(packageName)) return false;
        if (TextUtils.isEmpty(accessibilityServiceName)) return false;

        String cmd = "settings put secure enabled_accessibility_services %s/%s";
        // 格式化 shell 命令
        String[] cmds = new String[2];
        cmds[0] = String.format(cmd, packageName, accessibilityServiceName);
        cmds[1] = "settings put secure accessibility_enabled 1";
        // 执行 shell
        ShellUtils.CommandResult result = ShellUtils.execCmd(cmds, true);
        return result.isSuccess2();
    }

    /**
     * 关闭无障碍辅助功能
     * @param packageName              应用包名
     * @param accessibilityServiceName 无障碍服务名
     * @return {@code true} success, {@code false} fail
     */
    public static boolean closeAccessibility(final String packageName,
            final String accessibilityServiceName) {
        if (TextUtils.isEmpty(packageName)) return false;
        if (TextUtils.isEmpty(accessibilityServiceName)) return false;

        String cmd = "settings put secure enabled_accessibility_services %s/%s";
        // 格式化 shell 命令
        String[] cmdArrays = new String[2];
        cmdArrays[0] = String.format(cmd, packageName, accessibilityServiceName);
        cmdArrays[1] = "settings put secure accessibility_enabled 0";
        // 执行 shell
        ShellUtils.CommandResult result = ShellUtils.execCmd(cmdArrays, true);
        return result.isSuccess2();
    }

}
