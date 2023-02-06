package com.yc.m3u8.bean;

import androidx.annotation.NonNull;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : m3u8切片类
 *     revise:
 * </pre>
 */
public class M3u8Ts implements Comparable<M3u8Ts> {

    private String file;
    private float seconds;

    public M3u8Ts(String file, float seconds) {
        this.file = file;
        this.seconds = seconds;
    }

    public String getFile() {
        return file;
    }

    /**
     * 获取文件名字，支取***.ts
     * @return
     */
    public String getFileName() {
        String fileName = file.substring(file.lastIndexOf("/") + 1);
        if (fileName.contains("?")) {
            return fileName.substring(0, fileName.indexOf("?"));
        }
        return fileName;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public float getSeconds() {
        return seconds;
    }

    public void setSeconds(float seconds) {
        this.seconds = seconds;
    }

    @Override
    public String toString() {
        return file + " (" + seconds + "sec)";
    }

    /**
     * 获取时间
     */
    public long getLongDate() {
        try {
            return Long.parseLong(file.substring(0, file.lastIndexOf(".")));
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public int compareTo(@NonNull M3u8Ts o) {
        return file.compareTo(o.file);
    }
}
