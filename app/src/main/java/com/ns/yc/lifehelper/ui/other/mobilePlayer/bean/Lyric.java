package com.ns.yc.lifehelper.ui.other.mobilePlayer.bean;

/**
 * 歌词JavaBean，一个JavaBean代表一行歌词
 * Created by dzl on 2016/10/6.
 */

public class Lyric implements Comparable<Lyric>{

    /** 歌词的开始显示时间 */
    public int startShowTime;

    /** 要显示的歌词内容 */
    public String text;

    public Lyric(String text, int startShowTime) {
        this.text = text;
        this.startShowTime = startShowTime;
    }

    @Override
    public String toString() {
        return "Lyric{" +
                "startShowTime=" + startShowTime +
                ", text='" + text + '\'' +
                '}';
    }

    /** 实现这个方法是为了排序 */
    @Override
    public int compareTo(Lyric o) {
        // 通过startShowTime进行排序
        return Integer.valueOf(startShowTime).compareTo(o.startShowTime);
    }
}
