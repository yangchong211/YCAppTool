package com.ns.yc.lifehelper.ui.other.mobilePlayer.bean;

import android.database.Cursor;
import android.provider.MediaStore;

import java.io.Serializable;

/**
 * Created by PC on 2017/10/10.
 * 作者：PC
 */

public class VideoItem implements Serializable {

    // Media._ID, Media.TITLE, Media.SIZE, Media.DURATION, Media.DATA
    public String title;
    /** 视频的保存路径 */
    public String data;
    public long size;
    public long duration;

    /**
     * 把Cursor转换为一个视频JavaBean
     * @param cursor
     * @return
     */
    public static VideoItem fromCursor(Cursor cursor) {
        VideoItem item = new VideoItem();
        item.title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
        item.data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        item.size = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
        item.duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
        return item;
    }

}
