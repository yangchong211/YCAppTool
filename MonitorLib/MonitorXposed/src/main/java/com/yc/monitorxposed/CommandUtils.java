package com.yc.monitorxposed;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : 指令工具类
 *     revise:
 * </pre>
 */
public final class CommandUtils {

    public static String getProperty(String propName) {
        String value = null;
        Object roSecureObj;
        try {
            Class<?> aClass = Class.forName("android.os.SystemProperties");
            roSecureObj = aClass.getMethod("get", String.class).invoke(null, propName);
            if (roSecureObj != null){
                value = (String) roSecureObj;
            }
        } catch (Exception e) {
            value = null;
        } finally {
            return value;
        }
    }

    public static String exec(String command) {
        BufferedOutputStream bufferedOutputStream = null;
        BufferedInputStream bufferedInputStream = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("sh");
            bufferedOutputStream = new BufferedOutputStream(process.getOutputStream());

            bufferedInputStream = new BufferedInputStream(process.getInputStream());
            bufferedOutputStream.write(command.getBytes());
            bufferedOutputStream.write('\n');
            bufferedOutputStream.flush();
            bufferedOutputStream.close();

            process.waitFor();

            String outputStr = getStrFromBufferInputSteam(bufferedInputStream);
            return outputStr;
        } catch (Exception e) {
            return null;
        } finally {
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (process != null) {
                process.destroy();
            }
        }
    }

    /**
     * 读取流转变化为字符串
     * @param bufferedInputStream                           流
     * @return
     */
    private static String getStrFromBufferInputSteam(BufferedInputStream bufferedInputStream) {
        if (null == bufferedInputStream) {
            return "";
        }
        int BUFFER_SIZE = 512;
        byte[] buffer = new byte[BUFFER_SIZE];
        StringBuilder result = new StringBuilder();
        try {
            while (true) {
                int read = bufferedInputStream.read(buffer);
                if (read > 0) {
                    result.append(new String(buffer, 0, read));
                }
                if (read < BUFFER_SIZE) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
