package com.yc.configlayer.constant;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2016/1/20
 * 描    述：存放基本常量
 * 修订历史：
 * ================================================
 */
public class Constant {

    public static final String SP_NAME = "yc";

    public static final String GITHUB = "https://github.com/yangchong211/YCBlogs";
    public static final String LIFE_HELPER = "https://github.com/yangchong211/LifeHelper";
    public static final String JIAN_SHU = "https://www.jianshu.com/u/b7b2c6ed9284";
    public static final String JUE_JIN = "https://juejin.im/user/5939433efe88c2006afa0c6e";
    public static final String ZHI_HU = "https://www.zhihu.com/people/yczbj/activities";
    public static final String FLUTTER = "https://github.com/yangchong211/ycflutter";

    /**
     * 配合CoordinatorLayout使用
     */
    @Retention(RetentionPolicy.SOURCE)
    public @interface STATES{
        int EXPANDED = 3;
        int COLLAPSED = 2;
        int INTERMEDIATE = 1;
    }

    /**-------------------------------------键-------------------------------------------------**/
    //Sp键
    public static final String KEY_FIRST_SPLASH = "first_splash";                 //是否第一次启动
    public static final String KEY_IS_LOGIN = "is_login";                         //登录
    public static final String KEY_IS_SHOW_LIST_IMG = "is_show_list_img";         //是否展示list页面图片
    public static final String KEY_IS_SHOW_GIRL_IMG = "is_show_girl_img";         //启动页是否是妹子图
    public static final String KEY_IS_SHOW_IMG_RANDOM = "is_show_girl_random";    //启动页是否概率出现
    public static final String KEY_THUMBNAIL_QUALITY = "thumbnail_quality";       //启动页是否概率出现
    public static final String KEY_BANNER_URL = "banner_url";                     //启动页是否概率出现
    public static final String KEY_NIGHT_STATE = "night_state";                   //启动页夜间模式



    /**-------------------------------------集合-------------------------------------------------**/
    public static final String URL = "url";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";


}
