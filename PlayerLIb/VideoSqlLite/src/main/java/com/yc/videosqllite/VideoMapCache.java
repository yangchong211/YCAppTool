package com.yc.videosqllite;


import com.yc.applrucache.SystemLruCache;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/8/6
 *     desc  : 内存缓存
 *     revise:
 * </pre>
 */
public class VideoMapCache {

    /**
     * 缓存
     */
    private final SystemLruCache<String, VideoLocation> mCache;
    private final SafeKeyGenerator safeKeyGenerator;

    public VideoMapCache(){
        //默认设置存储最大值为1000条
        mCache =  new SystemLruCache<>(1000);
        this.safeKeyGenerator = new SafeKeyGenerator();
    }

    /**
     * 存数据
     * @param url                           链接
     * @param location                      视频数据
     */
    public synchronized void put(String url , VideoLocation location){
        if (url==null || url.length()==0){
            return;
        }
        if (location==null){
            return;
        }
        //String key = VideoMd5Utils.encryptMD5ToString(url, cacheConfig.getSalt());
        String safeKey = safeKeyGenerator.getSafeKey(url);
        location.setUrlMd5(safeKey);
        mCache.put(safeKey,location);
    }

    /**
     * 取数据
     * @param url                           链接
     * @return
     */
    public synchronized long get(String url){
        if (url==null || url.length()==0){
            return -1;
        }
        //String key = VideoMd5Utils.encryptMD5ToString(url, cacheConfig.getSalt());
        String key = safeKeyGenerator.getSafeKey(url);
        VideoLocation videoLocation = mCache.get(key);
        if (videoLocation==null){
            //没找到
            return -1;
        }
        if (videoLocation.getTotalTime()<=videoLocation.getPosition()){
            //这一步主要是避免外部开发员瞎存播放进度
            return -1;
        }
        long position = videoLocation.getPosition();
        if (position<0){
            position = -1;
        }
        return position;
    }

    /**
     * 移除数据
     * @param url                           链接
     * @return
     */
    public synchronized boolean remove(String url){
        if (url==null || url.length()==0){
            return false;
        }
        //String key = VideoMd5Utils.encryptMD5ToString(url, cacheConfig.getSalt());
        String key = safeKeyGenerator.getSafeKey(url);
        VideoLocation location = mCache.remove(key);
        if (location==null){
            return false;
        } else {
            //移除成功
            return true;
        }
    }

    /**
     * 是否包含
     * @param url                           链接
     * @return
     */
    public synchronized boolean containsKey(String url){
        if (url==null || url.length()==0){
            return false;
        }
        //String key = VideoMd5Utils.encryptMD5ToString(url, cacheConfig.getSalt());
        String key = safeKeyGenerator.getSafeKey(url);
        return mCache.containsKey(key);
    }


    /**
     * 清楚所有数据
     * @return                              是否清楚完毕
     */
    public synchronized boolean clearAll(){
        mCache.clear();
        int size = mCache.size();
        if (size==0){
            return true;
        } else {
            return false;
        }
    }

}
