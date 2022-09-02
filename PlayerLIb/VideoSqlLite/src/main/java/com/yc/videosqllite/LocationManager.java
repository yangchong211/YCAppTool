package com.yc.videosqllite;

import com.yc.videotool.VideoLogUtils;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/8/6
 *     desc  : 音视频播放记录本地缓存
 *     revise: 最开始使用greenDao，二级缓存耗时100毫秒左右
 *             磁盘+内存+key缓存+读写优化，耗时大概2到5毫秒左右
 * </pre>
 */
public class LocationManager {

    /**
     * 终极目标
     * 1.开发者可以自由切换缓存模式
     * 2.可以设置内存缓存最大值，设置磁盘缓存的路径
     * 3.能够有增删改查基础方法
     * 4.多线程下安全和脏数据避免
     * 5.代码体积小
     * 6.一键打印存取表结构日志
     * 7.如何一键将本地记录数据上传
     * 8.拓展性和封闭性
     * 9.性能，插入和获取数据，超1000条数据测试
     */

    /**
     * 内存缓存
     */
    private VideoMapCache videoMapCache;
    /**
     * 磁盘缓存
     */
    private SqlLiteCache sqlLiteCache;
    /**
     * 配置类
     */
    private CacheConfig cacheConfig;

    private static class ManagerHolder {
        private static final LocationManager INSTANCE = new LocationManager();
    }

    public static LocationManager getInstance() {
        return ManagerHolder.INSTANCE;
    }

    public void init(CacheConfig cacheConfig){
        this.cacheConfig = cacheConfig;
        VideoLogUtils.setIsLog(cacheConfig.isLog());
        videoMapCache = new VideoMapCache();
        sqlLiteCache = new SqlLiteCache();
        VideoLogUtils.d("LocationManager-----init初始化-");
    }

    public CacheConfig getCacheConfig() {
        if (cacheConfig==null){
            throw new RuntimeException("请先调用init方法进行初始化");
        }
        return cacheConfig;
    }

    /**
     * 存数据
     * url为什么要md5？思考一下……
     *
     * @param url                           链接
     * @param location                      视频数据
     */
    public synchronized void put(String url , VideoLocation location){
        if (!cacheConfig.isEffective()){
            return;
        }
        if (url==null || url.length()==0 || location==null){
            return ;
        }
        /*
         * type
         * 0，表示内存缓存
         * 1，表示磁盘缓存
         * 2，表示内存缓存+磁盘缓存
         */
        long currentTimeMillis1 = System.currentTimeMillis();
        if (cacheConfig.getType() ==1){
            //存储到磁盘中
            sqlLiteCache.put(url,location);
        } else if (cacheConfig.getType() ==2){
            //存储到内存中
            videoMapCache.put(url,location);
            //存储到磁盘中
            sqlLiteCache.put(url,location);
        } else if (cacheConfig.getType()==0){
            //存储到内存中
            videoMapCache.put(url,location);
        } else {
            //存储到内存中
            videoMapCache.put(url,location);
        }
        long currentTimeMillis2 = System.currentTimeMillis();
        VideoLogUtils.d("LocationManager-----put--存数据耗时-"+(currentTimeMillis2-currentTimeMillis1));
    }

    /**
     * 取数据
     * @param url                           链接
     * @return
     */
    public synchronized long get(String url){
        if (!cacheConfig.isEffective()){
            return 0;
        }
        if (url==null || url.length()==0){
            return 0;
        }
        /*
         * type
         * 0，表示内存缓存
         * 1，表示磁盘缓存
         * 2，表示内存缓存+磁盘缓存
         */
        long currentTimeMillis1 = System.currentTimeMillis();
        long position;
        if (cacheConfig.getType() ==1){
            //从磁盘中查找
            position = sqlLiteCache.get(url);
        } else if (cacheConfig.getType() ==2){
            //先从内存中找
            position = videoMapCache.get(url);
            if (position<0){
                //内存找不到，则从磁盘中查找
                position = sqlLiteCache.get(url);
            }
        } else if (cacheConfig.getType()==0){
            //先从内存中找
            position = videoMapCache.get(url);
        } else {
            //先从内存中找
            position = videoMapCache.get(url);
        }
        long currentTimeMillis2 = System.currentTimeMillis();
        VideoLogUtils.d("LocationManager-----get--取数据耗时-"+(currentTimeMillis2-currentTimeMillis1)
                + "---进度-"+position);
        return position;
    }

    /**
     * 移除数据
     * @param url                           链接
     * @return
     */
    public synchronized boolean remove(String url){
        if (!cacheConfig.isEffective()){
            return false;
        }
        if (url==null || url.length()==0){
            return false;
        }
        /*
         * type
         * 0，表示内存缓存
         * 1，表示磁盘缓存
         * 2，表示内存缓存+磁盘缓存
         */
        if (cacheConfig.getType() ==1){
            return sqlLiteCache.remove(url);
        } else if (cacheConfig.getType() ==2){
            boolean remove = videoMapCache.remove(url);
            boolean removeSql = sqlLiteCache.remove(url);
            return remove || removeSql;
        } else if (cacheConfig.getType()==0){
            return videoMapCache.remove(url);
        } else {
            return videoMapCache.remove(url);
        }
    }

    /**
     * 是否包含
     * @param url                           链接
     * @return
     */
    public synchronized boolean containsKey(String url){
        if (!cacheConfig.isEffective()){
            return false;
        }
        if (url==null || url.length()==0){
            return false;
        }
        /*
         * type
         * 0，表示内存缓存
         * 1，表示磁盘缓存
         * 2，表示内存缓存+磁盘缓存
         */
        boolean containsKey;
        if (cacheConfig.getType() ==1){
            containsKey = sqlLiteCache.containsKey(url);
        } else if (cacheConfig.getType() ==2){
            containsKey = videoMapCache.containsKey(url);
            if (!containsKey){
                containsKey = sqlLiteCache.containsKey(url);
                return containsKey;
            }
        } else if (cacheConfig.getType()==0){
            containsKey = videoMapCache.containsKey(url);
        } else {
            containsKey = videoMapCache.containsKey(url);
        }
        return containsKey;
    }

    /**
     * 清楚所有数据
     * @return                              是否清楚完毕
     */
    public synchronized void clearAll(){
        if (!cacheConfig.isEffective()){
            return;
        }
        /*
         * type
         * 0，表示内存缓存
         * 1，表示磁盘缓存
         * 2，表示内存缓存+磁盘缓存
         */
        if (cacheConfig.getType() ==1){
            sqlLiteCache.clearAll();
        } else if (cacheConfig.getType() ==2){
            videoMapCache.clearAll();
            sqlLiteCache.clearAll();
        } else if (cacheConfig.getType()==0){
            videoMapCache.clearAll();
        } else {
            videoMapCache.clearAll();
        }
    }

}
