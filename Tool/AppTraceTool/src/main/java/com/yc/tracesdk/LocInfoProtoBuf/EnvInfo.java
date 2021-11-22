
package com.yc.tracesdk.LocInfoProtoBuf;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.FLOAT;
import static com.squareup.wire.Message.Datatype.INT32;
import static com.squareup.wire.Message.Datatype.INT64;
import static com.squareup.wire.Message.Label.REQUIRED;

/**
 * 环境感知采集数据
 */
public final class EnvInfo extends Message {

  public static final Long DEFAULT_TIME = 0L;
  public static final Integer DEFAULT_WIFI_ENABLED = 0;
  public static final Integer DEFAULT_WIFI_CONNECTED = 0;
  public static final Integer DEFAULT_GPS_ENABLED = 0;
  public static final Long DEFAULT_GPS_FIX_INTERV = 0L;
  public static final Integer DEFAULT_LIGHT = 0;
  public static final Integer DEFAULT_AIR_PRESSURE = 0;
  public static final Integer DEFAULT_BLUETOOTH_ENABLED = 0;

  /**
   * 采集时间，单位：毫秒
   */
  @ProtoField(tag = 1, type = INT64, label = REQUIRED)
  public final Long time;

  /**
   * WIFI开关状态，0=关闭，1=开启
   */
  @ProtoField(tag = 2, type = INT32)
  public final Integer wifi_enabled;

  /**
   * WIFI连接状态，0=未连接，1=连接
   */
  @ProtoField(tag = 3, type = INT32)
  public final Integer wifi_connected;

  /**
   * GPS开关状态，0=关闭，1=开启
   */
  @ProtoField(tag = 4, type = INT32)
  public final Integer gps_enabled;

  /**
   * GPS锁定时间间隔，单位：毫秒
   */
  @ProtoField(tag = 5, type = INT64)
  public final Long gps_fix_interv;

  /**
   * 光照强度，单位：lx
   */
  @ProtoField(tag = 6, type = INT32)
  public final Integer light;

  /**
   * 气压计量值
   */
  @ProtoField(tag = 7, type = INT32)
  public final Integer air_pressure;

  /**
   * 三轴加速度值
   */
  @ProtoField(tag = 8)
  public final Accelerometer accelerometer;

  /**
   * 蓝牙开关状态
   */
  @ProtoField(tag = 9, type = INT32)
  public final Integer bluetooth_enabled;

  public EnvInfo(Long time, Integer wifi_enabled, Integer wifi_connected, Integer gps_enabled, Long gps_fix_interv, Integer light, Integer air_pressure, Accelerometer accelerometer, Integer bluetooth_enabled) {
    this.time = time;
    this.wifi_enabled = wifi_enabled;
    this.wifi_connected = wifi_connected;
    this.gps_enabled = gps_enabled;
    this.gps_fix_interv = gps_fix_interv;
    this.light = light;
    this.air_pressure = air_pressure;
    this.accelerometer = accelerometer;
    this.bluetooth_enabled = bluetooth_enabled;
  }

  private EnvInfo(Builder builder) {
    this(builder.time, builder.wifi_enabled, builder.wifi_connected, builder.gps_enabled, builder.gps_fix_interv, builder.light, builder.air_pressure, builder.accelerometer, builder.bluetooth_enabled);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof EnvInfo)) return false;
    EnvInfo o = (EnvInfo) other;
    return equals(time, o.time)
        && equals(wifi_enabled, o.wifi_enabled)
        && equals(wifi_connected, o.wifi_connected)
        && equals(gps_enabled, o.gps_enabled)
        && equals(gps_fix_interv, o.gps_fix_interv)
        && equals(light, o.light)
        && equals(air_pressure, o.air_pressure)
        && equals(accelerometer, o.accelerometer)
        && equals(bluetooth_enabled, o.bluetooth_enabled);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = time != null ? time.hashCode() : 0;
      result = result * 37 + (wifi_enabled != null ? wifi_enabled.hashCode() : 0);
      result = result * 37 + (wifi_connected != null ? wifi_connected.hashCode() : 0);
      result = result * 37 + (gps_enabled != null ? gps_enabled.hashCode() : 0);
      result = result * 37 + (gps_fix_interv != null ? gps_fix_interv.hashCode() : 0);
      result = result * 37 + (light != null ? light.hashCode() : 0);
      result = result * 37 + (air_pressure != null ? air_pressure.hashCode() : 0);
      result = result * 37 + (accelerometer != null ? accelerometer.hashCode() : 0);
      result = result * 37 + (bluetooth_enabled != null ? bluetooth_enabled.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<EnvInfo> {

    public Long time;
    public Integer wifi_enabled;
    public Integer wifi_connected;
    public Integer gps_enabled;
    public Long gps_fix_interv;
    public Integer light;
    public Integer air_pressure;
    public Accelerometer accelerometer;
    public Integer bluetooth_enabled;

    public Builder() {
    }

    public Builder(EnvInfo message) {
      super(message);
      if (message == null) return;
      this.time = message.time;
      this.wifi_enabled = message.wifi_enabled;
      this.wifi_connected = message.wifi_connected;
      this.gps_enabled = message.gps_enabled;
      this.gps_fix_interv = message.gps_fix_interv;
      this.light = message.light;
      this.air_pressure = message.air_pressure;
      this.accelerometer = message.accelerometer;
      this.bluetooth_enabled = message.bluetooth_enabled;
    }

    /**
     * 采集时间，单位：毫秒
     */
    public Builder time(Long time) {
      this.time = time;
      return this;
    }

    /**
     * WIFI开关状态，0=关闭，1=开启
     */
    public Builder wifi_enabled(Integer wifi_enabled) {
      this.wifi_enabled = wifi_enabled;
      return this;
    }

    /**
     * WIFI连接状态，0=未连接，1=连接
     */
    public Builder wifi_connected(Integer wifi_connected) {
      this.wifi_connected = wifi_connected;
      return this;
    }

    /**
     * GPS开关状态，0=关闭，1=开启
     */
    public Builder gps_enabled(Integer gps_enabled) {
      this.gps_enabled = gps_enabled;
      return this;
    }

    /**
     * GPS锁定时间间隔，单位：毫秒
     */
    public Builder gps_fix_interv(Long gps_fix_interv) {
      this.gps_fix_interv = gps_fix_interv;
      return this;
    }

    /**
     * 光照强度，单位：lx
     */
    public Builder light(Integer light) {
      this.light = light;
      return this;
    }

    /**
     * 气压计量值
     */
    public Builder air_pressure(Integer air_pressure) {
      this.air_pressure = air_pressure;
      return this;
    }

    /**
     * 三轴加速度值
     */
    public Builder accelerometer(Accelerometer accelerometer) {
      this.accelerometer = accelerometer;
      return this;
    }

    /**
     * 蓝牙开关状态
     */
    public Builder bluetooth_enabled(Integer bluetooth_enabled) {
      this.bluetooth_enabled = bluetooth_enabled;
      return this;
    }

    @Override
    public EnvInfo build() {
      checkRequiredFields();
      return new EnvInfo(this);
    }
  }

  /**
   * 加速度计三轴
   */
  public static final class Accelerometer extends Message {

    public static final Float DEFAULT_X_AXIS = 0F;
    public static final Float DEFAULT_Y_AXIS = 0F;
    public static final Float DEFAULT_Z_AXIS = 0F;

    @ProtoField(tag = 1, type = FLOAT, label = REQUIRED)
    public final Float x_axis;

    @ProtoField(tag = 2, type = FLOAT, label = REQUIRED)
    public final Float y_axis;

    @ProtoField(tag = 3, type = FLOAT, label = REQUIRED)
    public final Float z_axis;

    public Accelerometer(Float x_axis, Float y_axis, Float z_axis) {
      this.x_axis = x_axis;
      this.y_axis = y_axis;
      this.z_axis = z_axis;
    }

    private Accelerometer(Builder builder) {
      this(builder.x_axis, builder.y_axis, builder.z_axis);
      setBuilder(builder);
    }

    @Override
    public boolean equals(Object other) {
      if (other == this) return true;
      if (!(other instanceof Accelerometer)) return false;
      Accelerometer o = (Accelerometer) other;
      return equals(x_axis, o.x_axis)
          && equals(y_axis, o.y_axis)
          && equals(z_axis, o.z_axis);
    }

    @Override
    public int hashCode() {
      int result = hashCode;
      if (result == 0) {
        result = x_axis != null ? x_axis.hashCode() : 0;
        result = result * 37 + (y_axis != null ? y_axis.hashCode() : 0);
        result = result * 37 + (z_axis != null ? z_axis.hashCode() : 0);
        hashCode = result;
      }
      return result;
    }

    public static final class Builder extends Message.Builder<Accelerometer> {

      public Float x_axis;
      public Float y_axis;
      public Float z_axis;

      public Builder() {
      }

      public Builder(Accelerometer message) {
        super(message);
        if (message == null) return;
        this.x_axis = message.x_axis;
        this.y_axis = message.y_axis;
        this.z_axis = message.z_axis;
      }

      public Builder x_axis(Float x_axis) {
        this.x_axis = x_axis;
        return this;
      }

      public Builder y_axis(Float y_axis) {
        this.y_axis = y_axis;
        return this;
      }

      public Builder z_axis(Float z_axis) {
        this.z_axis = z_axis;
        return this;
      }

      @Override
      public Accelerometer build() {
        checkRequiredFields();
        return new Accelerometer(this);
      }
    }
  }
}
