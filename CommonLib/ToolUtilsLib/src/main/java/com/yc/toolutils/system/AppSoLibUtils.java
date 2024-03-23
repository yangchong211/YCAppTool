package com.yc.toolutils.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2022/07/21
 *     desc  : so库工具类
 *     revise:
 * </pre>
 */
public final class AppSoLibUtils {

    private static final HashSet<String> ALL_SO_LISTS = new HashSet<>();

    /**
     * 获取全部已加载的SO库
     */
    public static String getAllSoLoaded() {
        ALL_SO_LISTS.clear();
        // 当前应用的进程ID
        int pid = android.os.Process.myPid();
        String path = "/proc/" + pid + "/maps";
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            readFileByLines(file.getAbsolutePath());
        } else {
            AppLogUtils.e("DAVIS", "不存在[" + path + "]文件.");
        }
        StringBuilder sb = new StringBuilder();
        for (String next : ALL_SO_LISTS) {
            sb.append(next).append("\n");
        }
        AppLogUtils.e("DAVIS", sb.toString());
        return sb.toString() + "\n 搜索完毕";
    }

    /**
     * 获取当前应用已加载的SO库
     */
    public static String getCurrSoLoaded() {
        ALL_SO_LISTS.clear();
        // 当前应用的进程ID
        int pid = android.os.Process.myPid();
        String path = "/proc/" + pid + "/maps";
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            readFileByLines(file.getAbsolutePath());
        } else {
            AppLogUtils.d("DAVIS: "+"不存在[" + path + "]文件.");
        }
        StringBuilder sb = new StringBuilder();
        for (String next : ALL_SO_LISTS) {
            if (next.startsWith("/data/app/")){
                sb.append(next).append("\n");
            }
        }
        AppLogUtils.d("DAVIS: "+sb.toString());
        return sb.toString() + "";
    }

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    private static void readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                if (tempString.endsWith(".so")) {
                    int index = tempString.indexOf("/");
                    if (index != -1) {
                        String str = tempString.substring(index);
                        // 所有so库（包括系统的，即包含/system/目录下的）
                        ALL_SO_LISTS.add(str);
                    }
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
