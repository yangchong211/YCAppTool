
package com.yc.tracesdk.LocInfoProtoBuf;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Datatype.INT32;
import static com.squareup.wire.Message.Datatype.INT64;
import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Label.REPEATED;
import static com.squareup.wire.Message.Label.REQUIRED;

public final class WifiInfo extends Message {

  public static final Long DEFAULT_TIME = 0L;
  public static final List<Wifi> DEFAULT_WIFI = Collections.emptyList();

  /**
   * 采集时间，单位：毫秒
   */
  @ProtoField(tag = 1, type = INT64, label = REQUIRED)
  public final Long time;

  /**
   * wifi列表
   */
  @ProtoField(tag = 2, label = REPEATED, messageType = Wifi.class)
  public final List<Wifi> wifi;

  public WifiInfo(Long time, List<Wifi> wifi) {
    this.time = time;
    this.wifi = immutableCopyOf(wifi);
  }

  private WifiInfo(Builder builder) {
    this(builder.time, builder.wifi);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof WifiInfo)) return false;
    WifiInfo o = (WifiInfo) other;
    return equals(time, o.time)
        && equals(wifi, o.wifi);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = time != null ? time.hashCode() : 0;
      result = result * 37 + (wifi != null ? wifi.hashCode() : 1);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<WifiInfo> {

    public Long time;
    public List<Wifi> wifi;

    public Builder() {
    }

    public Builder(WifiInfo message) {
      super(message);
      if (message == null) return;
      this.time = message.time;
      this.wifi = copyOf(message.wifi);
    }

    /**
     * 采集时间，单位：毫秒
     */
    public Builder time(Long time) {
      this.time = time;
      return this;
    }

    /**
     * wifi列表
     */
    public Builder wifi(List<Wifi> wifi) {
      this.wifi = checkForNulls(wifi);
      return this;
    }

    @Override
    public WifiInfo build() {
      checkRequiredFields();
      return new WifiInfo(this);
    }
  }

  public static final class Wifi extends Message {

    public static final String DEFAULT_BSSID = "";
    public static final String DEFAULT_SSID = "";
    public static final Integer DEFAULT_LEVEL = 0;
    public static final Integer DEFAULT_FREQUENCY = 0;
    public static final Integer DEFAULT_IS_CONNECTED = 0;

    /**
     * Basic Service Set ID
     */
    @ProtoField(tag = 1, type = STRING, label = REQUIRED)
    public final String bssid;

    /**
     * wifi 名称
     */
    @ProtoField(tag = 2, type = STRING)
    public final String ssid;

    /**
     * 信号强度，单位：dBm
     */
    @ProtoField(tag = 3, type = INT32)
    public final Integer level;

    /**
     * 信道的频率，单位：MHz
     */
    @ProtoField(tag = 4, type = INT32)
    public final Integer frequency;

    /**
     * 是否连接此wifi，0=否，1=是
     */
    @ProtoField(tag = 5, type = INT32)
    public final Integer is_connected;

    public Wifi(String bssid, String ssid, Integer level, Integer frequency, Integer is_connected) {
      this.bssid = bssid;
      this.ssid = ssid;
      this.level = level;
      this.frequency = frequency;
      this.is_connected = is_connected;
    }

    private Wifi(Builder builder) {
      this(builder.bssid, builder.ssid, builder.level, builder.frequency, builder.is_connected);
      setBuilder(builder);
    }

    @Override
    public boolean equals(Object other) {
      if (other == this) return true;
      if (!(other instanceof Wifi)) return false;
      Wifi o = (Wifi) other;
      return equals(bssid, o.bssid)
          && equals(ssid, o.ssid)
          && equals(level, o.level)
          && equals(frequency, o.frequency)
          && equals(is_connected, o.is_connected);
    }

    @Override
    public int hashCode() {
      int result = hashCode;
      if (result == 0) {
        result = bssid != null ? bssid.hashCode() : 0;
        result = result * 37 + (ssid != null ? ssid.hashCode() : 0);
        result = result * 37 + (level != null ? level.hashCode() : 0);
        result = result * 37 + (frequency != null ? frequency.hashCode() : 0);
        result = result * 37 + (is_connected != null ? is_connected.hashCode() : 0);
        hashCode = result;
      }
      return result;
    }

    public static final class Builder extends Message.Builder<Wifi> {

      public String bssid;
      public String ssid;
      public Integer level;
      public Integer frequency;
      public Integer is_connected;

      public Builder() {
      }

      public Builder(Wifi message) {
        super(message);
        if (message == null) return;
        this.bssid = message.bssid;
        this.ssid = message.ssid;
        this.level = message.level;
        this.frequency = message.frequency;
        this.is_connected = message.is_connected;
      }

      /**
       * Basic Service Set ID
       */
      public Builder bssid(String bssid) {
        this.bssid = bssid;
        return this;
      }

      /**
       * wifi 名称
       */
      public Builder ssid(String ssid) {
        this.ssid = ssid;
        return this;
      }

      /**
       * 信号强度，单位：dBm
       */
      public Builder level(Integer level) {
        this.level = level;
        return this;
      }

      /**
       * 信道的频率，单位：MHz
       */
      public Builder frequency(Integer frequency) {
        this.frequency = frequency;
        return this;
      }

      /**
       * 是否连接此wifi，0=否，1=是
       */
      public Builder is_connected(Integer is_connected) {
        this.is_connected = is_connected;
        return this;
      }

      @Override
      public Wifi build() {
        checkRequiredFields();
        return new Wifi(this);
      }
    }
  }
}
