/*
Copyright 2017 yangchong211（github.com/yangchong211）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.yc.video.config;


import java.io.Serializable;
import java.util.Map;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/1/29
 *     desc  : 视频信息实体类
 *     revise:
 * </pre>
 */
public class VideoInfoBean implements Serializable {

    /**
     * 视频的标题
     */
    private String title;
    /**
     * 播放的视频地址
     */
    private String videoUrl;
    /**
     * 请求header
     */
    private Map<String, String> headers;
    /**
     * 视频封面
     */
    private String cover;
    /**
     * 视频时长
     */
    private long length;
    /**
     * 清晰度等级
     */
    private String grade;
    /**
     * 270P、480P、720P、1080P、4K ...
     */
    private String p;

    public VideoInfoBean(String title, String cover, String url) {
        this.title = title;
        this.videoUrl = url;
        this.cover = cover;
    }

    public VideoInfoBean(String title ,String grade, String p, String videoUrl) {
        this.title = title;
        this.grade = grade;
        this.p = p;
        this.videoUrl = videoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }
}
