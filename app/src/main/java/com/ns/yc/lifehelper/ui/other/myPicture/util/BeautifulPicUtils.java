package com.ns.yc.lifehelper.ui.other.myPicture.util;

/**
 * Created by PC on 2017/9/1.
 * 作者：PC
 */

public class BeautifulPicUtils {

    public static String url2GroupId(String url) {
        return url.split("/")[3];
    }

    public static String makeUrl(String type, String count) {
        String page = "";
        if (type.equals("")) {
            page = "page/";
            if (count.equals("")) {
                page = "";
            }
        } else {
            page = "/page/";
            if (count.equals("")) {
                page = "";
            }
        }
        return type + page + count;
    }
}
