
package com.yc.tracesdk.LocInfoProtoBuf;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Label.REPEATED;

/**
 * 打包数据类型
 */
public final class TraceData extends Message {

  public static final List<WifiInfo> DEFAULT_WIFI_LIST = Collections.emptyList();
  public static final List<GpsInfo> DEFAULT_GPS_LIST = Collections.emptyList();
  public static final List<CellInfo> DEFAULT_CELL_LIST = Collections.emptyList();
  public static final List<ExtraLocInfo> DEFAULT_EXTRA_LOC_LIST = Collections.emptyList();
  public static final List<EnvInfo> DEFAULT_ENV_LIST = Collections.emptyList();

  @ProtoField(tag = 1, label = REPEATED, messageType = WifiInfo.class)
  public final List<WifiInfo> wifi_list;

  /**
   * wifi信息列表
   */
  @ProtoField(tag = 2, label = REPEATED, messageType = GpsInfo.class)
  public final List<GpsInfo> gps_list;

  /**
   * gps信息列表
   */
  @ProtoField(tag = 3, label = REPEATED, messageType = CellInfo.class)
  public final List<CellInfo> cell_list;

  /**
   * cell信息列表
   */
  @ProtoField(tag = 4, label = REPEATED, messageType = ExtraLocInfo.class)
  public final List<ExtraLocInfo> extra_loc_list;

  /**
   * extraloc信息列表
   */
  @ProtoField(tag = 5, label = REPEATED, messageType = EnvInfo.class)
  public final List<EnvInfo> env_list;

  public TraceData(List<WifiInfo> wifi_list, List<GpsInfo> gps_list, List<CellInfo> cell_list, List<ExtraLocInfo> extra_loc_list, List<EnvInfo> env_list) {
    this.wifi_list = immutableCopyOf(wifi_list);
    this.gps_list = immutableCopyOf(gps_list);
    this.cell_list = immutableCopyOf(cell_list);
    this.extra_loc_list = immutableCopyOf(extra_loc_list);
    this.env_list = immutableCopyOf(env_list);
  }

  private TraceData(Builder builder) {
    this(builder.wifi_list, builder.gps_list, builder.cell_list, builder.extra_loc_list, builder.env_list);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof TraceData)) return false;
    TraceData o = (TraceData) other;
    return equals(wifi_list, o.wifi_list)
        && equals(gps_list, o.gps_list)
        && equals(cell_list, o.cell_list)
        && equals(extra_loc_list, o.extra_loc_list)
        && equals(env_list, o.env_list);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = wifi_list != null ? wifi_list.hashCode() : 1;
      result = result * 37 + (gps_list != null ? gps_list.hashCode() : 1);
      result = result * 37 + (cell_list != null ? cell_list.hashCode() : 1);
      result = result * 37 + (extra_loc_list != null ? extra_loc_list.hashCode() : 1);
      result = result * 37 + (env_list != null ? env_list.hashCode() : 1);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<TraceData> {

    public List<WifiInfo> wifi_list;
    public List<GpsInfo> gps_list;
    public List<CellInfo> cell_list;
    public List<ExtraLocInfo> extra_loc_list;
    public List<EnvInfo> env_list;

    public Builder() {
    }

    public Builder(TraceData message) {
      super(message);
      if (message == null) return;
      this.wifi_list = copyOf(message.wifi_list);
      this.gps_list = copyOf(message.gps_list);
      this.cell_list = copyOf(message.cell_list);
      this.extra_loc_list = copyOf(message.extra_loc_list);
      this.env_list = copyOf(message.env_list);
    }

    public Builder wifi_list(List<WifiInfo> wifi_list) {
      this.wifi_list = checkForNulls(wifi_list);
      return this;
    }

    /**
     * wifi信息列表
     */
    public Builder gps_list(List<GpsInfo> gps_list) {
      this.gps_list = checkForNulls(gps_list);
      return this;
    }

    /**
     * gps信息列表
     */
    public Builder cell_list(List<CellInfo> cell_list) {
      this.cell_list = checkForNulls(cell_list);
      return this;
    }

    /**
     * cell信息列表
     */
    public Builder extra_loc_list(List<ExtraLocInfo> extra_loc_list) {
      this.extra_loc_list = checkForNulls(extra_loc_list);
      return this;
    }

    /**
     * extraloc信息列表
     */
    public Builder env_list(List<EnvInfo> env_list) {
      this.env_list = checkForNulls(env_list);
      return this;
    }

    @Override
    public TraceData build() {
      return new TraceData(this);
    }
  }
}
