package com.ns.yc.lifehelper.ui.other.mobilePlayer.utils;


import com.ns.yc.lifehelper.ui.other.mobilePlayer.bean.Lyric;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

/** 歌词加载器 */
public class LyricLoader {

    /**
     * 加载音乐对应的歌词文件
     * @param musicPath 音乐路径
     * @return
     */
    public static ArrayList<Lyric> loadLyric(String musicPath) {
        ArrayList<Lyric> lyrics = null;

        // 1、把音乐文件的扩展名换成歌词的扩展名
        String prefix = musicPath.substring(0, musicPath.lastIndexOf(".")); // 去除.mp3
        File lrcFile = new File(prefix + ".lrc");
        File txtFile = new File(prefix + ".txt");

        if (lrcFile.exists()) { // 如果歌词文件存在，则读取文件内容
            lyrics = readLyricFile(lrcFile);
        } else if (txtFile.exists()){
            lyrics = readLyricFile(txtFile);
        }

        if (lyrics != null && !lyrics.isEmpty()) {
            Collections.sort(lyrics);   // 如果歌词，则对歌词进行排序
        }
        return lyrics;
    }

    /**
     * 读取歌词文件
     * @param file
     * @return
     */
    private static ArrayList<Lyric> readLyricFile(File file) {
        ArrayList<Lyric> lyrics = new ArrayList<Lyric>();
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "GBK"));

            // 2、读取歌词文件（BufferReader），一行一行地读
            String line;
            while ((line = reader.readLine()) != null) {
//                按]进行分割：
                String[] strings = line.split("]");
                // 遍历所有的时间，每个时间都对应了一个歌词的JavaBean
                String lyricText = strings[strings.length - 1]; // 取出歌词文本

                // 遍历所有的时间
                for (int i = 0; i < strings.length - 1; i++) {
                    int startShowTime = parseTime(strings[i]);    // 解析这样的时间：[02:23.36
                    lyrics.add(new Lyric(lyricText, startShowTime));
                }
            }

            // 3、把一行一行的歌词解析成JavaBean
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(in);
        }
        return lyrics;
    }

    /** 解析这样的时间：[02:23.36 */
    private static int parseTime(String time) {
        String minuteStr = time.substring(1, 3);    // 取出[02:23.36中的02
        String sencondStr = time.substring(4, 6);   // 取出[02:23.36中的23
        String millsStr = time.substring(7, 9);     // 取出[02:23.36中的36

        int minuteMills = Integer.valueOf(minuteStr) * 60 * 1000;
        int sencondMills = Integer.valueOf(sencondStr) * 1000;
        int mills = Integer.valueOf(millsStr) * 10;
        return minuteMills + sencondMills + mills;
    }

    private static void close(InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
