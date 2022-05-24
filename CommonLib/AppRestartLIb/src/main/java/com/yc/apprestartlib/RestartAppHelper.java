package com.yc.apprestartlib;

import android.content.Context;

public class RestartAppHelper {

    public static void restartApp(Context context, String type){
        IRestartApp iRestartApp = RestartFactory.create(type);
        iRestartApp.reStartApp(context);
    }

}
