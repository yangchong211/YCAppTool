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

    }

    /**
     * 笔记模块
     */
    interface Note{

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
    interface Library{

    }
}
