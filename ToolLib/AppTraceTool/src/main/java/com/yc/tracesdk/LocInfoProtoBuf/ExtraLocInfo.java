
package com.yc.tracesdk.LocInfoProtoBuf;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.DOUBLE;
import static com.squareup.wire.Message.Datatype.FLOAT;
import static com.squareup.wire.Message.Datatype.INT64;
import static com.squareup.wire.Message.Label.REQUIRED;

/**
 * 第三方定位服务提供的数据
 */
public final class ExtraLocInfo extends Message {

  public static final Long DEFAULT_TIME = 0L;
  public static final Double DEFAULT_LONGITUDE = 0D;
  public static final Double DEFAULT_LATITUDE = 0D;
  public static final Double DEFAULT_ALTITUDE = 0D;
  public static final Float DEFAULT_ACCURACY = 0F;
  public static final Float DEFAULT_SPEED = 0F;
  public static final Long DEFAULT_GPS_TS = 0L;
  public static final Float DEFAULT_BEARING = 0F;

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
   * 速度，获取不到时为-1
   */
  @ProtoField(tag = 6, type = FLOAT)
  public final Float speed;

  /**
   * GPS时间戳
   */
  @ProtoField(tag = 7, type = INT64)
  public final Long gps_ts;

  /**
   * 方向，获取不到时为-1
   */
  @ProtoField(tag = 8, type = FLOAT)
  public final Float bearing;

  public ExtraLocInfo(Long time, Double longitude, Double latitude, Double altitude, Float accuracy, Float speed, Long gps_ts, Float bearing) {
    this.time = time;
    this.longitude = longitude;
    this.latitude = latitude;
    this.altitude = altitude;
    this.accuracy = accuracy;
    this.speed = speed;
    this.gps_ts = gps_ts;
    this.bearing = bearing;
  }

  private ExtraLocInfo(Builder builder) {
    this(builder.time, builder.longitude, builder.latitude, builder.altitude, builder.accuracy, builder.speed, builder.gps_ts, builder.bearing);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof ExtraLocInfo)) return false;
    ExtraLocInfo o = (ExtraLocInfo) other;
    return equals(time, o.time)
        && equals(longitude, o.longitude)
        && equals(latitude, o.latitude)
        && equals(altitude, o.altitude)
        && equals(accuracy, o.accuracy)
        && equals(speed, o.speed)
        && equals(gps_ts, o.gps_ts)
        && equals(bearing, o.bearing);
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
      result = result * 37 + (speed != null ? speed.hashCode() : 0);
      result = result * 37 + (gps_ts != null ? gps_ts.hashCode() : 0);
      result = result * 37 + (bearing != null ? bearing.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<ExtraLocInfo> {

    public Long time;
    public Double longitude;
    public Double latitude;
    public Double altitude;
    public Float accuracy;
    public Float speed;
    public Long gps_ts;
    public Float bearing;

    public Builder() {
    }

    public Builder(ExtraLocInfo message) {
      super(message);
      if (message == null) return;
      this.time = message.time;
      this.longitude = message.longitude;
      this.latitude = message.latitude;
      this.altitude = message.altitude;
      this.accuracy = message.accuracy;
      this.speed = message.speed;
      this.gps_ts = message.gps_ts;
      this.bearing = message.bearing;
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

    @Override
    public ExtraLocInfo build() {
      checkRequiredFields();
      return new ExtraLocInfo(this);
    }
  }
}
