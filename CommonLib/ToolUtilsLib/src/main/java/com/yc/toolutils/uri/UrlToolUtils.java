package com.yc.toolutils.uri;

import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UrlToolUtils {

    /**
     * 拼接字符串
     * 比如：https://github.com/yangchong211/LifeHelper?name=yc&age=26
     * @param url                       url
     * @param map                       map集合
     * @return
     */
    public static String getUrl(String url, HashMap<String, String> map){
        if(TextUtils.isEmpty(url)){
            return null;
        }
        //解析一个url
        Uri uri = Uri.parse(url);
        Uri.Builder builder = uri.buildUpon();
        if (map != null && map.size() > 0) {
            Set<Map.Entry<String, String>> entries = map.entrySet();
            //使用迭代器进行遍历
            for (Object o : entries) {
                Map.Entry entry = (Map.Entry) o;
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                //对键和值进行编码，然后将参数追加到查询字符串中。
                builder.appendQueryParameter(key, value);
            }
        }
        return builder.toString();
    }

}
