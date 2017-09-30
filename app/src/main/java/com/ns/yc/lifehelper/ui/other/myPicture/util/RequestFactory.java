package com.ns.yc.lifehelper.ui.other.myPicture.util;

import okhttp3.Request;

/**
 * Created by PC on 2017/9/1.
 * 作者：PC
 */

public class RequestFactory {

    public static final String URL = "http://www.mzitu.com/";

    public static Request make(String path) {
        return new Request
                .Builder()
                .url(URL + path)
                .build();
    }

}
