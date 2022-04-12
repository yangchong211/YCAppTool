package com.ycbjie.android.util

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import com.blankj.utilcode.util.Utils


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

    //Kt文件中的声明方式： object 关键字声明,其内部不允许声明构造方法
    object SingleObject {
        fun getApp(): Application {
            if (Utils.getApp()!=null){
                return Utils.getApp()
            }
            throw NullPointerException("u should init first")
        }
    }


}