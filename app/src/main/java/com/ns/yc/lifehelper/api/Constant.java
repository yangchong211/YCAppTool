package com.ns.yc.lifehelper.api;

import android.os.Environment;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.bean.HomeBlogEntity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

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

    public interface viewType{
        int typeBanner = 1;         //轮播图
        int typeGv = 2;             //九宫格
        int typeTitle = 3;          //标题
        int typeList = 4;           //list
        int typeNews = 5;           //新闻
        int typeMarquee = 6;        //跑马灯
        int typePlus = 7 ;          //不规则视图
        int typeSticky = 8;         //指示器
        int typeFooter = 9;         //底部
        int typeGvSecond = 10;      //九宫格
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

    public static int NEW_ACTIVITY_REQUEST_CODE = 0;
    public static int EDIT_ACTIVITY_REQUEST_CODE = 1;
    public static String INTENT_EXTRA_DAY_OF_WEEK = "DAY_OF_WEEK";
    public static String INTENT_BUNDLE_NEW_TASK_DETAIL = "NEW_TASK_DETAIL";
    public static String INTENT_EXTRA_EDIT_TASK_DETAIL_ENTITY = "EDIT_TASK_DETAIL_ENTITY";
    public static String INTENT_EXTRA_MODE_OF_NEW_ACT = "MODE_OF_NEW_ACT";
    public static String INTENT_EXTRA_SWITCH_TO_INDEX = "SWITCH_TO_INDEX";
    public static String CHOOSE_PAPER_DIALOG_CHECK_ITEM_BUNDLE_KEY = "CHECK_ITEM_BUNDLE_KEY";
    public static String ExternalStorageDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    public static String DATABASE_FILE_PATH_FOLDER = "WeekToDo";
    public static String DATABASE_FILE_PATH_FILE_NAME = "data.realm";
    public static String DATABASE_FILE_BACKUP_PATH_FOLDER = "番茄周/备份";
    public static String DATABASE_FILE_EXPORT_PATH_FOLDER = "番茄周/导出";

    public static long AUTO_NOTIFY_INTERVAL_TIME = 60 * 60 * 1000;
    public static int AUTO_NOTIFY_NOTIFICATION_ID = 0;
    public interface MODE_OF_NEW_ACT {
        int MODE_EDIT = 5;
        int MODE_CREATE = 6;
        int MODE_QUICK = 7;
    }

    public interface CONFIG_KEY {
        String SHOW_WEEK_TASK = "SHOW_WEEK_TASK";
        String SHOW_AS_LIST = "SHOW_AS_LIST";
        String SHOW_PRIORITY = "SHOW_PRIORITY";
        String NIGHT_MODE = "NIGHT_MODE";
        String AUTO_SWITCH_NIGHT_MODE = "AUTO_SWITCH_NIGHT_MODE";
        String BACKUP = "BACKUP";
        String RECOVERY = "RECOVERY";
        String AUTO_NOTIFY = "AUTO_NOTIFY";
    }

    public interface TaskState {
        int DEFAULT = 0;
        int FINISHED = 1;
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


    /**-------------------------------------集合-------------------------------------------------**/
    //
    public static List<HomeBlogEntity> findNews = new ArrayList<>();
    public static List<HomeBlogEntity> findBottomNews = new ArrayList<>();
}
