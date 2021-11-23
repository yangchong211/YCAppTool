package com.yc.location.utils;

import android.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class TransformUtils {

    private static final double pi = 3.14159265358979324;

    //
    // Krasovsky 1940
    //
    // a = 6378245.0, 1/f = 298.3
    // b = a * (1 - f)
    // ee = (a^2 - b^2) / a^2;
    private static final double a = 6378245.0;
    private static final double ee = 0.00669342162296594323;

    //
    // World Geodetic System ==> Mars Geodetic System
    public static double[] transform(double wgLon, double wgLat) {
        double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
        double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
        double radLat = wgLat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        return new double[]{wgLon + dLon, wgLat + dLat};
    }

    /**
     * 计算两个定位点的距离
     * @param lon1                  定位点1的经度
     * @param lat1                  定位点1的纬度
     * @param lon2                  定位点2的经度
     * @param lat2                  定位点2的纬度
     * @return                      两点距离，单位米
     */
    public static double calcDistance(double lon1, double lat1, double lon2, double lat2) {
        double Rc = 6378137.0;
        double Rj = 6356725.0;
        double r_lat_1 = lat1 * pi / 180.0;
        double r_lng_1 = lon1 * pi / 180.0;
        double r_lat_2 = lat2 * pi / 180.0;
        double r_lng_2 = lon2 * pi / 180.0;

        double Ec = Rj + (Rc - Rj) * (90.0 - lat1) / 90.0;
        double Ed = Ec * Math.cos(r_lat_1);
        double dx = (r_lng_2 - r_lng_1) * Ed;
        double dy = (r_lat_2 - r_lat_1) * Ec;

        return Math.sqrt(dx * dx + dy * dy);
    }

    public static boolean outOfChina(double lon, double lat) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }

    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
        return ret;
    }


    private static final List<Pair<Double, Double>> mMainTaiWanBoundary = new ArrayList<>();
    private static final List<Pair<Double, Double>> mJinMenBoundary = new ArrayList<>();
    private static final List<List<Pair<Double, Double>>> mTaiWanPolygons = new ArrayList<>();
    private static final List<Pair<Double, Double>> mJapanBoundary = new ArrayList<>();
    private static final List<List<Pair<Double, Double>>> mJapanPolygons = new ArrayList<>();

    static {
        mMainTaiWanBoundary.add(new Pair<>(121.600456, 25.522615));
        mMainTaiWanBoundary.add(new Pair<>(122.424431, 24.866503));
        mMainTaiWanBoundary.add(new Pair<>(121.336784, 21.053744));
        mMainTaiWanBoundary.add(new Pair<>(119.425163, 23.684774));
        mJinMenBoundary.add(new Pair<>(118.187034, 24.435898));
        mJinMenBoundary.add(new Pair<>(118.436286, 24.547123));
        mJinMenBoundary.add(new Pair<>(118.520744, 24.426520));
        mJinMenBoundary.add(new Pair<>(118.254325, 24.362735));
        mTaiWanPolygons.add(mMainTaiWanBoundary);
        mTaiWanPolygons.add(mJinMenBoundary);
        mJapanBoundary.add(new Pair<>(140.522690, 45.644768));
        mJapanBoundary.add(new Pair<>(149.179916, 45.644768));
        mJapanBoundary.add(new Pair<>(144.785385, 35.817813));
        mJapanBoundary.add(new Pair<>(130.634995, 29.343875));
        mJapanBoundary.add(new Pair<>(127.470932, 32.76880));
        mJapanPolygons.add(mJapanBoundary);
    }

    /**
     * 判断经纬度是否在台湾范围内。注意：请传入wgs84坐标系的经纬度
     * 以传入点为起点的射线（Lng = lng (Lat > lat)）与台湾、金门两个多边形相交的点数来判读在多边形内外
     *
     * @param lng wgs84坐标系下的经度
     * @param lat wgs84坐标系下的纬度
     * @return
     */
    public static boolean isInTaiWan(double lng, double lat) {
        return isInArea(lng, lat, mTaiWanPolygons);
    }

    public static boolean isInJapan(double lng, double lat) {
        return isInArea(lng, lat, mJapanPolygons);
    }

    private static boolean isInArea(double lng, double lat, List<List<Pair<Double, Double>>> areaPolygons) {
        Set<Pair<Double, Double>> intersectPoints = new HashSet<>();
        for (List<Pair<Double, Double>> polygon : areaPolygons) {
            int boundarySize = polygon.size();
            for (int i = 0; i < boundarySize; i++) {
                int next = i + 1;
                if (i == boundarySize - 1) {
                    next = 0;
                }
                double x1 = polygon.get(next).first;
                double x0 = polygon.get(i).first;
                double y1 = polygon.get(next).second;
                double y0 = polygon.get(i).second;
                if ((lat >= y0 && lat >= y1)
                        || (x1 - lng) * (lng - x0) < 0
                        || Math.abs(x1 - x0) < 0.0000001f) {
                    continue;
                }
                double insetLat = (y1 * (lng - x0) + y0 * (x1 - lng)) / (x1 - x0);
                if (insetLat > lat && (y1 - insetLat) * (insetLat - y0) >= 0) {
                    intersectPoints.add(new Pair<>(lng, insetLat));
                }
            }
        }
        return intersectPoints.size() % 2 == 1;
    }

    /**
     * 是否在国外及台湾（是否使用WGS84坐标系）
     *
     * @param lng 定位经度
     * @param lat 定位纬度
     * @return
     */
    public static boolean isOutOfMainLand(double lng, double lat) {
        return TransformUtils.outOfChina(lng, lat)
                || TransformUtils.isInTaiWan(lng, lat);
    }
}
