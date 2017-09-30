package com.ns.yc.lifehelper.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/1/20
 * 描    述：存放基本常量
 * 修订历史：
 * ================================================
 */
public class Constant {

    public static String expressDelivery = "";
    //数据库的版本号
    public static int REALM_VERSION = 0;
    //数据库的名称
    public static String REALM_NAME = "yc";
    //性别
    public static String sex = "sex";
    //sp的名称
    public static String SP_NAME = "yc";
    //记录用户是否登录
    public static boolean isLogin;
    //投资界应用宝评分链接
    public static String QQUrl = "http://android.myapp.com/myapp/detail.htm?apkName=com.zero2ipo.harlanhu.pedaily";

    @Retention(RetentionPolicy.SOURCE)
    public @interface Gender {
        String MALE = "male";
        String FEMALE = "female";
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface CateType {
        String HOT = "hot";
        String NEW = "new";
        String REPUTATION = "reputation";
        String OVER = "over";
    }


}
