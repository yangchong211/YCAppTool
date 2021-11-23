package com.yc.location;

import android.text.TextUtils;

import com.yc.location.bean.DefaultLocation;
import com.yc.location.constant.Constants;
import com.yc.location.log.LogHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/** create from thrift
 * Created by liuchuang on 2015/9/29.
 */
public class LocationDataDef {

    static short calcStringLen(String s) {
        if(s == null || s.length() == 0) return 0;
        return (short)s.length();
    }

    static void fillStringToByteBuffer(String s, ByteBuffer byteBuffer) {

        // when s is null or 0 len
        if(s == null || s.length() == 0) {
            byteBuffer.putShort((short)0);
            return;
        }

        // when string cannot perfectly fill to bytearray in utf-8
        byte[] b = null;
        try {
            b = s.getBytes(Constants.paramCharset);
        } catch (Exception e) {}
        if(b == null || b.length != s.length()) {
            byteBuffer.putShort((short)0);
            return;
        }

        byteBuffer.putShort((short)(s.length())).put(b);
        return;
    }
}

enum CellTypeEnum implements Serializable {
    unknown,gsm,cdma,_3g,lte
}

enum ValidFlagEnum implements Serializable {
    invalid,cell,wifi,mixed
}

class location_user_info_t implements Serializable {
    long timestamp;
    String imei;
    String app_id;
    String user_id;
    String phone;
    String modellevel;
    String app_version;

    String toJson() {
        return new StringBuilder().append(Constants.joLeft)
                .append(Constants.js_req_user_timestamp).append(Constants.jsAssi).append(timestamp).append(Constants.jsSepr)
                .append(Constants.js_req_user_imei).append(Constants.jsAssi).append(Constants.formatString(imei)).append(Constants.jsSepr)
                .append(Constants.js_req_user_app_id).append(Constants.jsAssi).append(Constants.formatString(app_id)).append(Constants.jsSepr)
                .append(Constants.js_req_user_user_id).append(Constants.jsAssi).append(Constants.formatString(user_id)).append(Constants.jsSepr)
                .append(Constants.js_req_user_phone).append(Constants.jsAssi).append(Constants.formatString(phone)).append(Constants.jsSepr)
                .append(Constants.js_req_user_modellevel).append(Constants.jsAssi).append(Constants.formatString(modellevel)).append(Constants.jsSepr)
                .append(Constants.js_req_app_version).append(Constants.jsAssi).append(Constants.formatString(app_version))
                .append(Constants.joRight).toString();
    }

    static location_user_info_t toObject(String jsonStr) {
        try {
            location_user_info_t newobj = new location_user_info_t();

            newobj.timestamp = Long.parseLong(Constants.getJsonObject(jsonStr, Constants.js_req_user_timestamp));
            newobj.imei = Constants.getJsonObjectString(jsonStr, Constants.js_req_user_imei);
            newobj.app_id = Constants.getJsonObjectString(jsonStr, Constants.js_req_user_app_id);
            newobj.user_id = Constants.getJsonObjectString(jsonStr, Constants.js_req_user_user_id);
            newobj.phone = Constants.getJsonObjectString(jsonStr, Constants.js_req_user_phone);

            return newobj;
        } catch (Exception e) {
            LogHelper.writeException(e);
            return null;
        }
    }

    byte[] toByteArray() {

        short len = getByteLen();

        ByteBuffer buffer = ByteBuffer.allocate(len);

        buffer.putLong(timestamp);
        LocationDataDef.fillStringToByteBuffer(imei, buffer);
        LocationDataDef.fillStringToByteBuffer(app_id, buffer);
        LocationDataDef.fillStringToByteBuffer(user_id, buffer);
        LocationDataDef.fillStringToByteBuffer(phone, buffer);

        return buffer.array();
    }

    short getByteLen() {
        return (short)(8
                +2+LocationDataDef.calcStringLen(imei)
                +2+LocationDataDef.calcStringLen(app_id)
                +2+LocationDataDef.calcStringLen(user_id)
                +2+LocationDataDef.calcStringLen(phone));
    }
}

class neigh_cell_t implements Serializable {
    long lac;
    long cid;
    long rssi;

    String toJson() {
        return new StringBuilder().append(Constants.joLeft)
                .append(Constants.js_req_cell_neigh_lac).append(Constants.jsAssi).append(lac).append(Constants.jsSepr)
                .append(Constants.js_req_cell_neigh_cid).append(Constants.jsAssi).append(cid).append(Constants.jsSepr)
                .append(Constants.js_req_cell_neigh_rssi).append(Constants.jsAssi).append(rssi)
                .append(Constants.joRight).toString();
    }

    static neigh_cell_t toObject(String jsonStr) {
        try {
            neigh_cell_t newobj = new neigh_cell_t();

            newobj.lac = Long.parseLong(Constants.getJsonObject(jsonStr, Constants.js_req_cell_neigh_lac));
            newobj.cid = Long.parseLong(Constants.getJsonObject(jsonStr, Constants.js_req_cell_neigh_cid));
            newobj.rssi = Long.parseLong(Constants.getJsonObject(jsonStr, Constants.js_req_cell_neigh_rssi));

            return newobj;
        } catch (Exception e) {
            LogHelper.writeException(e);
            return null;
        }
    }

    byte[] toByteArray() {

        short len = getByteLen();

        ByteBuffer buffer = ByteBuffer.allocate(len);

        buffer.putLong(lac).putLong(cid).putLong(rssi);

        return buffer.array();
    }

    short getByteLen() {
        return 24;
    }
}

class cell_info_t implements Serializable {
    long mcc;
    long mnc_sid;
    long lac_nid;
    long cellid_bsid;
    long rssi;
    long type;
    double lon_cdma;
    double lat_cdma;
    List<neigh_cell_t> neighcells = new ArrayList<>();

    String toJson() {
        String neighcellstr = Constants.jaLeft;
        //lcc:fix crash bug 4573:按道理neighcells不应该为null，可能在个别手机个别情况下序列化时出问题。
        if (null != neighcells && neighcells.size() > 0) {
            for(int i = 0; i < neighcells.size(); i++) {
                if(i != 0) neighcellstr += Constants.jsSepr;
                neighcellstr += neighcells.get(i).toJson();
            }
        }
        neighcellstr += Constants.jaRight;

        return new StringBuilder().append(Constants.joLeft)
                .append(Constants.js_req_cell_mcc).append(Constants.jsAssi).append(mcc).append(Constants.jsSepr)
                .append(Constants.js_req_cell_mnc_sid).append(Constants.jsAssi).append(mnc_sid).append(Constants.jsSepr)
                .append(Constants.js_req_cell_lac_nid).append(Constants.jsAssi).append(lac_nid).append(Constants.jsSepr)
                .append(Constants.js_req_cell_cellid_bsid).append(Constants.jsAssi).append(cellid_bsid).append(Constants.jsSepr)
                .append(Constants.js_req_cell_rssi).append(Constants.jsAssi).append(rssi).append(Constants.jsSepr)
                .append(Constants.js_req_cell_type).append(Constants.jsAssi).append(type).append(Constants.jsSepr)
                //.append(Const.js_req_cell_lon_cdma).append(Const.jsAssi).append(Const.formatDouble(lon_cdma, Const.lonlatDots)).append(Const.jsSepr)
                //.append(Const.js_req_cell_lat_cdma).append(Const.jsAssi).append(Const.formatDouble(lat_cdma, Const.lonlatDots)).append(Const.jsSepr)
                .append(Constants.js_req_cell_neighcells).append(Constants.jsAssi).append(neighcellstr)
                .append(Constants.joRight).toString();
    }

    static cell_info_t toObject(String jsonStr) {
        try {
            cell_info_t newobj = new cell_info_t();

            newobj.mcc = Long.parseLong(Constants.getJsonObject(jsonStr, Constants.js_req_cell_mcc));
            newobj.mnc_sid = Long.parseLong(Constants.getJsonObject(jsonStr, Constants.js_req_cell_mnc_sid));
            newobj.lac_nid = Long.parseLong(Constants.getJsonObject(jsonStr, Constants.js_req_cell_lac_nid));
            newobj.cellid_bsid = Long.parseLong(Constants.getJsonObject(jsonStr, Constants.js_req_cell_cellid_bsid));
            newobj.rssi = Long.parseLong(Constants.getJsonObject(jsonStr, Constants.js_req_cell_rssi));
            newobj.type = Long.parseLong(Constants.getJsonObject(jsonStr, Constants.js_req_cell_type));
            newobj.lon_cdma = Double.parseDouble(Constants.getJsonObject(jsonStr, Constants.js_req_cell_lon_cdma));
            newobj.lat_cdma = Double.parseDouble(Constants.getJsonObject(jsonStr, Constants.js_req_cell_lat_cdma));

            ArrayList<String> nestrs = Constants.getJsonArrayObjects(Constants.getJsonObject(jsonStr, Constants.js_req_cell_neighcells));
            for(String ne : nestrs) {
                newobj.neighcells.add(neigh_cell_t.toObject(ne));
            }

            return newobj;
        } catch (Exception e) {
            LogHelper.writeException(e);
            return null;
        }
    }

    byte[] toByteArray() {

        short len = getByteLen();

        short neigblistlen = 0;
        for(int i = 0; i < neighcells.size(); i++) {
            neigblistlen += neighcells.get(i).getByteLen();
        }

        ByteBuffer buffer = ByteBuffer.allocate(len);

        buffer.putLong(mcc)
                .putLong(mnc_sid)
                .putLong(lac_nid)
                .putLong(cellid_bsid)
                .putLong(rssi)
                .putLong(type)
                .putDouble(lon_cdma)
                .putDouble(lat_cdma);
        buffer.putShort(neigblistlen);
        for(int i = 0; i < neighcells.size(); i++) {
            buffer.put(neighcells.get(i).toByteArray());
        }

        return buffer.array();
    }

    short getByteLen() {

        short neigblistlen = 0;
        for(int i = 0; i < neighcells.size(); i++) {
            neigblistlen += neighcells.get(i).getByteLen();
        }

        return (short)(64 + 2 + neigblistlen);
    }
}

class wifi_info_t implements Serializable {
    String mac;
    long level;
    String ssid;
    long frequency;
    boolean connect;
    //当前时间与wifi结果中时间差值
    long time_diff;

    String toJson() {
        return new StringBuilder().append(Constants.joLeft)
               .append(Constants.js_req_wifi_mac).append(Constants.jsAssi).append(Constants.formatString(mac)).append(Constants.jsSepr)
               .append(Constants.js_req_wifi_level).append(Constants.jsAssi).append(level).append(Constants.jsSepr)
               .append(Constants.js_req_wifi_ssid).append(Constants.jsAssi).append(Constants.formatString(ssid)).append(Constants.jsSepr)
               .append(Constants.js_req_wifi_frequency).append(Constants.jsAssi).append(frequency).append(Constants.jsSepr)
                .append(Constants.js_req_wifi_connect).append(Constants.jsAssi).append(connect).append(Constants.jsSepr)
                .append(Constants.js_req_time_diff).append(Constants.jsAssi).append(time_diff)
               .append(Constants.joRight).toString();
    }

    static wifi_info_t toObject(String jsonStr) {
        try {
            wifi_info_t newobj = new wifi_info_t();

            newobj.mac = Constants.getJsonObjectString(jsonStr, Constants.js_req_wifi_mac);
            newobj.level = Long.parseLong(Constants.getJsonObject(jsonStr, Constants.js_req_wifi_level));
            newobj.ssid = Constants.getJsonObjectString(jsonStr, Constants.js_req_wifi_ssid);
            newobj.frequency = Long.parseLong(Constants.getJsonObject(jsonStr, Constants.js_req_wifi_frequency));

            return newobj;
        } catch (Exception e) {
            LogHelper.writeException(e);
            return null;
        }
    }

    byte[] toByteArray() {

        short len = getByteLen();

        ByteBuffer buffer = ByteBuffer.allocate(len);

        LocationDataDef.fillStringToByteBuffer(mac, buffer);
        buffer.putLong(level);
        LocationDataDef.fillStringToByteBuffer(ssid, buffer);
        buffer.putLong(frequency);

        return buffer.array();
    }

    short getByteLen() {
        return (short)(16
                + 2 + LocationDataDef.calcStringLen(mac)
                + 2 + LocationDataDef.calcStringLen(ssid));
    }
}

class user_sensors_info_t implements Serializable {
    long timestamp;
    boolean wifi_open_not;
    boolean gps_open_not;
    int connect_type;
    int air_press;
    int light_value;
    int gps_inter;
    int location_switch_level;
    boolean wifi_scan_available;
    int location_permission;

    String toJson() {

        return new StringBuilder().append(Constants.joLeft)
                .append(Constants.js_req_sensor_timestamp).append(Constants.jsAssi).append(timestamp).append(Constants.jsSepr)
                .append(Constants.js_req_sensor_wifi_open_not).append(Constants.jsAssi).append(wifi_open_not).append(Constants.jsSepr)
                .append(Constants.js_req_sensor_wifi_scan_available).append(Constants.jsAssi).append(wifi_scan_available).append(Constants.jsSepr)
                .append(Constants.js_req_sensor_gps_open_not).append(Constants.jsAssi).append(gps_open_not).append(Constants.jsSepr)
                .append(Constants.js_req_sensor_wifi_connect_not).append(Constants.jsAssi).append(connect_type).append(Constants.jsSepr)
                .append(Constants.js_req_sensor_air_press).append(Constants.jsAssi).append(air_press).append(Constants.jsSepr)
                .append(Constants.js_req_sensor_light_value).append(Constants.jsAssi).append(light_value).append(Constants.jsSepr)
                .append(Constants.js_req_sensor_gps_inter).append(Constants.jsAssi).append(gps_inter).append(Constants.jsSepr)
                .append(Constants.js_req_sensor_location_switch_level).append(Constants.jsAssi).append(location_switch_level).append(Constants.jsSepr)
                .append(Constants.js_req_sensor_location_permission).append(Constants.jsAssi).append(location_permission)
                .append(Constants.joRight).toString();
    }
    String toBamaiLog() {

        return new StringBuilder().append(Constants.joLeft)
                .append(Constants.js_req_sensor_timestamp).append(Constants.jsAssi).append(timestamp).append(Constants.jsSepr)
                .append(Constants.js_req_sensor_wifi_open_not).append(Constants.jsAssi).append(wifi_open_not).append(Constants.jsSepr)
                .append(Constants.js_req_sensor_wifi_scan_available).append(Constants.jsAssi).append(wifi_scan_available).append(Constants.jsSepr)
                .append(Constants.js_req_sensor_gps_open_not).append(Constants.jsAssi).append(gps_open_not).append(Constants.jsSepr)
                .append(Constants.js_req_sensor_wifi_connect_not).append(Constants.jsAssi).append(connect_type).append(Constants.jsSepr)
//                .append(Const.js_req_sensor_air_press).append(Const.jsAssi).append(air_press).append(Const.jsSepr)
//                .append(Const.js_req_sensor_light_value).append(Const.jsAssi).append(light_value).append(Const.jsSepr)
//                .append(Const.js_req_sensor_gps_inter).append(Const.jsAssi).append(gps_inter).append(Const.jsSepr)
                .append(Constants.js_req_sensor_location_switch_level).append(Constants.jsAssi).append(location_switch_level).append(Constants.jsSepr)
                .append(Constants.js_req_sensor_location_permission).append(Constants.jsAssi).append(location_permission)
                .append(Constants.joRight).toString();
    }
}

class LocationServiceRequest implements Serializable, Cloneable {
    location_user_info_t user_info = new location_user_info_t();
    cell_info_t cell = new cell_info_t();
    List<wifi_info_t> wifis = new ArrayList<>();
    long valid_flag;
    long version;
    long trace_id;
    String tencent_loc;
    String listeners_info;
    user_sensors_info_t user_sensors_info = new user_sensors_info_t();

    @Override
    protected Object clone() throws CloneNotSupportedException{
        LocationServiceRequest request = (LocationServiceRequest)super.clone();

        request.valid_flag = this.valid_flag;
        //注释掉的信息会在每轮loop中重新赋值。
//        request.version = this.version;
//        request.trace_id = this.trace_id;
//        request.tencent_loc = this.tencent_loc;

        request.user_info = new location_user_info_t();
        //以下信息会在每轮loop中重新赋值。
//        request.user_info.timestamp = this.user_info.timestamp;
//        request.user_info.imei = this.user_info.imei;
//        request.user_info.app_id = this.user_info.app_id;
//        request.user_info.user_id = this.user_info.user_id;
//        request.user_info.phone = this.user_info.phone;
//        request.user_info.modellevel = this.user_info.modellevel;

        request.cell = new cell_info_t();
        request.cell.mcc = this.cell.mcc;
        request.cell.mnc_sid = this.cell.mnc_sid;
        request.cell.lac_nid = this.cell.lac_nid;
        request.cell.cellid_bsid = this.cell.cellid_bsid;
        request.cell.rssi = this.cell.rssi;
        request.cell.type = this.cell.type;
        request.cell.lon_cdma = this.cell.lon_cdma;
        request.cell.lat_cdma = this.cell.lat_cdma;
        request.cell.neighcells = new ArrayList<>();
        request.cell.neighcells.addAll(this.cell.neighcells);

        request.wifis = new ArrayList<>();
        request.wifis.addAll(this.wifis);

        request.user_sensors_info = new user_sensors_info_t();
        //以下信息会在每轮loop中重新赋值。
//        request.user_sensors_info.timestamp = this.user_sensors_info.timestamp;
//        request.user_sensors_info.wifi_open_not = this.user_sensors_info.wifi_open_not;
//        request.user_sensors_info.gps_open_not = this.user_sensors_info.gps_open_not;
//        request.user_sensors_info.connect_type = this.user_sensors_info.connect_type;
//        request.user_sensors_info.air_press = this.user_sensors_info.air_press;
//        request.user_sensors_info.light_value = this.user_sensors_info.light_value;
//        request.user_sensors_info.gps_inter = this.user_sensors_info.gps_inter;

        return request;
    }

    protected Object deepClone() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(this);
        ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
        ObjectInputStream oi = new ObjectInputStream(bi);
        return oi.readObject();
    }

    String toJson() {

        String wifiinfostr = Constants.jaLeft;
        for(int i = 0; i < wifis.size(); i++) {
            if(i != 0) wifiinfostr += Constants.jsSepr;
            wifiinfostr += wifis.get(i).toJson();
        }
        wifiinfostr += Constants.jaRight;

        return new StringBuilder().append(Constants.joLeft)
                .append(Constants.js_req_user_info).append(Constants.jsAssi).append(user_info.toJson()).append(Constants.jsSepr)
                .append(Constants.js_req_cell).append(Constants.jsAssi).append(cell.toJson()).append(Constants.jsSepr)
                .append(Constants.js_req_wifis).append(Constants.jsAssi).append(wifiinfostr).append(Constants.jsSepr)
                .append(Constants.js_req_valid_flag).append(Constants.jsAssi).append(valid_flag).append(Constants.jsSepr)
                .append(Constants.js_req_version).append(Constants.jsAssi).append(version).append(Constants.jsSepr)
                .append(Constants.js_req_trace_id).append(Constants.jsAssi).append(trace_id).append(Constants.jsSepr)
                .append(Constants.js_req_tencent_loc).append(Constants.jsAssi).append(Constants.formatString(tencent_loc)).append(Constants.jsSepr)
                .append(Constants.js_req_user_sensors_info).append(Constants.jsAssi).append(user_sensors_info.toJson()).append(Constants.jsSepr)
                .append(Constants.js_req_listeners_info).append(Constants.jsAssi).append(Constants.formatString(listeners_info))
                .append(Constants.joRight).toString();
    }
    String toBamaiLog() {

        String wifiinfostr = Constants.jaLeft;
        for(int i = 0; i < wifis.size() && i < 2; i++) {
            if(i != 0) wifiinfostr += Constants.jsSepr;
            wifiinfostr += wifis.get(i).toJson();
        }
        wifiinfostr += Constants.jaRight;

        return new StringBuilder().append(Constants.joLeft)
                .append(Constants.js_req_user_info).append(Constants.jsAssi).append(user_info.toJson()).append(Constants.jsSepr)
                .append(Constants.js_req_cell).append(Constants.jsAssi).append(cell.toJson()).append(Constants.jsSepr)
                .append(Constants.js_req_wifis).append(Constants.jsAssi).append(wifiinfostr).append(Constants.jsSepr)
//                .append(Const.js_req_valid_flag).append(Const.jsAssi).append(valid_flag).append(Const.jsSepr)
                .append(Constants.js_req_version).append(Constants.jsAssi).append(version).append(Constants.jsSepr)
                .append(Constants.js_req_trace_id).append(Constants.jsAssi).append(trace_id).append(Constants.jsSepr)
//                .append(Const.js_req_tencent_loc).append(Const.jsAssi).append(Const.formatString(tencent_loc)).append(Const.jsSepr)
                .append(Constants.js_req_user_sensors_info).append(Constants.jsAssi).append(user_sensors_info.toBamaiLog())
                .append(Constants.joRight).toString();
    }

    static LocationServiceRequest toObject(String jsonStr) {
        try {
            LocationServiceRequest newobj = new LocationServiceRequest();

            newobj.user_info = location_user_info_t.toObject(Constants.getJsonObject(jsonStr, Constants.js_req_user_info));
            newobj.cell = cell_info_t.toObject(Constants.getJsonObject(jsonStr, Constants.js_req_cell));
            newobj.valid_flag = Long.parseLong(Constants.getJsonObject(jsonStr, Constants.js_req_valid_flag));
            newobj.version = Long.parseLong(Constants.getJsonObject(jsonStr, Constants.js_req_version));
            newobj.trace_id = Long.parseLong(Constants.getJsonObject(jsonStr, Constants.js_req_trace_id));

            if(jsonStr.contains(Constants.js_req_tencent_loc)) newobj.tencent_loc = Constants.getJsonObjectString(jsonStr, Constants.js_req_tencent_loc);

            ArrayList<String> wifistrs = Constants.getJsonArrayObjects(Constants.getJsonObject(jsonStr, Constants.js_req_wifis));
            for(String wif : wifistrs) {
                newobj.wifis.add(wifi_info_t.toObject(wif));
            }

            return newobj;
        } catch (Exception e) {
            LogHelper.writeException(e);
            return null;
        }
    }

    byte[] toByteArray() {

        short len_wifilist = 0;
        for(int i = 0; i < wifis.size(); i++) {
            len_wifilist += wifis.get(i).getByteLen();
        }

        short len = getByteLen();

        ByteBuffer buffer = ByteBuffer.allocate(len);

        buffer.put(user_info.toByteArray())
                .put(cell.toByteArray());

        buffer.putShort(len_wifilist);

        for(int i = 0; i < wifis.size(); i++) {
            buffer.put(wifis.get(i).toByteArray());
        }

        buffer.putLong(valid_flag);
        buffer.putLong(version);
        buffer.putLong(trace_id);

        LocationDataDef.fillStringToByteBuffer(tencent_loc, buffer);

        return buffer.array();

    }

    short getByteLen() {

        short len_userinfo = user_info.getByteLen();
        short len_cellinfo = cell.getByteLen();
        short len_wifilist = 0;
        for(int i = 0; i < wifis.size(); i++) {
            len_wifilist += wifis.get(i).getByteLen();
        }

        return (short)(len_userinfo + len_cellinfo
                + 2 + len_wifilist
                + 24
                + 2 + LocationDataDef.calcStringLen(tencent_loc));

    }
}

class location_info_t implements Serializable {
    double lon_gcj;
    double lat_gcj;
    long accuracy;
    double confidence;

    String toJson() {
        return new StringBuilder().append(Constants.joLeft)
                .append(Constants.js_rsp_loc_lon_gcj).append(Constants.jsAssi).append(Constants.formatDouble(lon_gcj, Constants.lonlatDots)).append(Constants.jsSepr)
                .append(Constants.js_rsp_loc_lat_gcj).append(Constants.jsAssi).append(Constants.formatDouble(lat_gcj, Constants.lonlatDots)).append(Constants.jsSepr)
                .append(Constants.js_rsp_loc_accuracy).append(Constants.jsAssi).append(accuracy).append(Constants.jsSepr)
                .append(Constants.js_rsp_loc_confidence).append(Constants.jsAssi).append(Constants.formatDouble(confidence, Constants.confiprobDots))
                .append(Constants.joRight).toString();
    }

    static location_info_t toObject(String jsonStr) {
        try {
            location_info_t newobj = new location_info_t();
            String jslongcj = Constants.getJsonObject(jsonStr, Constants.js_rsp_loc_lon_gcj);
            String jslatgcj = Constants.getJsonObject(jsonStr, Constants.js_rsp_loc_lat_gcj);
            String jsaccuracy = Constants.getJsonObject(jsonStr, Constants.js_rsp_loc_accuracy);
            String jsconfidence = Constants.getJsonObject(jsonStr, Constants.js_rsp_loc_confidence);
            if (jslongcj.length() == 0
                    || jslatgcj.length() == 0
                    || jsaccuracy.length() == 0
                    || jsconfidence.length() == 0) return null;

            newobj.lon_gcj = Double.parseDouble(jslongcj);
            newobj.lat_gcj = Double.parseDouble(jslatgcj);
            newobj.accuracy = Long.parseLong(jsaccuracy);
            newobj.confidence = Double.parseDouble(jsconfidence);

            return newobj;
        } catch (Exception e) {
            LogHelper.writeException(e);
            return null;
        }
    }
}

class LocationServiceResponse implements Serializable {
    private final String RSP_COORDINATE_WGS84 = "wgs84ll";
    private final String RSP_COORDINATE_GCJ02 = "gcj02ll";
    int ret_code;
    String ret_msg;
    long timestamp;
    private String coord_system;
    List<location_info_t> locations = new ArrayList<>();

    int getCoordinateType() {
        int coordinateType = -1;
        if (!TextUtils.isEmpty(coord_system)) {
            switch (coord_system) {
                case RSP_COORDINATE_WGS84:
                    coordinateType = DefaultLocation.COORDINATE_TYPE_WGS84;
                    break;
                case RSP_COORDINATE_GCJ02:
                    coordinateType = DefaultLocation.COORDINATE_TYPE_GCJ02;
                    break;
                default:
                    break;
            }
        }
        return coordinateType;
    }

    String toJson() {

        String locationsstr = Constants.jaLeft;
        for(int i = 0; i < locations.size(); i++) {
            if(i != 0) locationsstr += Constants.jsSepr;
            locationsstr += locations.get(i).toJson();
        }
        locationsstr += Constants.jaRight;

        return new StringBuilder().append(Constants.joLeft)
                .append(Constants.js_rsp_ret_code).append(Constants.jsAssi).append(ret_code).append(Constants.jsSepr)
                .append(Constants.js_rsp_ret_msg).append(Constants.jsAssi).append(Constants.formatString(ret_msg)).append(Constants.jsSepr)
                .append(Constants.js_rsp_timestamp).append(Constants.jsAssi).append(timestamp).append(Constants.jsSepr)
                .append(Constants.js_rsp_coord_system).append(Constants.jsAssi).append(coord_system).append(Constants.jsSepr)
                .append(Constants.js_rsp_locations).append(Constants.jsAssi).append(locationsstr)
                .append(Constants.joRight).toString();
    }

    static LocationServiceResponse toObject(String jsonStr) {
        try {
            LocationServiceResponse newobj = new LocationServiceResponse();

            String jsretcode = Constants.getJsonObject(jsonStr, Constants.js_rsp_ret_code);
            String jstimestamp = Constants.getJsonObject(jsonStr, Constants.js_rsp_timestamp);
            if (jsretcode.length() == 0 || jstimestamp.length() == 0) return null;

            newobj.ret_code = Integer.parseInt(jsretcode);
            newobj.ret_msg = Constants.getJsonObjectString(jsonStr, Constants.js_rsp_ret_msg);
            newobj.timestamp = Long.parseLong(jstimestamp);
            newobj.coord_system = Constants.getJsonObjectString(jsonStr, Constants.js_rsp_coord_system);

            ArrayList<String> locstrs = Constants.getJsonArrayObjects(Constants.getJsonObject(jsonStr, Constants.js_rsp_locations));
            for(String loc : locstrs) {
                location_info_t oneloc = location_info_t.toObject(loc);
                if (oneloc != null) {
                    newobj.locations.add(oneloc);
                }
            }

            return newobj;
        } catch (Exception e) {
            LogHelper.writeException(e);
            return null;
        }
    }
}

class GpsData implements Serializable {
    long timestamp;
    double lon_wgs84;
    double lat_wgs84;
    int altitude;
    int accuracy;
    int speed;
    int bearing;

    @Override
    public String toString() {
        return "gpsdata=timestamp=" + timestamp + ",lon_wgs84=" + lon_wgs84 + ",lat_wgs84=" + lat_wgs84 + ",altitude=" + altitude + ",accuracy=" + accuracy + ",speed=" + speed + ",bearing=" + bearing;
    }
    protected Object deepClone() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(this);
        ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
        ObjectInputStream oi = new ObjectInputStream(bi);
        return oi.readObject();
    }

    String toJson() {
        return Constants.joLeft
                + "\"timestamp\"" + Constants.jsAssi + timestamp + Constants.jsSepr
                + "\"lon_wgs84\"" + Constants.jsAssi + Constants.formatDouble(lon_wgs84, Constants.lonlatDots) + Constants.jsSepr
                + "\"lat_wgs84\"" + Constants.jsAssi + Constants.formatDouble(lat_wgs84, Constants.lonlatDots) + Constants.jsSepr
                + "\"altitude\"" + Constants.jsAssi + altitude + Constants.jsSepr
                + "\"accuracy\"" + Constants.jsAssi + accuracy + Constants.jsSepr
                + "\"speed\"" + Constants.jsAssi + speed + Constants.jsSepr
                + "\"bearing\"" + Constants.jsAssi + bearing
                + Constants.joRight;
    }

    static GpsData toObject(String jsonStr) {
        try {
            GpsData newobj = new GpsData();

            newobj.timestamp = Long.parseLong(Constants.getJsonObject(jsonStr, "\"timestamp\""));
            newobj.lon_wgs84 = Double.parseDouble(Constants.getJsonObject(jsonStr, "\"lon_wgs84\""));
            newobj.lat_wgs84 = Double.parseDouble(Constants.getJsonObject(jsonStr, "\"lon_wgs84\""));
            newobj.altitude = Integer.parseInt(Constants.getJsonObject(jsonStr, "\"altitude\""));
            newobj.accuracy = Integer.parseInt(Constants.getJsonObject(jsonStr, "\"accuracy\""));
            newobj.speed = Integer.parseInt(Constants.getJsonObject(jsonStr, "\"speed\""));
            newobj.bearing = Integer.parseInt(Constants.getJsonObject(jsonStr, "\"bearing\""));

            return newobj;
        } catch (Exception e) {
            LogHelper.writeException(e);
            return null;
        }
    }
}
