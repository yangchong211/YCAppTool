package com.ns.yc.lifehelper.api;

import com.ns.yc.lifehelper.R;

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
    //loading加载style样式
    public static int loadingStyle = R.style.Loading;


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

    public class status{
        public static final int success=200;
        public static final int error=-1;
    }

    /**-------------------------------------记事本----------------------------------------------**/
    public static String DAY_OF_WEEK = "day_of_week";
    public static String ADD_NEW_OLD = "add_new_old";
    public interface NOTE_TYPE{
        int add_new = 1;            //新建
        int add_old = 2;            //修改
    }


    /**-------------------------------------键-------------------------------------------------**/
    //Sp键
    public static String KEY_FIRST_SPLASH = "first_splash";                 //是否第一次启动
    public static String KEY_IS_LOGIN = "is_login";                         //登录
    public static String KEY_IS_SHOW_LIST_IMG = "is_show_list_img";         //是否展示list页面图片
    public static String KEY_IS_SHOW_GIRL_IMG = "is_show_girl_img";         //启动页是否是妹子图
    public static String KEY_IS_SHOW_IMG_RANDOM = "is_show_girl_random";    //启动页是否概率出现
    public static String KEY_THUMBNAIL_QUALITY = "thumbnail_quality";       //启动页是否概率出现
    public static String KEY_BANNER_URL = "banner_url";                     //启动页是否概率出现

}
