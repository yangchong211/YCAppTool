package com.yc.serialport;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public final class SerialPort {

    private static final String TAG = "SerialPort";
    private final FileDescriptor mFd;
    private final FileInputStream mFileInputStream;
    private final FileOutputStream mFileOutputStream;
    /**
     * 有些设备su路径是/system/xbin/su
     */
    private static String mSuPath = "/system/bin/su";

    static {
        System.loadLibrary("SerialPort");
    }

    public static void setSuPath(String suPath) {
        if (TextUtils.isEmpty(suPath)) {
            return;
        }
        mSuPath = suPath;
    }

    /**
     * 打开串口
     * @param device 串口路径，根据你的设备不同，路径也不同
     * @param baudrate 波特率
     * @param flags flags 给0就好
     */
    public SerialPort(File device, int baudrate, int flags) throws SecurityException, IOException {
        /* 检查访问权限 */
        if (!device.canRead() || !device.canWrite()) {
            try {
                /* 没有读/写权限，尝试对文件进行提权 */
                Process su = Runtime.getRuntime().exec(mSuPath);
                String cmd = "chmod 666 " + device.getAbsolutePath() + "\n" + "exit\n";
                su.getOutputStream().write(cmd.getBytes());
                if ((su.waitFor() != 0) || !device.canRead() || !device.canWrite()) {
                    throw new SecurityException();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new SecurityException();
            }
        }
        /**
         * 不要删除或重命名字段mFd:原生方法close()使用了该字段
         */
        mFd = open(device.getAbsolutePath(), baudrate, flags);
        if (mFd == null) {
            Log.i(TAG, "open() return null");
            throw new IOException();
        }
        mFileInputStream = new FileInputStream(mFd);
        mFileOutputStream = new FileOutputStream(mFd);
    }

    /**
     * 打开串口
     *
     * @param path     设备路径
     * @param baudrate 波特率
     * @param flags
     * @return FileDescriptor
     */
    private native static FileDescriptor open(String path, int baudrate, int flags);

    /**
     * 关闭串口
     */
    public native void close();

    public InputStream getInputStream() {
        return mFileInputStream;
    }

    public OutputStream getOutputStream() {
        return mFileOutputStream;
    }
}
