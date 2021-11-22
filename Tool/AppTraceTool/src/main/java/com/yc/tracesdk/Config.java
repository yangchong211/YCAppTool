package com.yc.tracesdk;

public class Config {
    public final static boolean DEBUG = false;
    public final static boolean TEST = false; // use in test for rate of flow, cause do not upload while wifi is connected !!.
    public final static String URL3 = "http://pre.diditaxi.com.cn/api/v2/c_uploadtracedata";
    public final static String URL2 = "http://10.10.9.18/bigdata/location/c_uploadtracedata";
    public final static String URL = "http://locstorage.map.xiaojukeji.com/map/loc/wificell/collection";

}
