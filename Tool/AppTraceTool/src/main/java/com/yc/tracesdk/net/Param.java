package com.yc.tracesdk.net;

import java.util.HashMap;

public class Param {
    /** 请求URL */
    public String mUrl;
    /** url参数 */
    public HashMap<String, String> mPostMap = new HashMap<String, String>();
    /** 通过Post方式提交到服务器的data数据 */
    public HashMap<String, byte[]> mPostData = new HashMap<String, byte[]>();
}
