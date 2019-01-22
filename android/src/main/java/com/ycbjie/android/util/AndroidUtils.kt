package com.ycbjie.android.util

import android.content.Context
import android.content.pm.PackageManager


/**
 * <pre>
 *     @author 杨充
 *     blog  :
 *     time  : 2017/05/30
 *     desc  : 工具类
 *     revise: 我们需要一个类里面有一些静态的属性、常量或者函数，我们可以使用伴随对象。这个对象被这个类的所有对象
 *             所共享，就像java中的静态属性或者方法。在类声明内部可以用 companion 关键字标记对象声明：
 * </pre>
 */
class AndroidUtils{

    companion object {

        const val name = "yc"

        fun create(): AndroidUtils = AndroidUtils()

        /**
         * 获取当前本地apk的版本
         *
         * @param mContext
         * @return
         */
        fun getVersionCode(mContext: Context): Int {
            var versionCode = 0
            try {
                //获取软件版本号，对应AndroidManifest.xml下android:versionCode
                versionCode = mContext.packageManager.getPackageInfo(mContext.packageName,
                        0).versionCode
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return versionCode
        }
    }

}