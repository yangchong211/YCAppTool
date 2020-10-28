package com.yc.configlayer.arounter;

public interface RouterConfig {


    String SCHEME = "yc";
    String ROUTER_SCHEME = "yc://";
    String ROUTER_HOST = "cc、om.ns.yc.lifehelper";
    String ROUTER_PRE = ROUTER_SCHEME + ROUTER_HOST;
    String PATH_TOP_LEVEL_VIEW = "/view";
    String PATH_TOP_LEVEL_ACTION = "/action";

    /**
     * 主App
     */
    interface App {
        //设置中心
        String ACTIVITY_APP_SETTING_ACTIVITY = "/app/MeSettingActivity";

    }

    /**
     * Android模块
     */
    interface Android{
        //跳转到玩Android模块的首页面
        String ACTIVITY_ANDROID_ACTIVITY = "/android/AndroidActivity";
    }

    /**
     * 豆瓣模块
     */
    interface DouBan{
        //跳转到豆瓣电影页面
        String ACTIVITY_DOU_MOVIE_ACTIVITY = "/dou/DouMovieActivity";
        //跳转到豆瓣音乐页面
        String ACTIVITY_DOU_MUSIC_ACTIVITY = "/dou/DouMusicActivity";
        //跳转到豆瓣读书页面
        String ACTIVITY_DOU_BOOK_ACTIVITY = "/dou/DouBookActivity";
        //跳转到豆瓣电影评分榜页面
        String ACTIVITY_DOU_TOP_ACTIVITY = "/dou/MovieTopActivity";
    }

    /**
     * 游戏模块
     */
    interface Game{
        //拼图游戏
        String ACTIVITY_OTHER_PIN_TU_ACTIVITY = "/game/PinTuGameActivity";
        //飞机大战
        String ACTIVITY_OTHER_AIR_ACTIVITY = "/game/AirGameActivity";
        //绘画页面
        String ACTIVITY_BOOK_DOODLE_ACTIVITY = "/game/DoodleViewActivity";
        //老虎机页面
        String ACTIVITY_OTHER_MONKEY_ACTIVITY = "/game/MonkeyGameActivity";
    }

    /**
     * Gank模块
     */
    interface Gank{
        //我的干货页面
        String ACTIVITY_GANK_KNOWLEDGE_ACTIVITY = "/gank/MyKnowledgeActivity";
        //干货集中营首页
        String ACTIVITY_GANK_ACTIVITY = "/gank/GanKHomeActivity";
    }

    /**
     * 游戏模块
     */
    interface Love{
        //表达爱意
        String ACTIVITY_LOVE_ACTIVITY = "/love/LoveGirlMainActivity";
    }

    /**
     * 音乐模块
     */
    interface Music{
        //音乐首页
        String ACTIVITY_MUSIC_ACTIVITY = "/music/MusicActivity";
        //音乐启动页
        String ACTIVITY_MUSIC_GUIDE_ACTIVITY = "/music/GuideMusicActivity";
    }

    /**
     * 笔记模块
     */
    interface Note{
        //富文本文章页面
        String ACTIVITY_OTHER_ARTICLE = "/note/NewArticleActivity";
        //markdown首页
        String ACTIVITY_MARKDOWN_ACTIVITY = "/note/MdMainActivity";
    }

    /**
     * 视频模块
     */
    interface Video{
        //跳转到视频页面
        String ACTIVITY_VIDEO_VIDEO = "/video/VideoActivity";
    }

    /**
     * 基础模块
     */
    interface Demo{
        //画廊页面
        String ACTIVITY_OTHER_GALLERY_ACTIVITY = "/other/ImageGalleryActivity";
        //加载大图页面
        String ACTIVITY_LARGE_IMAGE_ACTIVITY = "/other/ZoomLargeImageActivity";
        //轮播图
        String ACTIVITY_OTHER_BANNER_ACTIVITY = "/other/MeBannerActivity";
        //轮播图
        String ACTIVITY_OTHER_BANNER_LIST_ACTIVITY = "/other/BannerViewActivity";
        //SnapHelper轮播图
        String ACTIVITY_OTHER_SNAPHELPER_ACTIVITY = "/other/SnapHelperActivity";
        //关于仿杀毒软件进度条控件
        String ACTIVITY_OTHER_PROGRESS1_ACTIVITY = "/other/ProgressFirstActivity";
        //下载圆形百分比进度条自定义控件
        String ACTIVITY_OTHER_PROGRESS2_ACTIVITY = "/other/ProgressSecondActivity";
        //直线百分比进度条自定义控件
        String ACTIVITY_OTHER_PROGRESS3_ACTIVITY = "/other/ProgressThirdActivity";
        //跳转到意见反馈页面
        String ACTIVITY_OTHER_FEEDBACK = "/other/MeFeedBackActivity";
        //跳转到关于项目更多页面
        String ACTIVITY_OTHER_ABOUT_ME = "/other/AboutMeActivity";
        //跳转到登陆页面
        String ACTIVITY_LOGIN_ACTIVITY = "/other/MeLoginActivity";
        //跳转到注册页面
        String ACTIVITY_REGISTER_ACTIVITY = "/other/MeRegisterActivity";
        //跳转到画廊相册页面
        String ACTIVITY_COVER_ACTIVITY = "/other/GalleryCoverActivity";
    }

    /**
     * 基础模块
     */
    interface Library{
        //跳转到webView详情页面
        String ACTIVITY_LIBRARY_WEB_VIEW = "/library/WebViewActivity";
    }

    /**
     * nfc
     */
    interface Nfc {
        //跳转测试nfc页面
        String ACTIVITY_NFC_MAIN = "/nfc/NfcMainActivity";
    }
}
