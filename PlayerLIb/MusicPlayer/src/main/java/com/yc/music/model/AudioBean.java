package com.yc.music.model;

import java.io.Serializable;

/**
 * <pre>
 *     @author yangchong
 *     blog  : www.pedaily.cn
 *     time  : 2018/03/22
 *     desc  : 音频单曲信息
 *     revise:
 * </pre>
 */
public class AudioBean implements Serializable {

    // [本地歌曲]歌曲id
    private String id;
    // 音乐标题
    private String title;
    // 艺术家
    private String artist;
    // 专辑
    private String album;
    // [本地歌曲]专辑ID
    private long albumId;
    // [在线歌曲]专辑封面路径
    private String coverPath;
    // 持续时间
    private long duration;
    // 音乐路径
    private String path;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    /**
     * 思考为什么要重写这两个方法
     * 对比本地歌曲是否相同
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AudioBean) {
            AudioBean bean = (AudioBean) obj;
            return this.id.equals(bean.getId());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

}
