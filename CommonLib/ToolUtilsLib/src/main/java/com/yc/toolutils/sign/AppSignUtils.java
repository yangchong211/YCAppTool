package com.yc.toolutils.sign;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.SigningInfo;
import android.os.Build;


import com.yc.toolutils.appinfo.AppInfoUtils;
import com.yc.toolutils.context.AppToolUtils;
import com.yc.appencryptlib.Md5EncryptUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCAppTool
 *     email  : yangchong211@163.com
 *     time  : 2016/09/23
 *     desc  : app签名相关工具类
 *     revise:
 * </pre>
 */
public final class AppSignUtils {


    public static String getAidlCheckAppInfoSign(){
        String appPackageName = AppInfoUtils.getAppPackageName();
        @SuppressLint("SimpleDateFormat")
        // 设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
        // new Date()为获取当前系统时间，也可使用当前时间戳
        String date = df.format(new Date());
        return Md5EncryptUtils.encryptMD5File1(date+"_"+appPackageName);
    }

    /**
     * 获取包签名MD5
     * @return
     */
    public static String getPackageSign() {
        String signStr = "-1";
        //获取包管理器
        PackageManager packageManager = AppToolUtils.getApp().getPackageManager();
        PackageInfo packageInfo;
        //获取当前要获取 SHA1 值的包名，也可以用其他的包名，但需要注意，
        //在用其他包名的前提是，此方法传递的参数 Context 应该是对应包的上下文。
        String packageName = AppToolUtils.getApp().getPackageName();
        //签名信息
        Signature[] signatures = null;
        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                packageInfo = packageManager.getPackageInfo(packageName,
                        PackageManager.GET_SIGNING_CERTIFICATES);
                SigningInfo signingInfo = packageInfo.signingInfo;
                signatures = signingInfo.getApkContentsSigners();
            } else {
                //获得包的所有内容信息类
                packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
                signatures = packageInfo.signatures;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (null != signatures && signatures.length > 0) {
            Signature sign = signatures[0];
            signStr = Md5EncryptUtils.encryptMD5ToString(sign.toByteArray()).toUpperCase();
        }
        return signStr;
    }

}
