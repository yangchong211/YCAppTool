
package com.yc.tracesdk.LocInfoProtoBuf;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Datatype.DOUBLE;
import static com.squareup.wire.Message.Datatype.FLOAT;
import static com.squareup.wire.Message.Datatype.INT32;
import static com.squareup.wire.Message.Datatype.INT64;
import static com.squareup.wire.Message.Label.REPEATED;
import static com.squareup.wire.Message.Label.REQUIRED;

public final class GpsInfo extends Message {

  public static final Long DEFAULT_TIME = 0L;
  public static final Double DEFAULT_LONGITUDE = 0D;
  public static final Double DEFAULT_LATITUDE = 0D;
  public static final Double DEFAULT_ALTITUDE = 0D;
  public static final Float DEFAULT_ACCURACY = 0F;
  public static final Float DEFAULT_PDOP = 0F;
  public static final Float DEFAULT_HDOP = 0F;
  public static final Float DEFAULT_VDOP = 0F;
  public static final Float DEFAULT_SPEED = 0F;
  public static final Long DEFAULT_GPS_TS = 0L;
  public static final Float DEFAULT_BEARING = 0F;
  public static final Integer DEFAULT_NUM_SATELLITES = 0;
  public static final List<SatelliteInfo> DEFAULT_SATELLITE = Collections.emptyList();

  /**
   * 采集时间，单位：毫秒
   */
  @ProtoField(tag = 1, type = INT64, label = REQUIRED)
  public final Long time;

  /**
   * 经度
   */
  @ProtoField(tag = 2, type = DOUBLE, label = REQUIRED)
  public final Double longitude;

  /**
   * 纬度
   */
  @ProtoField(tag = 3, type = DOUBLE, label = REQUIRED)
  public final Double latitude;

  /**
   * 高度，获取不到时为-1
   */
  @ProtoField(tag = 4, type = DOUBLE)
  public final Double altitude;

  /**
   * 精度，获取不到时为-1
   */
  @ProtoField(tag = 5, type = FLOAT)
  public final Float accuracy;

  /**
   * 综合精度因子
   */
  @ProtoField(tag = 6, type = FLOAT)
  public final Float pdop;

  /**
   * 水平精度因子
   */
  @ProtoField(tag = 7, type = FLOAT)
  public final Float hdop;

  /**
   * 垂直精度因子
   */
  @ProtoField(tag = 8, type = FLOAT)
  public final Float vdop;

  /**
   * 速度，获取不到时为-1
   */
  @ProtoField(tag = 9, type = FLOAT)
  public final Float speed;

  /**
   * GPS时间戳
   */
  @ProtoField(tag = 10, type = INT64)
  public final Long gps_ts;

  /**
   * 方向，获取不到时为-1
   */
  @ProtoField(tag = 11, type = FLOAT)
  public final Float bearing;

  /**
   * 搜索到的卫星数
   */
  @ProtoField(tag = 12, type = INT32)
  public final Integer num_satellites;

  /**
   * 卫星列表
   */
  @ProtoField(tag = 13, label = REPEATED, messageType = SatelliteInfo.class)
  public final List<SatelliteInfo> satellite;

  public GpsInfo(Long time, Double longitude, Double latitude, Double altitude, Float accuracy, Float pdop, Float hdop, Float vdop, Float speed, Long gps_ts, Float bearing, Integer num_satellites, List<SatelliteInfo> satellite) {
    this.time = time;
    this.longitude = longitude;
    this.latitude = latitude;
    this.altitude = altitude;
    this.accuracy = accuracy;
    this.pdop = pdop;
    this.hdop = hdop;
    this.vdop = vdop;
    this.speed = speed;
    this.gps_ts = gps_ts;
    this.bearing = bearing;
    this.num_satellites = num_satellites;
    this.satellite = immutableCopyOf(satellite);
  }

  private GpsInfo(Builder builder) {
    this(builder.time, builder.longitude, builder.latitude, builder.altitude, builder.accuracy, builder.pdop, builder.hdop, builder.vdop, builder.speed, builder.gps_ts, builder.bearing, builder.num_satellites, builder.satellite);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GpsInfo)) return false;
    GpsInfo o = (GpsInfo) other;
    return equals(time, o.time)
        && equals(longitude, o.longitude)
        && equals(latitude, o.latitude)
        && equals(altitude, o.altitude)
        && equals(accuracy, o.accuracy)
        && equals(pdop, o.pdop)
        && equals(hdop, o.hdop)
        && equals(vdop, o.vdop)
        && equals(speed, o.speed)
        && equals(gps_ts, o.gps_ts)
        && equals(bearing, o.bearing)
        && equals(num_satellites, o.num_satellites)
        && equals(satellite, o.satellite);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = time != null ? time.hashCode() : 0;
      result = result * 37 + (longitude != null ? longitude.hashCode() : 0);
      result = result * 37 + (latitude != null ? latitude.hashCode() : 0);
      result = result * 37 + (altitude != null ? altitude.hashCode() : 0);
      result = result * 37 + (accuracy != null ? accuracy.hashCode() : 0);
      result = result * 37 + (pdop != null ? pdop.hashCode() : 0);
      result = result * 37 + (hdop != null ? hdop.hashCode() : 0);
      result = result * 37 + (vdop != null ? vdop.hashCode() : 0);
      result = result * 37 + (speed != null ? speed.hashCode() : 0);
      result = result * 37 + (gps_ts != null ? gps_ts.hashCode() : 0);
      result = result * 37 + (bearing != null ? bearing.hashCode() : 0);
      result = result * 37 + (num_satellites != null ? num_satellites.hashCode() : 0);
      result = result * 37 + (satellite != null ? satellite.hashCode() : 1);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<GpsInfo> {

    public Long time;
    public Double longitude;
    public Double latitude;
    public Double altitude;
    public Float accuracy;
    public Float pdop;
    public Float hdop;
    public Float vdop;
    public Float speed;
    public Long gps_ts;
    public Float bearing;
    public Integer num_satellites;
    public List<SatelliteInfo> satellite;

    public Builder() {
    }

    public Builder(GpsInfo message) {
      super(message);
      if (message == null) return;
      this.time = message.time;
      this.longitude = message.longitude;
      this.latitude = message.latitude;
      this.altitude = message.altitude;
      this.accuracy = message.accuracy;
      this.pdop = message.pdop;
      this.hdop = message.hdop;
      this.vdop = message.vdop;
      this.speed = message.speed;
      this.gps_ts = message.gps_ts;
      this.bearing = message.bearing;
      this.num_satellites = message.num_satellites;
      this.satellite = copyOf(message.satellite);
    }

    /**
     * 采集时间，单位：毫秒
     */
    public Builder time(Long time) {
      this.time = time;
      return this;
    }

    /**
     * 经度
     */
    public Builder longitude(Double longitude) {
      this.longitude = longitude;
      return this;
    }

    /**
     * 纬度
     */
    public Builder latitude(Double latitude) {
      this.latitude = latitude;
      return this;
    }

    /**
     * 高度，获取不到时为-1
     */
    public Builder altitude(Double altitude) {
      this.altitude = altitude;
      return this;
    }

    /**
     * 精度，获取不到时为-1
     */
    public Builder accuracy(Float accuracy) {
      this.accuracy = accuracy;
      return this;
    }

    /**
     * 综合精度因子
     */
    public Builder pdop(Float pdop) {
      this.pdop = pdop;
      return this;
    }

    /**
     * 水平精度因子
     */
    public Builder hdop(Float hdop) {
      this.hdop = hdop;
      return this;
    }

    /**
     * 垂直精度因子
     */
    public Builder vdop(Float vdop) {
      this.vdop = vdop;
      return this;
    }

    /**
     * 速度，获取不到时为-1
     */
    public Builder speed(Float speed) {
      this.speed = speed;
      return this;
    }

    /**
     * GPS时间戳
     */
    public Builder gps_ts(Long gps_ts) {
      this.gps_ts = gps_ts;
      return this;
    }

    /**
     * 方向，获取不到时为-1
     */
    public Builder bearing(Float bearing) {
      this.bearing = bearing;
      return this;
    }

    /**
     * 搜索到的卫星数
     */
    public Builder num_satellites(Integer num_satellites) {
      this.num_satellites = num_satellites;
      return this;
    }

    /**
     * 卫星列表
     */
    public Builder satellite(List<SatelliteInfo> satellite) {
      this.satellite = checkForNulls(satellite);
      return this;
    }

    @Override
    public GpsInfo build() {
      checkRequiredFields();
      return new GpsInfo(this);
    }
  }

  public static final class SatelliteInfo extends Message {

    public static final Integer DEFAULT_PRN = 0;
    public static final Float DEFAULT_AZIMUTH = 0F;
    public static final Float DEFAULT_ELEVATION = 0F;
    public static final Float DEFAULT_SNR = 0F;

    /**
     * 伪随机噪声码，每个卫星唯一
     */
    @ProtoField(tag = 1, type = INT32, label = REQUIRED)
    public final Integer prn;

    /**
     * 方位角
     */
    @ProtoField(tag = 2, type = FLOAT)
    public final Float azimuth;

    /**
     * 卫星仰角
     */
    @ProtoField(tag = 3, type = FLOAT)
    public final Float elevation;

    /**
     * 信噪比，0表示没有信号
     */
    @ProtoField(tag = 4, type = FLOAT)
    public final Float snr;

    public SatelliteInfo(Integer prn, Float azimuth, Float elevation, Float snr) {
      this.prn = prn;
      this.azimuth = azimuth;
      this.elevation = elevation;
      this.snr = snr;
    }

    private SatelliteInfo(Builder builder) {
      this(builder.prn, builder.azimuth, builder.elevation, builder.snr);
      setBuilder(builder);
    }

    @Override
    public boolean equals(Object other) {
      if (other == this) return true;
      if (!(other instanceof SatelliteInfo)) return false;
      SatelliteInfo o = (SatelliteInfo) other;
      return equals(prn, o.prn)
          && equals(azimuth, o.azimuth)
          && equals(elevation, o.elevation)
          && equals(snr, o.snr);
    }

    @Override
    public int hashCode() {
      int result = hashCode;
      if (result == 0) {
        result = prn != null ? prn.hashCode() : 0;
        result = result * 37 + (azimuth != null ? azimuth.hashCode() : 0);
        result = result * 37 + (elevation != null ? elevation.hashCode() : 0);
        result = result * 37 + (snr != null ? snr.hashCode() : 0);
        hashCode = result;
      }
      return result;
    }

    public static final class Builder extends Message.Builder<SatelliteInfo> {

      public Integer prn;
      public Float azimuth;
      public Float elevation;
      public Float snr;

      public Builder() {
      }

      public Builder(SatelliteInfo message) {
        super(message);
        if (message == null) return;
        this.prn = message.prn;
        this.azimuth = message.azimuth;
        this.elevation = message.elevation;
        this.snr = message.snr;
      }

      /**
       * 伪随机噪声码，每个卫星唯一
       */
      public Builder prn(Integer prn) {
        this.prn = prn;
        return this;
      }

      /**
       * 方位角
       */
      public Builder azimuth(Float azimuth) {
        this.azimuth = azimuth;
        return this;
      }

      /**
       * 卫星仰角
       */
      public Builder elevation(Float elevation) {
        this.elevation = elevation;
        return this;
      }

      /**
       * 信噪比，0表示没有信号
       */
      public Builder snr(Float snr) {
        this.snr = snr;
        return this;
      }

      @Override
      public SatelliteInfo build() {
        checkRequiredFields();
        return new SatelliteInfo(this);
      }
    }
  }
}
