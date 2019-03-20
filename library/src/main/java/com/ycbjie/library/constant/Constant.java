package com.ycbjie.library.constant;

import android.os.Environment;

import com.ycbjie.library.model.HomeBlogEntity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

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

    public static final int REALM_VERSION = 1;
    public static final String REALM_NAME = "life";
    public static final String SP_NAME = "yc";
    public static final String EXTERNAL_STORAGE_DIRECTORY =
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/";

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
    public static final int NEW_ACTIVITY_REQUEST_CODE = 0;
    public static final int EDIT_ACTIVITY_REQUEST_CODE = 1;
    public static final String INTENT_EXTRA_DAY_OF_WEEK = "DAY_OF_WEEK";
    public static final String INTENT_BUNDLE_NEW_TASK_DETAIL = "NEW_TASK_DETAIL";
    public static final String INTENT_EXTRA_EDIT_TASK_DETAIL_ENTITY = "EDIT_TASK_DETAIL_ENTITY";
    public static final String INTENT_EXTRA_MODE_OF_NEW_ACT = "MODE_OF_NEW_ACT";
    public static final String INTENT_EXTRA_SWITCH_TO_INDEX = "SWITCH_TO_INDEX";
    public static final String CHOOSE_PAPER_DIALOG_CHECK_ITEM_BUNDLE_KEY = "CHECK_ITEM_BUNDLE_KEY";
    public static final String DATABASE_FILE_PATH_FOLDER = "WeekToDo";
    public static final String DATABASE_FILE_PATH_FILE_NAME = "data.realm";
    public static final String DATABASE_FILE_BACKUP_PATH_FOLDER = "番茄周/备份";
    public static final String DATABASE_FILE_EXPORT_PATH_FOLDER = "番茄周/导出";
    public static final long AUTO_NOTIFY_INTERVAL_TIME = 60 * 60 * 1000;
    public static final int AUTO_NOTIFY_NOTIFICATION_ID = 0;
    public static final int DAY_OF_WEEK = 7;
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


    public interface LikeType{
        int TYPE_ZHI_HU = 101;
        int TYPE_GIRL = 105;
        int TYPE_WE_CHAT = 106;
        int TYPE_GOLD = 108;
    }


    public interface DetailKeys{
        String IT_DETAIL_URL = "url";
        String IT_DETAIL_IMG_URL = "img_url";
        String IT_DETAIL_ID = "id";
        String IT_DETAIL_TYPE = "type";
        String IT_DETAIL_TITLE = "title";
        String IT_GOLD_TYPE = "type";
        String IT_GOLD_TYPE_STR = "type_str";
        String IT_GOLD_MANAGER = "manager";
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
    //
    public static List<HomeBlogEntity> findNews = new ArrayList<>();
    public static List<HomeBlogEntity> findBottomNews = new ArrayList<>();

    /**-------------------------------------集合-------------------------------------------------**/
    public static final String URL = "url";
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";


    /**-------------------------------------music-------------------------------------------------**/
    public static final String EXTRA_NOTIFICATION = "extra_notification";
    public static final String LOCK_SCREEN = "lock_screen";
    public static final String LOCK_SCREEN_ACTION = "cn.ycbjie.lock";
    public static final String FILTER_SIZE = "filter_size";
    public static final String FILTER_TIME = "filter_time";
    public static final String MUSIC_ID = "music_id";
    public static final String PLAY_MODE = "play_mode";
    public static final String IS_SCREEN_LOCK = "is_screen_lock";
    public static final String APP_OPEN_COUNT = "app_open_count";
    public static final String PLAY_POSITION = "play_position";


    /**
     * 网络缓存最大值
     */
    public static final int CACHE_MAXSIZE = 1024 * 1024 * 30;

    /**
     * 网络缓存保存时间
     */
    public static final int TIME_CACHE = 60 * 60;
}
