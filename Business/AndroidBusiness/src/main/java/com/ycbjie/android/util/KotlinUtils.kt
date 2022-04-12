package com.ycbjie.android.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager


object KotlinUtils{




    /**
     * 获取当前本地apk的版本
     * 如果是在Java中调用该静态方法，则必须添加上@JvmStatic
     * @param mContext
     * @return
     */
    @JvmStatic
    fun getVersionCode(mContext: Context): Int {
        var versionCode = 0
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.packageManager.getPackageInfo(mContext.packageName, 0).versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return versionCode
    }

    /**
     * 获取版本号名称
     *
     * @param context 上下文
     * @return
     */
    @JvmStatic
    fun getVerName(context: Context): String {
        var verName = ""
        try {
            verName = context.packageManager.getPackageInfo(context.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return verName
    }



    /**
     * 获取活动网络信息
     * <p>需添加权限
     * {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />}</p>
     *
     * @return NetworkInfo
     */
    @JvmStatic
    @SuppressLint("MissingPermission")
    fun isConnect(context: Context): Boolean? {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = manager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }


}
