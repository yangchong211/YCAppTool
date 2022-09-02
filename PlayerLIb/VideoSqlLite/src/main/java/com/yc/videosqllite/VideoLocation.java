package com.yc.videosqllite;


import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Objects;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/8/6
 *     desc  : 音视频bean
 *     revise: 必须
 * </pre>
 */
public class VideoLocation implements Serializable , Cloneable{

    /**
     * 视频链接
     */
    private String url;
    /**
     * 视频链接md5
     */
    private String urlMd5;
    /**
     * 视频播放位置
     */
    private long position;
    /**
     * 视频总时间
     */
    private long totalTime;

    public VideoLocation(){

    }

    public VideoLocation(String url, long position, long totalTime) {
        this.url = url;
        this.position = position;
        this.totalTime = totalTime;
    }

    /*public VideoLocation(String url, String urlMd5, long position, long totalTime) {
        this.url = url;
        this.urlMd5 = urlMd5;
        this.position = position;
        this.totalTime = totalTime;
    }*/

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlMd5() {
        return urlMd5;
    }

    public void setUrlMd5(String urlMd5) {
        this.urlMd5 = urlMd5;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }



    public String toJson() {
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("url", getUrl());
            jsonObject.put("urlMd5", getUrlMd5());
            jsonObject.put("position", getPosition());
            jsonObject.put("totalTime", getTotalTime());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static VideoLocation toObject(String jsonStr) {
        VideoLocation m =  new VideoLocation();
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            m.setUrl(jsonObject.has("url") ? jsonObject.getString("url"):null);
            m.setUrlMd5(jsonObject.has("urlMd5") ? jsonObject.getString("urlMd5"):null);
            m.setPosition(jsonObject.has("position") ? jsonObject.getLong("position"):0);
            m.setTotalTime(jsonObject.has("totalTime") ? jsonObject.getLong("totalTime"):0);
            return m;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return m;
    }

    @Override
    public String toString() {
        return "VideoLocation{" +
                "url='" + url + '\'' +
                ", urlMd5='" + urlMd5 + '\'' +
                ", position=" + position +
                ", totalTime=" + totalTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VideoLocation location = (VideoLocation) o;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return position == location.position &&
                    totalTime == location.totalTime &&
                    Objects.equals(url, location.url) &&
                    Objects.equals(urlMd5, location.urlMd5);
        } else {
            return position == location.position &&
                    totalTime == location.totalTime &&
                    equals(url,location.url) &&
                    equals(urlMd5,location.urlMd5);
        }
    }

    @Override
    public int hashCode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Objects.hash(url, urlMd5, position, totalTime);
        }
        return hash(url,urlMd5,position,totalTime);
    }

    /**
     * 重写clone()方法
     * 浅拷贝
     * @return
     */
    public Object clone() {
        //浅拷贝
        try {
            // 直接调用父类的clone()方法
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    /**
     * 重写clone()方法
     * 深拷贝
     * @return
     */
    public Object deepClone() {
        // 深拷贝，创建拷贝类的一个新对象，这样就和原始对象相互独立
        VideoLocation location = new VideoLocation(url,position,totalTime);
        return location;
    }

    /**
     * 比较两个对象
     * @param a                         a对象
     * @param b                         b对象
     * @return
     */
    private boolean equals(Object a, Object b) {
        boolean ab = (a == b);
        boolean equal = (a != null && a.equals(b));
        return ab || equal;
    }

    /**
     * hash算法
     * @param values                    参数
     * @return
     */
    private int hash(Object... values) {
        return hashCode(values);
    }

    private int hashCode(Object a[]) {
        if (a == null){
            return 0;
        }
        int result = 1;
        for (Object element : a){
            result = 31 * result + (element == null ? 0 : element.hashCode());
        }
        return result;
    }
}
