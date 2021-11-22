
package com.yc.tracesdk.LocInfoProtoBuf;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoEnum;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Datatype.ENUM;
import static com.squareup.wire.Message.Datatype.INT32;
import static com.squareup.wire.Message.Datatype.INT64;
import static com.squareup.wire.Message.Label.REPEATED;
import static com.squareup.wire.Message.Label.REQUIRED;

public final class CellInfo extends Message {

  public static final Long DEFAULT_TIME = 0L;
  public static final Integer DEFAULT_MCC = 0;
  public static final Integer DEFAULT_MNC = 0;
  public static final Integer DEFAULT_LAC = 0;
  public static final Integer DEFAULT_CID = 0;
  public static final CellType DEFAULT_CELL_TYPE = CellType.UNKNOWN;
  public static final Integer DEFAULT_RSSI = 0;
  public static final Integer DEFAULT_PSC = 0;
  public static final List<NeighboringCellInfo> DEFAULT_NEIGHBORINGCELLINFO = Collections.emptyList();

  /**
   * 采集时间，单位：毫秒
   */
  @ProtoField(tag = 1, type = INT64, label = REQUIRED)
  public final Long time;

  /**
   * 国家编码
   */
  @ProtoField(tag = 2, type = INT32, label = REQUIRED)
  public final Integer mcc;

  /**
   * 运营商编码
   */
  @ProtoField(tag = 3, type = INT32, label = REQUIRED)
  public final Integer mnc;

  /**
   * 小区编码
   */
  @ProtoField(tag = 4, type = INT32, label = REQUIRED)
  public final Integer lac;

  /**
   * 基站编码
   */
  @ProtoField(tag = 5, type = INT32, label = REQUIRED)
  public final Integer cid;

  /**
   * 基站类型
   */
  @ProtoField(tag = 6, type = ENUM)
  public final CellType cell_type;

  /**
   * 信号强度，单位：dBm
   */
  @ProtoField(tag = 9, type = INT32)
  public final Integer rssi;

  /**
   * Primary Scrambling Code
   */
  @ProtoField(tag = 7, type = INT32)
  public final Integer psc;

  /**
   * 附近基站列表
   */
  @ProtoField(tag = 8, label = REPEATED, messageType = NeighboringCellInfo.class)
  public final List<NeighboringCellInfo> neighboringCellInfo;

  public CellInfo(Long time, Integer mcc, Integer mnc, Integer lac, Integer cid, CellType cell_type, Integer rssi, Integer psc, List<NeighboringCellInfo> neighboringCellInfo) {
    this.time = time;
    this.mcc = mcc;
    this.mnc = mnc;
    this.lac = lac;
    this.cid = cid;
    this.cell_type = cell_type;
    this.rssi = rssi;
    this.psc = psc;
    this.neighboringCellInfo = immutableCopyOf(neighboringCellInfo);
  }

  private CellInfo(Builder builder) {
    this(builder.time, builder.mcc, builder.mnc, builder.lac, builder.cid, builder.cell_type, builder.rssi, builder.psc, builder.neighboringCellInfo);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof CellInfo)) return false;
    CellInfo o = (CellInfo) other;
    return equals(time, o.time)
        && equals(mcc, o.mcc)
        && equals(mnc, o.mnc)
        && equals(lac, o.lac)
        && equals(cid, o.cid)
        && equals(cell_type, o.cell_type)
        && equals(rssi, o.rssi)
        && equals(psc, o.psc)
        && equals(neighboringCellInfo, o.neighboringCellInfo);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = time != null ? time.hashCode() : 0;
      result = result * 37 + (mcc != null ? mcc.hashCode() : 0);
      result = result * 37 + (mnc != null ? mnc.hashCode() : 0);
      result = result * 37 + (lac != null ? lac.hashCode() : 0);
      result = result * 37 + (cid != null ? cid.hashCode() : 0);
      result = result * 37 + (cell_type != null ? cell_type.hashCode() : 0);
      result = result * 37 + (rssi != null ? rssi.hashCode() : 0);
      result = result * 37 + (psc != null ? psc.hashCode() : 0);
      result = result * 37 + (neighboringCellInfo != null ? neighboringCellInfo.hashCode() : 1);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<CellInfo> {

    public Long time;
    public Integer mcc;
    public Integer mnc;
    public Integer lac;
    public Integer cid;
    public CellType cell_type;
    public Integer rssi;
    public Integer psc;
    public List<NeighboringCellInfo> neighboringCellInfo;

    public Builder() {
    }

    public Builder(CellInfo message) {
      super(message);
      if (message == null) return;
      this.time = message.time;
      this.mcc = message.mcc;
      this.mnc = message.mnc;
      this.lac = message.lac;
      this.cid = message.cid;
      this.cell_type = message.cell_type;
      this.rssi = message.rssi;
      this.psc = message.psc;
      this.neighboringCellInfo = copyOf(message.neighboringCellInfo);
    }

    /**
     * 采集时间，单位：毫秒
     */
    public Builder time(Long time) {
      this.time = time;
      return this;
    }

    /**
     * 国家编码
     */
    public Builder mcc(Integer mcc) {
      this.mcc = mcc;
      return this;
    }

    /**
     * 运营商编码
     */
    public Builder mnc(Integer mnc) {
      this.mnc = mnc;
      return this;
    }

    /**
     * 小区编码
     */
    public Builder lac(Integer lac) {
      this.lac = lac;
      return this;
    }

    /**
     * 基站编码
     */
    public Builder cid(Integer cid) {
      this.cid = cid;
      return this;
    }

    /**
     * 基站类型
     */
    public Builder cell_type(CellType cell_type) {
      this.cell_type = cell_type;
      return this;
    }

    /**
     * 信号强度，单位：dBm
     */
    public Builder rssi(Integer rssi) {
      this.rssi = rssi;
      return this;
    }

    /**
     * Primary Scrambling Code
     */
    public Builder psc(Integer psc) {
      this.psc = psc;
      return this;
    }

    /**
     * 附近基站列表
     */
    public Builder neighboringCellInfo(List<NeighboringCellInfo> neighboringCellInfo) {
      this.neighboringCellInfo = checkForNulls(neighboringCellInfo);
      return this;
    }

    @Override
    public CellInfo build() {
      checkRequiredFields();
      return new CellInfo(this);
    }
  }

  public enum CellType
      implements ProtoEnum {
    UNKNOWN(0),
    GSM(1),
    CDMA(2),
    LTE(3);

    private final int value;

    private CellType(int value) {
      this.value = value;
    }

    @Override
    public int getValue() {
      return value;
    }
  }

  /**
   * 附近基站信息
   */
  public static final class NeighboringCellInfo extends Message {

    public static final Integer DEFAULT_LAC = 0;
    public static final Integer DEFAULT_CID = 0;
    public static final Integer DEFAULT_RSSI = 0;
    public static final Integer DEFAULT_PSC = 0;

    /**
     * 小区编码
     */
    @ProtoField(tag = 1, type = INT32)
    public final Integer lac;

    /**
     * 基站编码
     */
    @ProtoField(tag = 2, type = INT32)
    public final Integer cid;

    /**
     * 信号强度，单位：dBm
     */
    @ProtoField(tag = 3, type = INT32)
    public final Integer rssi;

    /**
     * Primary Scrambling Code
     */
    @ProtoField(tag = 4, type = INT32)
    public final Integer psc;

    public NeighboringCellInfo(Integer lac, Integer cid, Integer rssi, Integer psc) {
      this.lac = lac;
      this.cid = cid;
      this.rssi = rssi;
      this.psc = psc;
    }

    private NeighboringCellInfo(Builder builder) {
      this(builder.lac, builder.cid, builder.rssi, builder.psc);
      setBuilder(builder);
    }

    @Override
    public boolean equals(Object other) {
      if (other == this) return true;
      if (!(other instanceof NeighboringCellInfo)) return false;
      NeighboringCellInfo o = (NeighboringCellInfo) other;
      return equals(lac, o.lac)
          && equals(cid, o.cid)
          && equals(rssi, o.rssi)
          && equals(psc, o.psc);
    }

    @Override
    public int hashCode() {
      int result = hashCode;
      if (result == 0) {
        result = lac != null ? lac.hashCode() : 0;
        result = result * 37 + (cid != null ? cid.hashCode() : 0);
        result = result * 37 + (rssi != null ? rssi.hashCode() : 0);
        result = result * 37 + (psc != null ? psc.hashCode() : 0);
        hashCode = result;
      }
      return result;
    }

    public static final class Builder extends Message.Builder<NeighboringCellInfo> {

      public Integer lac;
      public Integer cid;
      public Integer rssi;
      public Integer psc;

      public Builder() {
      }

      public Builder(NeighboringCellInfo message) {
        super(message);
        if (message == null) return;
        this.lac = message.lac;
        this.cid = message.cid;
        this.rssi = message.rssi;
        this.psc = message.psc;
      }

      /**
       * 小区编码
       */
      public Builder lac(Integer lac) {
        this.lac = lac;
        return this;
      }

      /**
       * 基站编码
       */
      public Builder cid(Integer cid) {
        this.cid = cid;
        return this;
      }

      /**
       * 信号强度，单位：dBm
       */
      public Builder rssi(Integer rssi) {
        this.rssi = rssi;
        return this;
      }

      /**
       * Primary Scrambling Code
       */
      public Builder psc(Integer psc) {
        this.psc = psc;
        return this;
      }

      @Override
      public NeighboringCellInfo build() {
        return new NeighboringCellInfo(this);
      }
    }
  }
}
