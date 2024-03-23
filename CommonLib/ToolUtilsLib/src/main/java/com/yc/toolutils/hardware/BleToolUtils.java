package com.yc.toolutils.hardware;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

public final class BleToolUtils {

    /**
     * 判断是否支持蓝牙
     *
     * @return
     */
    public static boolean isSupportBle(Context context) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
                && context.getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

}
