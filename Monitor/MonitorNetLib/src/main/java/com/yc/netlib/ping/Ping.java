package com.yc.netlib.ping;

import android.content.Context;
import android.os.Environment;


import com.yc.netlib.utils.NetLogUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ping {

    private static final String TAG = "Ping";
    private static Runtime runtime;
    private static Process process;
    private static File pingFile;
    /**
     * Jdk1.5的InetAddresss,代码简单
     * @param ipAddress
     * @throws Exception
     */
    public static boolean ping(String ipAddress) throws Exception {
        // 超时应该在3钞以上
        int timeOut = 3000;
        // 当返回值是true时，说明host是可用的，false则不可。
        boolean status = InetAddress.getByName(ipAddress).isReachable(timeOut);
        return status;
    }

    /**
     * 使用java调用cmd命令,这种方式最简单，可以把ping的过程显示在本地。ping出相应的格式
     * @param url
     * @throws Exception
     */
    public static void ping1(Context context , String url) throws Exception {
        String line = null;
        // 获取主机名
        URL transUrl = null;
        File commonFilePath = new File(getPingPath(context));
        if (!commonFilePath.exists()) {
            commonFilePath.mkdirs();
            NetLogUtils.w(TAG, "create path: " + commonFilePath);
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = df.format(new Date());
        String file = "result" + date + ".txt";
        pingFile = new File(commonFilePath,file);
        try {
            transUrl = new URL(url);
            String hostName = transUrl.getHost();
            NetLogUtils.e(TAG, "hostName: " + hostName);
            runtime = Runtime.getRuntime();
            process = runtime.exec("ping " + hostName);
            BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
            int k = 0;
            while ((line = buf.readLine()) != null) {
                if (line.length() > 0 && line.indexOf("time=") > 0) {
                    String string = line.substring(line.indexOf("time="));
                    int index = string.indexOf("time=");
                    String str = string.substring(index + 5, index + 9);
                    NetLogUtils.e(TAG, "time=: " + str);
                    String result = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss")
                            .format(new Date()) + ", " + hostName + ", " + str + "\r\n";
                    NetLogUtils.e(TAG, "result: " + result);
                    write(pingFile, result);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    /**
     * 使用java调用控制台的ping命令，这个比较可靠，还通用.
     * 使用起来方便：传入个ip，设置ping的次数和超时，就可以根据返回值来判断是否ping通。
     */
    public static boolean ping2(String ipAddress, int pingTimes, int timeOut) {
        BufferedReader in = null;
        // 将要执行的ping命令,此命令是windows格式的命令
        Runtime r = Runtime.getRuntime();
        String pingCommand = "ping " + ipAddress + " -n " + pingTimes + " -w " + timeOut;
        try {
            // 执行命令并获取输出
            System.out.println(pingCommand);
            Process p = r.exec(pingCommand);
            if (p == null) {
                return false;
            }
            // 逐行检查输出,计算类似出现=23ms TTL=62字样的次数
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            int connectedCount = 0;
            String line = null;
            while ((line = in.readLine()) != null) {
                connectedCount += getCheckResult(line);
            }
            // 如果出现类似=23ms TTL=62这样的字样,出现的次数=测试次数则返回真
            return connectedCount == pingTimes;
        } catch (Exception ex) {
            // 出现异常则返回假
            ex.printStackTrace();
            return false;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 停止运行ping
     */
    public static void killPing() {
        if (process != null) {
            process.destroy();
            NetLogUtils.e(TAG, "process: " + process);
        }
    }

    /**
     * 目录地址
     * SDCard/Android/data/<application package>/ping
     * data/data/<application package>/ping
     */
    public static String getPingPath(Context context) {
        String path = getCachePath(context) + File.separator + "ping";
        return path;
    }

    /**
     * 获取app缓存路径
     * SDCard/Android/data/<application package>/cache
     * data/data/<application package>/cache
     *
     * @param context
     * @return
     */
    public static String getCachePath(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            //外部存储可用
            if (context.getExternalCacheDir()!=null){
                cachePath = context.getExternalCacheDir().getAbsolutePath();
            } else {
                cachePath = context.getCacheDir().getAbsolutePath();
            }
        } else {
            //外部存储不可用
            cachePath = context.getCacheDir().getAbsolutePath();
        }
        return cachePath;
    }

    public static void write(File file, String content) {
        BufferedWriter out = null;
        NetLogUtils.e(TAG, "file: " + file);
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            out.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // 若line含有=18ms TTL=16字样,说明已经ping通,返回1,否則返回0.
    private static int getCheckResult(String line) { // System.out.println("控制台输出的结果为:"+line);
        Pattern pattern = Pattern.compile("(\\d+ms)(\\s+)(TTL=\\d+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            return 1;
        }
        return 0;
    }

}
