package com.yc.tracesdk;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

public class NmeaParser {

    private static final String TAG = "NmeaParser";

    private long mLastParseTimeMillis;

    private static final TimeZone sUtcTimeZone = TimeZone.getTimeZone("UTC");

    private static final float KNOTS_TO_METERS_PER_SECOND = 0.51444444444f;

    private final String mName;

    private int mYear = -1;
    private int mMonth;
    private int mDay;

    private Location mLocation = null;
    private Bundle mExtras;

    public final static String PDOP = "pdop";
    public final static String HDOP = "hdop";
    public final static String VDOP = "vdop";
    public final static String NUM_SATELLITES = "num_satellites";

    public NmeaParser(String name) {
        mName = name;
        mLocation = new Location(mName);
    }

    /**
     * 更新gps时间
     * 
     * @param time
     *            gps时间
     * @return {@code true}如果更新成功，{@code false}如果更新失败
     */
    private boolean updateGpsTime(String time) {
        if (time.length() < 6) {
            return false;
        }

        if (mYear == -1) {
            mLocation.setTime(0);
            return false;
        }

        int hour, minute;
        float second;
        try {
            hour = Integer.parseInt(time.substring(0, 2));
            minute = Integer.parseInt(time.substring(2, 4));
            second = Float.parseFloat(time.substring(4, time.length()));
        } catch (NumberFormatException nfe) {
            Log.e(TAG, "Error parsing timestamp " + time);
            mLocation.setTime(0);
            return false;
        }

        int isecond = (int) second;
        int millis = (int) ((second - isecond) * 1000);
        Calendar c = new GregorianCalendar(sUtcTimeZone);
        c.set(mYear, mMonth, mDay, hour, minute, isecond);
        long newTime = c.getTimeInMillis() + millis;
        mLocation.setTime(newTime);

        return true;
    }

    /**
     * 更新gps日期
     * 
     * @param date
     *            gps日期
     * @return {@code true}如果更新成功，{@code false}如果更新失败
     */
    private boolean updateGpsDate(String date) {
        if (date.length() != 6) {
            return false;
        }
        int month, day, year;
        try {
            day = Integer.parseInt(date.substring(0, 2));
            month = Integer.parseInt(date.substring(2, 4));
            year = 2000 + Integer.parseInt(date.substring(4, 6));
        } catch (NumberFormatException nfe) {
            return false;
        }

        mYear = year;
        mMonth = month;
        mDay = day;
        return true;
    }

    /**
     * 更新gps时间
     * 
     * @param time
     *            时间
     * @param date
     *            日期
     * @return {@code true}如果更新成功，{@code false}如果更新失败
     */
    private boolean updateGpsTime(String time, String date) {
        if (!updateGpsDate(date)) {
            return false;
        }
        return updateGpsTime(time);
    }

    /**
     * 更新{@code Integer}类型的额外信息
     * 
     * @param name
     *            字段名
     * @param value
     *            字段值
     * @return {@code true}如果更新成功，{@code false}如果更新失败
     */
    private boolean updateIntExtra(String name, String value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }

        try {
            int val = Integer.parseInt(value);
            mExtras.putInt(name, val);
            mLocation.setExtras(mExtras);
            return true;
        } catch (NumberFormatException nfe) {
            mExtras.putInt(name, 0);
            mLocation.setExtras(mExtras);
            return false;
        }
    }

    /**
     * 更新{@code Float}类型的额外信息
     * 
     * @param name
     *            字段名
     * @param value
     *            字段值
     * @return {@code true}如果更新成功，{@code false}如果更新失败
     */
    private boolean updateFloatExtra(String name, String value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }

        try {
            float val = Float.parseFloat(value);
            mExtras.putFloat(name, val);
            mLocation.setExtras(mExtras);
            return true;
        } catch (NumberFormatException nfe) {
            mExtras.putFloat(name, 0);
            mLocation.setExtras(mExtras);
            return false;
        }
    }

    /**
     * 由60进制的位置坐标转换为10进制的位置坐标
     * 
     * @param coord
     *            60进制的位置坐标
     * @return 10进制的位置坐标
     */
    private double convertFromHHMM(String coord) {
        double val = Double.parseDouble(coord);
        int degrees = ((int) Math.floor(val)) / 100;
        double minutes = val - (degrees * 100);
        double dcoord = degrees + minutes / 60.0;
        return dcoord;
    }

    /**
     * 更新经纬度
     * 
     * @param latitude
     *            纬度
     * @param latitudeHemi
     *            纬度半球
     * @param longitude
     *            经度
     * @param longitudeHemi
     *            经度半球
     * @return {@code true}如果数据合法，更新成功，{@code false}如果数据不合法，更新失败
     */
    private boolean updateLatLon(String latitude, String latitudeHemi, String longitude, String longitudeHemi) {
        double lat, lon;
        try {
            lat = convertFromHHMM(latitude);
            if (latitudeHemi.charAt(0) == 'S') {
                lat = -lat;
            }
        } catch (NumberFormatException nfe1) {
            mLocation.setLatitude(0);
            mLocation.setLongitude(0);
            return false;
        }

        try {
            lon = convertFromHHMM(longitude);
            if (longitudeHemi.charAt(0) == 'W') {
                lon = -lon;
            }
        } catch (NumberFormatException nfe2) {
            mLocation.setLatitude(0);
            mLocation.setLongitude(0);
            return false;
        }

        /* 当经纬度都合法时更新 */
        mLocation.setLatitude(lat);
        mLocation.setLongitude(lon);
        return true;
    }

    /**
     * 更新海拔高度
     * 
     * @param altitude
     *            海拔高度
     * @return {@code true}如果更新成功，{@code false}如果更新失败
     */
    private boolean updateAltitude(String altitude) {
        try {
            double alt = Double.parseDouble(altitude);
            mLocation.setAltitude(alt);
            return true;
        } catch (NumberFormatException nfe) {
            mLocation.removeAltitude();
            return false;
        }
    }

    /**
     * 更新方位
     * 
     * @param bearing
     *            方位
     * @return {@code true}如果更新成功，{@code false}如果更新失败
     */
    private boolean updateBearing(String bearing) {
        try {
            float brg = Float.parseFloat(bearing);
            mLocation.setBearing(brg);
            return true;
        } catch (NumberFormatException nfe) {
            mLocation.removeBearing();
            return false;
        }
    }

    /**
     * 更新速度
     * 
     * @param speed
     *            速度
     * @return {@code true}如果更新成功，{@code false}如果更新失败
     */
    private boolean updateSpeed(String speed) {
        try {
            float spd = Float.parseFloat(speed) * KNOTS_TO_METERS_PER_SECOND;
            mLocation.setSpeed(spd);
            return true;
        } catch (NumberFormatException nfe) {
            mLocation.removeSpeed();
            return false;
        }
    }

    /**
     * 解析NMEA报文
     * 
     * @param s
     *            一条NMEA报文
     */
    public void parseSentence(String s) {
        mLastParseTimeMillis = System.currentTimeMillis();

        if (!s.startsWith("$G")) {
            return;
        }

        try {
            String[] tokens = s.split(",");
            String lastToken = tokens[tokens.length - 1];
            tokens[tokens.length - 1] = lastToken.split("\\*")[0];
            String sentenceId = tokens[0].substring(3, 6);

            int idx = 1;

            if (sentenceId.equals("GGA")) {
                /* GPS固定数据输出语句，这是一帧GPS定位的主要数据，也是使用最广的数据 */

                /* UTC时间，格式为hhmmss.sss */
                String time = tokens[idx++];
                /* 纬度，格式为ddmm.mmmm（前导位数不足则补0） */
                String latitude = tokens[idx++];
                /* 纬度半球，N或S（北纬或南纬） */
                String latitudeHemi = tokens[idx++];
                /* 经度，格式为dddmm.mmmm（前导位数不足则补0） */
                String longitude = tokens[idx++];
                /* 经度半球，E或W（东经或西经） */
                String longitudeHemi = tokens[idx++];
                /* 定位质量指示，0=定位无效，1=定位有效 */
                String fixQuality = tokens[idx++];
                /* 使用卫星数量，从00到12（前导位数不足则补0） */
                String numSatellites = tokens[idx++];
                /* 水平精确度，0.5到99.9 */
                String horizontalDilutionOfPrecision = tokens[idx++];
                /* 天线离海平面的高度，-9999.9到9999.9米 */
                String altitude = tokens[idx++];
                /* 高度单位，M表示单位米 */
                String altitudeUnits = tokens[idx++];
                /* 大地椭球面相对海平面的高度（-999.9到9999.9） */
                String heightOfGeoid = tokens[idx++];
                /* 高度单位，M表示单位米 */
                String heightOfGeoidUnits = tokens[idx++];
                /* 差分GPS数据期限（RTCM SC-104），最后设立RTCM传送的秒数量 */
                String timeSinceLastDgpsUpdate = tokens[idx++];

                if (fixQuality.equals("0")) {
                    return;
                }

                updateGpsTime(time);
                updateLatLon(latitude, latitudeHemi, longitude, longitudeHemi);
                updateAltitude(altitude);
                updateIntExtra(NUM_SATELLITES, numSatellites);
                updateFloatExtra(HDOP, horizontalDilutionOfPrecision);

            } else if (sentenceId.equals("GSA")) {
                /* GPS精度指针及使用卫星格式 */

                /* 模式2：M = 手动， A = 自动 */
                String selectionMode = tokens[idx++];
                /* 模式1：定位型式1 = 未定位，2 = 二维定位，3 = 三维定位 */
                String mode = tokens[idx++];
                for (int i = 0; i < 12; i++) {
                    /*
                     * 第 i+1 信道正在使用的卫星PRN码编号（Pseudo Random
                     * Noise，伪随机噪声码），01至32（前导位数不足则补0，最多可接收12颗卫星信息）
                     */
                    String id = tokens[idx++];
                }
                /* PDOP综合位置精度因子（0.5 - 99.9） */
                String pdop = tokens[idx++];
                /* HDOP水平精度因子（0.5 - 99.9） */
                String hdop = tokens[idx++];
                /* VDOP垂直精度因子（0.5 - 99.9） */
                String vdop = tokens[idx++];

                updateFloatExtra(PDOP, pdop);
                updateFloatExtra(HDOP, hdop);
                updateFloatExtra(VDOP, vdop);
            } else if (sentenceId.equals("GSV")) {
                /* 可视卫星状态输出语句 */

                /* 总的GSV语句电文数 */
                String numMessages = tokens[idx++];
                /* 当前GSV语句号 */
                String messageNum = tokens[idx++];
                /* 可视卫星总数 */
                String svsInView = tokens[idx++];
                for (int i = 0; i < 4; i++) {
                    if (idx + 2 < tokens.length) {
                        /* 卫星编号，01至32 */
                        String prnNumber = tokens[idx++];
                        /* 卫星仰角，00至90度 */
                        String elevation = tokens[idx++];
                        /* 卫星方位角，000至359度。实际值 */
                        String azimuth = tokens[idx++];

                        if (idx < tokens.length) {
                            /* 信噪比（C/No），00至99dB；无表未接收到讯号 */
                            String snr = tokens[idx++];
                        }
                    }
                }
            } else if (sentenceId.equals("RMC")) {
                /* 推荐最小数据量的GPS信息 */

                /* UTC（Coordinated Universal Time）时间，hhmmss（时分秒）格式 */
                String time = tokens[idx++];
                /* 定位状态，A=有效定位，V=无效定位 */
                String fixStatus = tokens[idx++];
                /* Latitude，纬度ddmm.mmmm（度分）格式（前导位数不足则补0） */
                String latitude = tokens[idx++];
                /* 纬度半球N（北半球）或S（南半球） */
                String latitudeHemi = tokens[idx++];
                /* Longitude，经度dddmm.mmmm（度分）格式（前导位数不足则补0） */
                String longitude = tokens[idx++];
                /* 经度半球E（东经）或W（西经） */
                String longitudeHemi = tokens[idx++];
                /* 地面速率（000.0~999.9节，Knot，前导位数不足则补0） */
                String speed = tokens[idx++];
                /* 地面航向（000.0~359.9度，以真北为参考基准，前导位数不足则补0） */
                String bearing = tokens[idx++];
                /* UTC日期，ddmmyy（日月年）格式 */
                String utcDate = tokens[idx++];
                /* Magnetic Variation，磁偏角（000.0~180.0度，前导位数不足则补0） */
                String magneticVariation = tokens[idx++];
                /* Declination，磁偏角方向，E（东）或W（西） */
                String magneticVariationDir = tokens[idx++];
                /*
                 * Mode Indicator，模式指示（仅NMEA0183
                 * 3.00版本输出，A=自主定位，D=差分，E=估算，N=数据无效）
                 */
                String mode = tokens[idx++];

                if (fixStatus.charAt(0) == 'A') {
                    updateGpsTime(time, utcDate);
                    updateLatLon(latitude, latitudeHemi, longitude, longitudeHemi);
                    updateBearing(bearing);
                    updateSpeed(speed);
                }

            } 
        } catch (Exception e) {
            Log.e(TAG, "AIOOBE", e);
        }
    }

    /**
     * 获取当前定位信息
     * 
     * @return 当前定位信息,可能为{@code null}
     */
    /* package */Location getLocation() {
        long currentTime = System.currentTimeMillis();
        /* 如果10秒钟没有更新，返回{@code null} */
        if (currentTime - mLastParseTimeMillis > 10 * 1000) {
            return null;
        }

        if (mLocation.getLatitude() == 0 || mLocation.getLongitude() == 0) {
            return null;
        }

        return mLocation;
    }
}
