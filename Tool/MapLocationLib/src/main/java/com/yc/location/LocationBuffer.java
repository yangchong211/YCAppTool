package com.yc.location;


import com.yc.location.utils.TransformUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class LocationBuffer {

    private long lastPushTimestamp;
    private Queue<LonLat> locBuffer = new LinkedList<>();

    private final long TIMEOUT = 60*1000L;
    private final long DISTOUT = 10*1000L;
    private final int MAXLEN = 10;

    public LocationBuffer() {
        locBuffer.clear();
        lastPushTimestamp = 0L;
    }

    public void clear() {
        locBuffer.clear();
        lastPushTimestamp = 0L;
    }

    public boolean isCompatible(LocationServiceResponse response) {

        if(response == null || response.locations.size() <= 0) {
            return false;
        }

        LonLat curLonLat = getMaxConfiPoint(response);

        // 缓存集合为空，直接添加
        if(locBuffer.size() <= 0) {

            lastPushTimestamp = System.currentTimeMillis();
            locBuffer.add(curLonLat);

            return true;
        }

        // 缓存集合不为空，考察时间与空间的兼容性
        long nowTimestamp = System.currentTimeMillis();

        // 超时，清空并添加
        if(nowTimestamp - lastPushTimestamp > TIMEOUT) {
            locBuffer.clear();
            locBuffer.add(curLonLat);
            lastPushTimestamp = nowTimestamp;

            return true;
        }

        // 计算各距离差, 统计跨越数量
        int distOutCount = 0;
        for(LonLat each : locBuffer) {
            if(TransformUtils.calcdistance(
                    curLonLat.lon, curLonLat.lat,
                    each.lon, each.lat) > DISTOUT) {
                distOutCount++;
            }
        }

        // 无跨越点
        if(distOutCount <= 0) {
            addToBuffer(curLonLat);
            lastPushTimestamp = nowTimestamp;

            return true;
        }

        // 判断为跨越
        if(distOutCount == locBuffer.size()) {
            return false;
        }

        // 复杂情形，暂清空处理
        locBuffer.clear();
        locBuffer.add(curLonLat);
        lastPushTimestamp = nowTimestamp;

        return true;

    }

    private void addToBuffer(LonLat lonlat) {
        if(locBuffer.size() >= MAXLEN) {
            locBuffer.poll();
        }
        locBuffer.add(lonlat);
    }

    private LonLat getMaxConfiPoint(LocationServiceResponse response) {

        List<location_info_t> locs = response.locations;

        location_info_t max = locs.get(0);
        for(int i = 1; i < locs.size(); i++) {
            if(locs.get(i).confidence > max.confidence) {
                max = locs.get(i);
            }
        }

        return new LonLat(max.lon_gcj, max.lat_gcj);
    }

    class LonLat {
        double lon;
        double lat;

        public LonLat(double p1, double p2) {
            lon = p1;
            lat = p2;
        }
    }
}
