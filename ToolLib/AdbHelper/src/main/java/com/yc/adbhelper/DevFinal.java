package com.yc.adbhelper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * detail: 常量类
 * @author Ttt
 */
public final class DevFinal {

    private DevFinal() {
    }

    /**
     * detail: 工具类内部返回值等常量定义
     * @author Ttt
     */
    public static final class INNER {

        // 异常错误返回值
        public static final int ERROR_INT = -1;
    }

    /**
     * detail: 符号、标记字符串常量
     * @author Ttt
     */
    public static final class SYMBOL {

        // 空格 字符串
        public static final String SPACE       = " ";
        // TAB 字符串
        public static final String TAB         = "\t";
        // 回车 ( CR ) 字符串
        public static final String CR          = "\r";
        // 换行 ( \n ) 字符串 ( single newline ('\n') character )
        public static final String NL          = "\n";
        public static final char   NL_CHAR     = '\n';
        // 点 字符串
        public static final String POINT       = ".";
        // 横杠 字符串
        public static final String HYPHEN      = "-";
        // 下划线 字符串
        public static final String UNDERSCORE  = "_";
        // 冒号 字符串
        public static final String COLON       = ":";
        // 逗号 字符串
        public static final String COMMA       = ",";
        // 顿号 字符串
        public static final String COMMA2      = "、";
        // 分号 字符串
        public static final String SEMICOLON   = ";";
        // 百分号 字符串
        public static final String PERCENT     = "%";
        // 反斜杠 字符串
        public static final String BACKSLASH   = "\\";
        // 斜杠 字符串
        public static final String SLASH       = "/";
        // 换行字符串
        public static final String NEW_LINE    = System.getProperty("line.separator");
        // 换行字符串 ( 两行 )
        public static final String NEW_LINE_X2 = NEW_LINE + NEW_LINE;
        // 换行字符串 ( 四行 )
        public static final String NEW_LINE_X4 = NEW_LINE_X2 + NEW_LINE_X2;
        // null 对象字符串
        public static final String NULL        = "null";
        // "" 对象字符串
        public static final String EMPTY       = "";
    }

    /**
     * detail: 编码格式字符串常量
     * @author Ttt
     */
    public static final class ENCODE {

        public static final String UNICODE    = "Unicode";
        public static final String US_ASCII   = "US-ASCII";
        public static final String ISO_8859_1 = "ISO-8859-1";
        public static final String UTF_8      = "UTF-8";
        public static final String UTF_16BE   = "UTF-16BE";
        public static final String UTF_16LE   = "UTF-16LE";
        public static final String UTF_16     = "UTF-16";
        public static final String GBK        = "GBK";
        public static final String GBK_2312   = "GBK-2312";
    }

    /**
     * detail: 时间格式字符串常量
     * @author Ttt
     */
    public static final class TIME {

        // 一分钟 60 秒
        public static final int MINUTE_S = 60;
        // 一小时 60 * 60 秒
        public static final int HOUR_S   = 3600;
        // 一天 24 * 60 * 60 秒
        public static final int DAY_S    = 86400;
        // 周与秒的倍数
        public static final int WEEK_S   = DAY_S * 7;
        // 月与秒的倍数
        public static final int MONTH_S  = DAY_S * 30;
        // 年与秒的倍数
        public static final int YEAR_S   = DAY_S * 365;

        // 秒与毫秒的倍数
        public static final long SECOND_MS = 1000;
        // 分与毫秒的倍数
        public static final long MINUTE_MS = SECOND_MS * 60;
        // 时与毫秒的倍数
        public static final long HOUR_MS   = MINUTE_MS * 60;
        // 天与毫秒的倍数
        public static final long DAY_MS    = HOUR_MS * 24;
        // 周与毫秒的倍数
        public static final long WEEK_MS   = DAY_MS * 7;
        // 月与毫秒的倍数
        public static final long MONTH_MS  = DAY_MS * 30;
        // 年与毫秒的倍数
        public static final long YEAR_MS   = DAY_MS * 365;

        // ==========
        // = 时间格式 =
        // ==========

        public static final String yy   = "yy";
        public static final String yyyy = "yyyy";
        public static final String MM   = "MM";
        public static final String dd   = "dd";
        public static final String HH   = "HH";
        public static final String mm   = "mm";
        public static final String ss   = "ss";

        public static final String yyMMdd            = "yyMMdd";
        public static final String yyMMdd_POINT      = "yy.MM.dd";
        public static final String yyMMdd_HYPHEN     = "yy-MM-dd";
        public static final String yyMMdd_UNDERSCORE = "yy_MM_dd";

        public static final String yyyyMMdd            = "yyyyMMdd";
        public static final String yyyyMMdd_POINT      = "yyyy.MM.dd";
        public static final String yyyyMMdd_HYPHEN     = "yyyy-MM-dd";
        public static final String yyyyMMdd_UNDERSCORE = "yyyy_MM_dd";

        public static final String yyyyMMddHHmm            = "yyyyMMddHHmm";
        public static final String yyyyMMddHHmm_POINT      = "yyyy.MM.dd HH:mm";
        public static final String yyyyMMddHHmm_HYPHEN     = "yyyy-MM-dd HH:mm";
        public static final String yyyyMMddHHmm_UNDERSCORE = "yyyy_MM_dd HH:mm";

        public static final String yyyyMMddHHmmss            = "yyyyMMddHHmmss";
        public static final String yyyyMMddHHmmss_POINT      = "yyyy.MM.dd HH:mm:ss";
        public static final String yyyyMMddHHmmss_HYPHEN     = "yyyy-MM-dd HH:mm:ss";
        public static final String yyyyMMddHHmmss_UNDERSCORE = "yyyy_MM_dd HH:mm:ss";

        public static final String MMdd            = "MMdd";
        public static final String MMdd_POINT      = "MM.dd";
        public static final String MMdd_HYPHEN     = "MM-dd";
        public static final String MMdd_UNDERSCORE = "MM_dd";

        public static final String HHmm            = "HHmm";
        public static final String HHmm_COLON      = "HH:mm";
        public static final String HHmm_POINT      = "HH.mm";
        public static final String HHmm_HYPHEN     = "HH-mm";
        public static final String HHmm_UNDERSCORE = "HH_mm";

        public static final String HHmmss            = "HHmmss";
        public static final String HHmmss_COLON      = "HH:mm:ss";
        public static final String HHmmss_POINT      = "HH.mm.ss";
        public static final String HHmmss_HYPHEN     = "HH-mm-ss";
        public static final String HHmmss_UNDERSCORE = "HH_mm_ss";

        public static final String SPECIAL_mmddHHmmyyyyss = "MMddHHmmyyyy.ss";

        // =============
        // = 中文时间格式 =
        // =============

        public static final String ZH_yy   = "yy年";
        public static final String ZH_yyyy = "yyyy年";
        public static final String ZH_MM   = "MM月";
        public static final String ZH_dd   = "dd日";
        public static final String ZH_HH   = "HH时";
        public static final String ZH_mm   = "mm分";
        public static final String ZH_ss   = "ss秒";

        public static final String ZH_yyMMdd         = "yy年MM月dd日";
        public static final String ZH_yyyyMMdd       = "yyyy年MM月dd日";
        public static final String ZH_yyyyMMddHHmm   = "yyyy年MM月dd日HH时mm分";
        public static final String ZH_yyyyMMddHHmmss = "yyyy年MM月dd日HH时mm分ss秒";
        public static final String ZH_MMdd           = "MM月dd日";
        public static final String ZH_HHmm           = "HH时mm分";
        public static final String ZH_HHmmss         = "HH时mm分ss秒";
    }

    /**
     * detail: 时间格式字符串常量
     * @author Ttt
     * <pre>
     *     存储历史时间格式常量, 方便更新库过渡使用
     *     并标记已弃用, 提醒使用 {@link TIME} 类
     *     随时删除请及时替换
     * </pre>
     */
    @Deprecated
    public static final class TIME_DEPRECATED {

        // 一分钟 60 秒
        public static final int  MINUTE_S = TIME.MINUTE_S;
        // 一小时 60 * 60 秒
        public static final int  HOUR_S   = TIME.HOUR_S;
        // 一天 24 * 60 * 60 秒
        public static final int  DAY_S    = TIME.DAY_S;
        // 秒与毫秒的倍数
        public static final long SECOND   = TIME.SECOND_MS;
        // 分与毫秒的倍数
        public static final long MINUTE   = TIME.MINUTE_MS;
        // 时与毫秒的倍数
        public static final long HOUR     = TIME.HOUR_MS;
        // 天与毫秒的倍数
        public static final long DAY      = TIME.DAY_MS;
        // 周与毫秒的倍数
        public static final long WEEK     = TIME.WEEK_MS;
        // 月与毫秒的倍数
        public static final long MONTH    = TIME.MONTH_MS;
        // 年与毫秒的倍数
        public static final long YEAR     = TIME.YEAR_MS;

        public static final String yyyy      = TIME.yyyy;
        public static final String yyMMdd    = TIME.yyMMdd_HYPHEN;
        public static final String yyMMdd2   = TIME.yyMMdd;
        public static final String yyyyMMdd  = TIME.yyyyMMdd_HYPHEN;
        public static final String yyyyMMdd2 = TIME.yyyyMMdd;
        public static final String yyyyMMdd3 = TIME.ZH_yyyyMMdd;
        public static final String yyyyMMdd4 = TIME.yyyyMMdd_UNDERSCORE;
        public static final String yyyyMMdd5 = TIME.yyyyMMdd_POINT;

        public static final String yyyyMMddHHmm  = TIME.yyyyMMddHHmm_HYPHEN;
        public static final String yyyyMMddHHmm2 = "yyyy年M月d日 HH:mm";
        public static final String yyyyMMddHHmm3 = TIME.yyyyMMddHHmm_POINT;

        public static final String yyyyMMddHHmmss  = TIME.yyyyMMddHHmmss_HYPHEN;
        public static final String yyyyMMddHHmmss2 = "yyyy年M月d日 HH:mm:ss";
        public static final String yyyyMMddHHmmss3 = "yyyyMMdd_HHmmss";
        public static final String yyyyMMddHHmmss4 = "yyyyMMdd.HHmmss";

        public static final String MMdd           = TIME.MMdd_HYPHEN;
        public static final String MMdd2          = TIME.ZH_MMdd;
        public static final String MMdd3          = TIME.MMdd;
        public static final String yy             = TIME.yy;
        public static final String MM             = TIME.MM;
        public static final String dd             = TIME.dd;
        public static final String hh             = "hh";
        public static final String HH             = TIME.HH;
        public static final String mm             = TIME.mm;
        public static final String HHmm           = TIME.HHmm_COLON;
        public static final String HHmm2          = TIME.HHmm;
        public static final String HHmmss         = TIME.HHmmss_COLON;
        public static final String HHmmss2        = TIME.HHmmss;
        public static final String hhmmMMDDyyyy   = "hh:mm M月d日 yyyy";
        public static final String hhmmssMMDDyyyy = "hh:mm:ss M月d日 yyyy";
        public static final String mmddHHmmyyyyss = TIME.SPECIAL_mmddHHmmyyyyss;
    }

    /**
     * detail: String 类型常量
     * @author Ttt
     */
    public static final class STR {

        // =======
        // = 通用 =
        // =======

        public static final String DEFAULT = "default";
        public static final String NONE    = "none";
        public static final String OBJECT  = "object";
        public static final String UNKNOWN = "unknown";

        public static final String BUG       = "bug";
        public static final String CHANNEL   = "channel";
        public static final String CHARSET   = "charset";
        public static final String CMD       = "cmd";
        public static final String CODE      = "code";
        public static final String COMPONENT = "component";
        public static final String CORE      = "core";
        public static final String ENGINE    = "engine";
        public static final String FETCH     = "fetch";
        public static final String FLAG      = "flag";
        public static final String FROM      = "from";
        public static final String GROUP     = "group";
        public static final String HASH      = "hash";
        public static final String LIB       = "lib";
        public static final String LIBS      = "libs";
        public static final String LIMIT     = "limit";
        public static final String MATCH     = "match";
        public static final String MODEL     = "model";
        public static final String MODULE    = "module";
        public static final String OBTAIN    = "obtain";
        public static final String OWNER     = "owner";
        public static final String PLUGIN    = "plugin";
        public static final String RESET     = "reset";
        public static final String ROUTER    = "router";
        public static final String SAFE      = "safe";
        public static final String SAFETY    = "safety";
        public static final String SHARE     = "share";
        public static final String STANDARD  = "standard";
        public static final String TARGET    = "target";
        public static final String TEMPLATE  = "template";
        public static final String TO        = "to";

        public static final String DECRYPT = "decrypt";
        public static final String ENCRYPT = "encrypt";
        public static final String PREFIX  = "prefix";
        public static final String SUFFIX  = "suffix";

        public static final String BASE = "base";
        public static final String BEAN = "bean";
        public static final String VO   = "vo";

        public static final String HIGH       = "high";
        public static final String LOW        = "low";
        public static final String MAX        = "max";
        public static final String MAX_LENGTH = "max_length";
        public static final String MAX_SIZE   = "max_size";
        public static final String MIN        = "min";
        public static final String MIN_LENGTH = "min_length";
        public static final String MIN_SIZE   = "min_size";

        public static final String EVENT    = "event";
        public static final String LINK     = "link";
        public static final String LISTENER = "listener";
        public static final String LOG      = "log";
        public static final String MESSAGE  = "message";
        public static final String REPORT   = "report";
        public static final String TRACK    = "track";

        public static final String DATABASE = "database";
        public static final String DB       = "db";

        public static final String BLANK    = "blank";
        public static final String GLOBAL   = "global";
        public static final String HOME     = "home";
        public static final String MAIN     = "main";
        public static final String PRIVACY  = "privacy";
        public static final String PROFILES = "profiles";
        public static final String SETTING  = "setting";
        public static final String SETTINGS = "settings";

        // =======
        // = 其他 =
        // =======

        public static final String ATTACH    = "attach";
        public static final String AUTO      = "auto";
        public static final String BALANCER  = "balancer";
        public static final String BANK      = "bank";
        public static final String BANNER    = "banner";
        public static final String BLOCK     = "block";
        public static final String BUCKET    = "bucket";
        public static final String CONTENT   = "content";
        public static final String EDIT      = "edit";
        public static final String ELASTIC   = "elastic";
        public static final String FACTORY   = "factory";
        public static final String GOTO      = "goto";
        public static final String IMPL      = "impl";
        public static final String INDENT    = "indent";
        public static final String INVENTORY = "inventory";
        public static final String KIND      = "kind";
        public static final String LEVEL     = "level";
        public static final String LOADER    = "loader";
        public static final String MENU      = "menu";
        public static final String MORE      = "more";
        public static final String NUMBER    = "number";
        public static final String OF        = "of";
        public static final String ONLY      = "only";
        public static final String OPERATE   = "operate";
        public static final String OPTIONS   = "options";
        public static final String ORIGINAL  = "original";
        public static final String OTHER     = "other";
        public static final String QUICK     = "quick";
        public static final String RANGE     = "range";
        public static final String REMARK    = "remark";
        public static final String SANDBOX   = "sandbox";
        public static final String SCORE     = "score";
        public static final String SKIP      = "skip";
        public static final String SMS       = "sms";
        public static final String TIMING    = "timing";
        public static final String TITLE     = "title";
        public static final String TRANSFER  = "transfer";
        public static final String WITH      = "with";

        // ==========
        // = 信息相关 =
        // ==========

        public static final String ACCOUNT   = "account";
        public static final String ADDRESS   = "address";
        public static final String AREA      = "area";
        public static final String CITY      = "city";
        public static final String EMAIL     = "email";
        public static final String INFO      = "info";
        public static final String LATITUDE  = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String MOBILE    = "mobile";
        public static final String NAME      = "name";
        public static final String PASSWORD  = "password";
        public static final String PHONE     = "phone";
        public static final String PROVINCE  = "province";
        public static final String REGION    = "region";
        public static final String SPEC      = "spec";
        public static final String USER      = "user";
        public static final String USER_ID   = "user_id";

        public static final String ACCESS   = "access";
        public static final String ID       = "id";
        public static final String IDENTITY = "identity";
        public static final String TOKEN    = "token";
        public static final String UNIQUE   = "unique";
        public static final String UUID     = "uuid";

        // =======
        // = 媒体 =
        // =======

        public static final String AUDIO      = "audio";
        public static final String IMAGE      = "image";
        public static final String IMAGES     = "images";
        public static final String MEDIA      = "media";
        public static final String MEDIA_TYPE = "media_type";
        public static final String TEXT       = "text";
        public static final String THUMBNAIL  = "thumbnail";
        public static final String VIDEO      = "video";
        public static final String WATERMARK  = "watermark";

        public static final String AAC           = "aac";
        public static final String AVI           = "avi";
        public static final String AVIF          = "avif";
        public static final String BMP           = "bmp";
        public static final String GIF           = "gif";
        public static final String HEIF          = "heif";
        public static final String ICON          = "icon";
        public static final String JPEG          = "jpeg";
        public static final String JPG           = "jpg";
        public static final String JSON          = "json";
        public static final String MP3           = "mp3";
        public static final String MP4           = "mp4";
        public static final String PNG           = "png";
        public static final String TXT           = "txt";
        public static final String WEBP          = "webp";
        public static final String WEBP_LOSSLESS = "webp_lossless";
        public static final String WEBP_LOSSY    = "webp_lossy";
        public static final String XML           = "xml";

        public static final String ALBUM      = "album";
        public static final String BEGIN_TIME = "begin_time";
        public static final String COMPRESS   = "compress";
        public static final String COVER      = "cover";
        public static final String CROP       = "crop";
        public static final String DURATION   = "duration";
        public static final String END_TIME   = "end_time";
        public static final String EXIF       = "exif";
        public static final String EXIF_TAG   = "exif_tag";
        public static final String MIME_TYPE  = "mimetype";
        public static final String PLAY_TIME  = "play_time";
        public static final String PREVIEW    = "preview";
        public static final String QUALITY    = "quality";
        public static final String RECORD     = "record";
        public static final String TIME       = "time";
        public static final String TIMESTAMP  = "timestamp";
        public static final String VALID_TIME = "valid_time";

        // ==========
        // = 时间相关 =
        // ==========

        public static final String CALENDAR = "calendar";

        public static final String DAY          = "day";
        public static final String HOUR         = "hour";
        public static final String MILLI_SECOND = "milli_second";
        public static final String MINUTE       = "minute";
        public static final String MONTH        = "month";
        public static final String SECOND       = "second";
        public static final String WEEK         = "week";
        public static final String YEAR         = "year";

        // ===============
        // = 状态、操作相关 =
        // ===============

        public static final String BIND      = "bind";
        public static final String UN_BINDER = "un_binder";

        public static final String ACCEPT       = "accept";
        public static final String ACTIVATED    = "activated";
        public static final String ACTIVE       = "active";
        public static final String AFTER        = "after";
        public static final String ALLOW        = "allow";
        public static final String ASYNC        = "async";
        public static final String BEFORE       = "before";
        public static final String CANCEL       = "cancel";
        public static final String CHECKABLE    = "checkable";
        public static final String CLOSE        = "close";
        public static final String COMPLETE     = "complete";
        public static final String CONFIRM      = "confirm";
        public static final String CONNECT      = "connect";
        public static final String CONNECTED    = "connected";
        public static final String CONNECTING   = "connecting";
        public static final String CREATE       = "create";
        public static final String DELAY        = "delay";
        public static final String DELETE       = "delete";
        public static final String DELIMITER    = "delimiter";
        public static final String DENIED       = "denied";
        public static final String DESTROY      = "destroy";
        public static final String DISABLED     = "disabled";
        public static final String DISABLING    = "disabling";
        public static final String DISCONNECT   = "disconnect";
        public static final String DISCONNECTED = "disconnected";
        public static final String DISK         = "disk";
        public static final String DOWNLOAD     = "download";
        public static final String DOWNLOADS    = "downloads";
        public static final String ENABLED      = "enabled";
        public static final String ENABLING     = "enabling";
        public static final String END          = "end";
        public static final String EXECUTE      = "execute";
        public static final String FAIL         = "fail";
        public static final String FINISH       = "finish";
        public static final String FOUND        = "found";
        public static final String GRANTED      = "granted";
        public static final String ING          = "ing";
        public static final String INIT         = "init";
        public static final String INSERT       = "insert";
        public static final String INVALID      = "invalid";
        public static final String LAUNCH       = "launch";
        public static final String LOAD         = "load";
        public static final String LOADING      = "loading";
        public static final String MARKER       = "marker";
        public static final String METADATA     = "metadata";
        public static final String NEED         = "need";
        public static final String NEXT         = "next";
        public static final String NORMAL       = "normal";
        public static final String NOT_FOUND    = "not_found";
        public static final String NOW          = "now";
        public static final String OPEN         = "open";
        public static final String OVERWRITE    = "overwrite";
        public static final String PAUSE        = "pause";
        public static final String PERIOD       = "period";
        public static final String PLAY         = "play";
        public static final String POOL         = "pool";
        public static final String RECYCLE      = "recycle";
        public static final String REFRESH      = "refresh";
        public static final String REQUEST      = "request";
        public static final String REQUIRE      = "require";
        public static final String RESPONSE     = "response";
        public static final String RESTART      = "restart";
        public static final String RESULT       = "result";
        public static final String RESUME       = "resume";
        public static final String SCOPE        = "scope";
        public static final String SHUTDOWN     = "shutdown";
        public static final String SLEEP        = "sleep";
        public static final String START        = "start";
        public static final String STATE        = "state";
        public static final String STOP         = "stop";
        public static final String SUBMIT       = "submit";
        public static final String SUCCESS      = "success";
        public static final String SUSPEND      = "suspend";
        public static final String SUSPENDED    = "suspended";
        public static final String SYNC         = "sync";
        public static final String TERMINATED   = "terminated";
        public static final String TRUNCATED    = "truncated";
        public static final String UNCONNECT    = "unconnect";
        public static final String UPLOAD       = "upload";
        public static final String VALID        = "valid";
        public static final String WAITING      = "waiting";

        // ==========
        // = 平台相关 =
        // ==========

        public static final String ANDROID      = "android";
        public static final String H5           = "h5";
        public static final String IOS          = "ios";
        public static final String MIN_IPROGRAM = "min_iprogram";
        public static final String PLATFORM     = "platform";
        public static final String WEB          = "web";

        // ===============
        // = UI、APP 相关 =
        // ===============

        public static final String BOTTOM = "bottom";
        public static final String LEFT   = "left";
        public static final String RIGHT  = "right";
        public static final String TOP    = "top";

        public static final String BORDER_WIDTH  = "border_width";
        public static final String DASH_WIDTH    = "dash_width";
        public static final String HEIGHT        = "height";
        public static final String MAX_WIDTH     = "max_width";
        public static final String MIN_HEIGHT    = "min_height";
        public static final String SCALE_HEIGHT  = "scale_height";
        public static final String SCALE_WIDTH   = "scale_width";
        public static final String SCREEN_HEIGHT = "screen_height";
        public static final String SCREEN_WIDTH  = "screen_width";
        public static final String STROKE_WIDTH  = "stroke_width";
        public static final String WIDTH         = "width";
        public static final String WIDTH_HEIGHT  = "width_height";

        public static final String ANIMATION   = "animation";
        public static final String BACKGROUND  = "background";
        public static final String BOLD        = "bold";
        public static final String CENTER      = "center";
        public static final String CHECK       = "check";
        public static final String CHECKBOX    = "checkbox";
        public static final String CHECKED     = "checked";
        public static final String CHOOSE      = "choose";
        public static final String CHOOSE_MODE = "choose_mode";
        public static final String COLOR       = "color";
        public static final String DOWN        = "down";
        public static final String FOCUSED     = "focused";
        public static final String HORIZONTAL  = "horizontal";
        public static final String INFLATER    = "inflater";
        public static final String LAYOUT      = "layout";
        public static final String MEASURE     = "measure";
        public static final String MOVE        = "move";
        public static final String ORIENTATION = "orientation";
        public static final String OUTSIDE     = "outside";
        public static final String PRESS       = "press";
        public static final String PRESSED     = "pressed";
        public static final String PROGRESS    = "progress";
        public static final String SCALE       = "scale";
        public static final String SCREEN      = "screen";
        public static final String SCROLL      = "scroll";
        public static final String SCROLLING   = "scrolling";
        public static final String SELECT      = "select";
        public static final String SELECTED    = "selected";
        public static final String SLIDE       = "slide";
        public static final String SLIDING     = "sliding";
        public static final String UP          = "up";
        public static final String VERTICAL    = "vertical";
        public static final String WEIGHT      = "weight";
        public static final String WIDGET      = "widget";
        public static final String X           = "x";
        public static final String Y           = "y";

        public static final String GRADIENT = "gradient";
        public static final String SHAPE    = "shape";
        public static final String SOLID    = "solid";
        public static final String STROKE   = "stroke";

        public static final String DEBUG        = "debug";
        public static final String RELEASE      = "release";
        public static final String UPGRADE      = "upgrade";
        public static final String VERSION      = "version";
        public static final String VERSION_CODE = "version_code";
        public static final String VERSION_NAME = "version_name";

        public static final String ACCESSIBILITY   = "accessibility";
        public static final String ACTION          = "action";
        public static final String ACTIVITY        = "activity";
        public static final String ADAPTER         = "adapter";
        public static final String APPLICATION     = "application";
        public static final String BROADCAST       = "broadcast";
        public static final String BUNDLE          = "bundle";
        public static final String CANVAS          = "canvas";
        public static final String CATEGORY        = "category";
        public static final String COMPOSE         = "compose";
        public static final String CORNER          = "corner";
        public static final String CURSOR          = "cursor";
        public static final String DASH            = "dash";
        public static final String DIALOG          = "dialog";
        public static final String DIRECTION       = "direction";
        public static final String DISCRETE        = "discrete";
        public static final String DISTANCE        = "distance";
        public static final String DRAW            = "draw";
        public static final String EFFECT          = "effect";
        public static final String EXTRA           = "extra";
        public static final String EXTRAS          = "extras";
        public static final String FILTER          = "filter";
        public static final String FONT            = "font";
        public static final String FONT_FAMILY     = "font_family";
        public static final String FONT_STYLE      = "font_style";
        public static final String FRAGMENT        = "fragment";
        public static final String GALLERY         = "gallery";
        public static final String GRAPHICS        = "graphics";
        public static final String GRID            = "grid";
        public static final String HANDLER         = "handler";
        public static final String HOLDER          = "holder";
        public static final String INSETS          = "insets";
        public static final String INTENT          = "intent";
        public static final String INTERPOLATOR    = "interpolator";
        public static final String INVALIDATE      = "invalidate";
        public static final String ITEM_COUNT      = "item_count";
        public static final String ITEM_DECORATION = "item_decoration";
        public static final String LAUNCHER        = "launcher";
        public static final String MATRIX          = "matrix";
        public static final String NOTIFY          = "notify";
        public static final String OFFSETS         = "offsets";
        public static final String OUTLINE         = "outline";
        public static final String PAINT           = "paint";
        public static final String PATH_EFFECT     = "path_effect";
        public static final String POINT           = "point";
        public static final String POINTF          = "pointf";
        public static final String POST_INVALIDATE = "post_invalidate";
        public static final String RECEIVE         = "receive";
        public static final String RECT            = "rect";
        public static final String RECTF           = "rectf";
        public static final String RENDER          = "render";
        public static final String SERVICE         = "service";
        public static final String SHADER          = "shader";
        public static final String SPACE           = "space";
        public static final String SPAN            = "span";
        public static final String SPAN_COUNT      = "span_count";
        public static final String TOAST           = "toast";
        public static final String TYPEFACE        = "typeface";
        public static final String VIBRATE         = "vibrate";
        public static final String VIEW            = "view";

        public static final String BINDING    = "binding";
        public static final String LIFECYCLE  = "lifecycle";
        public static final String LIVE_DATA  = "live_data";
        public static final String PERMISSION = "permission";
        public static final String VIEW_MODEL = "view_model";

        public static final String IMAGE_VIEW   = "image_view";
        public static final String RECYCLE_VIEW = "recycle_view";
        public static final String SCROLL_VIEW  = "scroll_view";
        public static final String TEXT_VIEW    = "text_view";
        public static final String VIEW_GROUP   = "view_group";
        public static final String VIEW_PAGER   = "view_pager";

        public static final String CHILD       = "child";
        public static final String COMPILE     = "compile";
        public static final String DEVICE      = "device";
        public static final String ELEMENT     = "element";
        public static final String ENVIRONMENT = "environment";
        public static final String MEMORY      = "memory";
        public static final String PACKAGE     = "package";
        public static final String PACKNAME    = "packname";

        public static final String ASSETS    = "assets";
        public static final String ASSIST    = "assist";
        public static final String CAMERA    = "camera";
        public static final String CAPTURE   = "capture";
        public static final String RAW       = "raw";
        public static final String RES       = "res";
        public static final String RICH_TEXT = "rich_text";
        public static final String SOURCE    = "source";
        public static final String STYLE     = "style";
        public static final String TRANSFORM = "transform";

        public static final String BODY       = "body";
        public static final String BROWSER    = "browser";
        public static final String CACHE      = "cache";
        public static final String CLIENT     = "client";
        public static final String CONFIG     = "config";
        public static final String COOKIE     = "cookie";
        public static final String COPY       = "copy";
        public static final String DNS        = "dns";
        public static final String DOMAIN     = "domain";
        public static final String EBS        = "ebs";
        public static final String FOOTER     = "footer";
        public static final String FOREGROUND = "foreground";
        public static final String HEAD       = "head";
        public static final String HEADER     = "header";
        public static final String HTTP       = "http";
        public static final String HTTPS      = "https";
        public static final String LANGUAGE   = "language";
        public static final String OSS        = "oss";
        public static final String PASTE      = "paste";
        public static final String PATCH      = "patch";
        public static final String POST       = "post";
        public static final String SESSION    = "session";
        public static final String SLB        = "slb";
        public static final String SSL        = "ssl";
        public static final String TIMEOUT    = "timeout";
        public static final String TRACE      = "trace";
        public static final String UNLINK     = "unlink";
        public static final String URI        = "uri";
        public static final String URL        = "url";
        public static final String WRAPPED    = "wrapped";
        public static final String WWW        = "www";

        // ==========
        // = 数据相关 =
        // ==========

        public static final String FALSE = "false";
        public static final String TRUE  = "true";

        public static final String ARRAY   = "array";
        public static final String BOOLEAN = "boolean";
        public static final String BYTE    = "byte";
        public static final String CHAR    = "char";
        public static final String DATE    = "date";
        public static final String DOUBLE  = "double";
        public static final String FLOAT   = "float";
        public static final String INT     = "int";
        public static final String INTEGER = "integer";
        public static final String LIST    = "list";
        public static final String LONG    = "long";
        public static final String MAP     = "map";
        public static final String STRING  = "string";

        public static final String BINARY = "binary";
        public static final String DEC    = "dec";
        public static final String DECODE = "decode";
        public static final String ENCODE = "encode";
        public static final String HEX    = "hex";
        public static final String OCT    = "oct";

        public static final String AES        = "aes";
        public static final String BASE64     = "base64";
        public static final String CRC32      = "crc32";
        public static final String DES        = "des";
        public static final String DES3       = "des3";
        public static final String ESCAPE     = "escape";
        public static final String HMACMD5    = "hmacmd5";
        public static final String HMACSHA1   = "hmacsha1";
        public static final String HMACSHA224 = "hmacsha224";
        public static final String HMACSHA256 = "hmacsha256";
        public static final String HMACSHA384 = "hmacsha384";
        public static final String HMACSHA512 = "hmacsha512";
        public static final String MD2        = "md2";
        public static final String MD5        = "md5";
        public static final String RSA        = "rsa";
        public static final String SHA1       = "sha1";
        public static final String SHA224     = "sha224";
        public static final String SHA256     = "sha256";
        public static final String SHA384     = "sha384";
        public static final String SHA512     = "sha512";
        public static final String TRIPLEDES  = "tripledes";
        public static final String UNESCAPE   = "unescape";
        public static final String XOR        = "xor";

        // ========
        // = 关键字 =
        // ========

        public static final String CATCH     = "catch";
        public static final String CRASH     = "crash";
        public static final String ERROR     = "error";
        public static final String EXCEPTION = "exception";
        public static final String EXIT      = "exit";
        public static final String THROWABLE = "throwable";
        public static final String TRY       = "try";

        public static final String ADD      = "add";
        public static final String APPEND   = "append";
        public static final String ARGS     = "args";
        public static final String COUNT    = "count";
        public static final String CURRENT  = "current";
        public static final String CYCLE    = "cycle";
        public static final String DIFF     = "diff";
        public static final String FIND     = "find";
        public static final String FIRST    = "first";
        public static final String GET      = "get";
        public static final String INDEX    = "index";
        public static final String ITEM     = "item";
        public static final String LAST     = "last";
        public static final String LOOP     = "loop";
        public static final String MIDDLE   = "middle";
        public static final String PAGE     = "page";
        public static final String PAGER    = "pager";
        public static final String POSITION = "position";
        public static final String PUT      = "put";
        public static final String QUERY    = "query";
        public static final String REMOVE   = "remove";
        public static final String SET      = "set";
        public static final String SINGLE   = "single";
        public static final String SIZE     = "size";
        public static final String SORT     = "sort";
        public static final String SUB      = "sub";
        public static final String TAB      = "tab";
        public static final String TAG      = "tag";
        public static final String TAKE     = "take";
        public static final String UPDATE   = "update";

        public static final String AGENT     = "agent";
        public static final String ALIAS     = "alias";
        public static final String CUSTOM    = "custom";
        public static final String DATA      = "data";
        public static final String DIR       = "dir";
        public static final String DIRECTORY = "directory";
        public static final String FILE      = "file";
        public static final String FOLD      = "fold";
        public static final String IGNORE    = "ignore";
        public static final String INPUT     = "input";
        public static final String KEY       = "key";
        public static final String KEYWORD   = "keyword";
        public static final String MISSING   = "missing";
        public static final String OUTPUT    = "output";
        public static final String PATH      = "path";
        public static final String PRINT     = "print";
        public static final String READER    = "reader";
        public static final String TASK      = "task";
        public static final String TEMP      = "temp";
        public static final String TYPE      = "type";
        public static final String VALUE     = "value";
        public static final String WRAPPER   = "wrapper";
        public static final String WRITER    = "writer";

        public static final String CONTROL  = "control";
        public static final String CONVERT  = "convert";
        public static final String INSTANCE = "instance";
        public static final String MARGIN   = "margin";
        public static final String PADDING  = "padding";
        public static final String PARENT   = "parent";
        public static final String PARSER   = "parser";

        public static final String MARGIN_BOTTOM  = "margin_bottom";
        public static final String MARGIN_LEFT    = "margin_left";
        public static final String MARGIN_RIGHT   = "margin_right";
        public static final String MARGIN_TOP     = "margin_top";
        public static final String PADDING_BOTTOM = "padding_bottom";
        public static final String PADDING_LEFT   = "padding_left";
        public static final String PADDING_RIGHT  = "padding_right";
        public static final String PADDING_TOP    = "padding_top";

        public static final String ATTRIBUTE = "attribute";
        public static final String BUFFER    = "buffer";
        public static final String BUILD     = "build";
        public static final String BUILDER   = "builder";
        public static final String CLASS     = "class";
        public static final String CLONE     = "clone";
        public static final String CONST     = "const";
        public static final String ENUM      = "enum";
        public static final String FIELD     = "field";
        public static final String FINAL     = "final";
        public static final String FOR       = "for";
        public static final String FUNCTION  = "function";
        public static final String INNER     = "inner";
        public static final String INTERFACE = "interface";
        public static final String INTERNAL  = "internal";
        public static final String INVOKE    = "invoke";
        public static final String JOB       = "job";
        public static final String METHOD    = "method";
        public static final String NEW       = "new";
        public static final String NULL      = "null";
        public static final String PARAM     = "param";
        public static final String PARAMS    = "params";
        public static final String PRIVATE   = "private";
        public static final String PROTECTED = "protected";
        public static final String PUBLIC    = "public";
        public static final String RETURN    = "return";
        public static final String RUNNABLE  = "runnable";
        public static final String SCHEDULE  = "schedule";
        public static final String STATIC    = "static";
        public static final String STREAM    = "stream";
        public static final String THREAD    = "thread";
        public static final String VAL       = "val";
        public static final String VAR       = "var";
        public static final String VOID      = "void";
        public static final String WHILE     = "while";
    }

    /**
     * detail: Int 类型常量
     * @author Ttt
     */
    public static final class INT {

        // ============
        // = 常用操作值 =
        // ============

        public static final int BASE         = 102030;
        // 默认状态 ( 暂未进行操作 )
        public static final int NORMAL       = BASE + 1;
        // 操作中
        public static final int ING          = BASE + 2;
        // 操作成功
        public static final int SUCCESS      = BASE + 3;
        // 操作失败
        public static final int FAIL         = BASE + 4;
        // 操作异常
        public static final int ERROR        = BASE + 5;
        // 开始操作
        public static final int START        = BASE + 6;
        // 重新开始操作
        public static final int RESTART      = BASE + 7;
        // 操作结束
        public static final int END          = BASE + 8;
        // 操作暂停
        public static final int PAUSE        = BASE + 9;
        // 操作恢复 ( 继续 )
        public static final int RESUME       = BASE + 10;
        // 操作停止
        public static final int STOP         = BASE + 11;
        // 操作取消
        public static final int CANCEL       = BASE + 12;
        // 创建
        public static final int CREATE       = BASE + 13;
        // 销毁
        public static final int DESTROY      = BASE + 14;
        // 回收
        public static final int RECYCLE      = BASE + 15;
        // 初始化
        public static final int INIT         = BASE + 16;
        // 已打开
        public static final int ENABLED      = BASE + 17;
        // 正在打开
        public static final int ENABLING     = BASE + 18;
        // 已关闭
        public static final int DISABLED     = BASE + 19;
        // 正在关闭
        public static final int DISABLING    = BASE + 20;
        // 连接成功
        public static final int CONNECTED    = BASE + 21;
        // 连接中
        public static final int CONNECTING   = BASE + 22;
        // 连接失败、断开
        public static final int DISCONNECTED = BASE + 23;
        // 暂停、延迟
        public static final int SUSPENDED    = BASE + 24;
        // 未知
        public static final int UNKNOWN      = BASE + 25;
        // 新增
        public static final int INSERT       = BASE + 26;
        // 删除
        public static final int DELETE       = BASE + 27;
        // 更新
        public static final int UPDATE       = BASE + 28;
        // 查询
        public static final int SELECT       = BASE + 29;
        // 加密
        public static final int ENCRYPT      = BASE + 30;
        // 解密
        public static final int DECRYPT      = BASE + 31;
        // 重置
        public static final int RESET        = BASE + 32;
        // 关闭
        public static final int CLOSE        = BASE + 33;
        // 打开
        public static final int OPEN         = BASE + 34;
        // 退出
        public static final int EXIT         = BASE + 35;
        // 下一步
        public static final int NEXT         = BASE + 36;
        // 无任何
        public static final int NONE         = BASE + 37;
        // 结束
        public static final int FINISH       = BASE + 38;
        // 等待中
        public static final int WAITING      = BASE + 39;
        // 完成
        public static final int COMPLETE     = BASE + 40;

        // ===========
        // = Request =
        // ===========

        // 默认状态 ( 暂未进行操作 )
        public static final int REQUEST_NORMAL  = NORMAL;
        // 请求中
        public static final int REQUEST_ING     = ING;
        // 请求成功
        public static final int REQUEST_SUCCESS = SUCCESS;
        // 请求失败
        public static final int REQUEST_FAIL    = FAIL;
        // 请求异常
        public static final int REQUEST_ERROR   = ERROR;
        // 请求开始
        public static final int REQUEST_START   = START;
        // 重新请求
        public static final int REQUEST_RESTART = RESTART;
        // 请求结束
        public static final int REQUEST_END     = END;
        // 请求暂停
        public static final int REQUEST_PAUSE   = PAUSE;
        // 请求恢复 ( 继续 )
        public static final int REQUEST_RESUME  = RESUME;
        // 请求停止
        public static final int REQUEST_STOP    = STOP;
        // 请求取消
        public static final int REQUEST_CANCEL  = CANCEL;
    }

    /**
     * detail: 格式化字符串常量
     * @author Ttt
     */
    public static final class FORMAT {

        public static final String S2             = "%s%s";
        public static final String S2_HYPHEN      = "%s-%s";
        public static final String S2_UNDERSCORE  = "%s_%s";
        public static final String S2_COMMA       = "%s,%s";
        public static final String S2_COMMA_SPACE = "%s, %s";
        public static final String S2_COMMA2      = "%s、%s";
        public static final String S2_SPACE       = "%s %s";
        public static final String S2_SPACE_SE    = " %s %s ";

        public static final String S3             = "%s%s%s";
        public static final String S3_HYPHEN      = "%s-%s-%s";
        public static final String S3_UNDERSCORE  = "%s_%s_%s";
        public static final String S3_COMMA       = "%s,%s,%s";
        public static final String S3_COMMA_SPACE = "%s, %s, %s";
        public static final String S3_COMMA2      = "%s、%s、%s";
        public static final String S3_SPACE       = "%s %s %s";
        public static final String S3_SPACE_SE    = " %s %s %s ";

        public static final String S4             = "%s%s%s%s";
        public static final String S4_HYPHEN      = "%s-%s-%s-%s";
        public static final String S4_UNDERSCORE  = "%s_%s_%s_%s";
        public static final String S4_COMMA       = "%s,%s,%s,%s";
        public static final String S4_COMMA_SPACE = "%s, %s, %s, %s";
        public static final String S4_COMMA2      = "%s、%s、%s、%s";
        public static final String S4_SPACE       = "%s %s %s %s";
        public static final String S4_SPACE_SE    = " %s %s %s %s ";

        public static final String BRACE       = "{ %s }";
        public static final String BRACE_SPACE = " { %s } ";

        public static final String BRACKET       = "[ %s ]";
        public static final String BRACKET_SPACE = " [ %s ] ";

        public static final String PARENTHESES       = "( %s )";
        public static final String PARENTHESES_SPACE = " ( %s ) ";
    }

    /**
     * detail: 正则表达式字符串常量
     * @author Ttt
     */
    public static final class REGEX {

        // 正则表达式: 空格
        public static final String SPACE = "\\s";

        // 正则表达式: 验证数字
        public static final String NUMBER = "^[0-9]*$";

        // 正则表达式: 验证数字或包含小数点
        public static final String NUMBER_OR_DECIMAL = "^[0-9]*[.]?[0-9]*$";

        // 正则表达式: 验证是否包含数字
        public static final String CONTAIN_NUMBER = ".*\\d+.*";

        // 正则表达式: 验证是否数字或者字母
        public static final String NUMBER_OR_LETTER = "^[A-Za-z0-9]+$";

        // 正则表达式: 验证是否全是字母
        public static final String LETTER = "^[A-Za-z]+$";

        // 正则表达式: 不能输入特殊字符 ^[\u4E00-\u9FA5A-Za-z0-9]+$ 或 ^[\u4E00-\u9FA5A-Za-z0-9]{2,20}$
        public static final String SPECIAL = "^[\\u4E00-\\u9FA5A-Za-z0-9]+$";

        // 正则表达式: 验证微信号
        public static final String WX = "^[a-zA-Z]{1}[-_a-zA-Z0-9]{5,19}+$";

        // 正则表达式: 验证真实姓名 ^[\u4e00-\u9fa5]+(·[\u4e00-\u9fa5]+)*$
        public static final String REALNAME = "^[\\u4e00-\\u9fa5]+(•[\\u4e00-\\u9fa5]*)*$|^[\\u4e00-\\u9fa5]+(·[\\u4e00-\\u9fa5]*)*$";

        // 正则表达式: 验证昵称
        public static final String NICKNAME = "^[\\u4E00-\\u9FA5A-Za-z0-9_]+$";

        // 正则表达式: 验证用户名 ( 不包含中文和特殊字符 ) 如果用户名使用手机号码或邮箱 则结合手机号验证和邮箱验证
        public static final String USERNAME = "^[a-zA-Z]\\w{5,17}$";

        // 正则表达式: 验证密码 ( 不包含特殊字符 )
        public static final String PASSWORD = "^[a-zA-Z0-9]{6,18}$";

        // 正则表达式: 验证邮箱
        public static final String EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

        // 正则表达式: 验证 URL
        public static final String URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?";

        // 正则表达式: 验证 IP 地址
        public static final String IP_ADDRESS = "(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})";

        // 正则表达式: 验证汉字
        public static final String CHINESE = "^[\u4e00-\u9fa5]+$";

        // 正则表达式: 验证汉字 ( 含双角符号 )
        public static final String CHINESE_ALL = "^[\u0391-\uFFE5]+$";

        // 正则表达式: 验证汉字 ( 含双角符号 )
        public static final String CHINESE_ALL2 = "[\u0391-\uFFE5]";
    }

    /**
     * detail: 数组常量
     * @author Ttt
     */
    public static final class ARRAY {

        // 用于建立十六进制字符的输出的小写字符数组
        private static final char[] HEX_DIGITS = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
        };

        // 用于建立十六进制字符的输出的大写字符数组
        private static final char[] HEX_DIGITS_UPPER = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };

        // 0123456789
        private static final char[] NUMBERS = {
                48, 49, 50, 51, 52, 53, 54, 55, 56, 57
        };

        // abcdefghijklmnopqrstuvwxyz
        private static final char[] LOWER_CASE_LETTERS = {
                97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109,
                110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122
        };

        // ABCDEFGHIJKLMNOPQRSTUVWXYZ
        private static final char[] CAPITAL_LETTERS = {
                65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80,
                81, 82, 83, 84, 85, 86, 87, 88, 89, 90
        };

        // abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ
        private static final char[] LETTERS = {
                97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110,
                111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 65, 66,
                67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83,
                84, 85, 86, 87, 88, 89, 90
        };

        // 0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ
        private static final char[] NUMBERS_AND_LETTERS = {
                48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102,
                103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115,
                116, 117, 118, 119, 120, 121, 122, 65, 66, 67, 68, 69, 70, 71, 72,
                73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90
        };

        // 生肖数组
        private static final String[] ZODIAC = {
                "猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊"
        };

        // 星座对应日期
        private static final String[] CONSTELLATION_DATE = {
                "01.20-02.18", "02.19-03.20", "03.21-04.19", "04.20-05.20", "05.21-06.21", "06.22-07.22",
                "07.23-08.22", "08.23-09.22", "09.23-10.23", "10.24-11.22", "11.23-12.21", "12.22-01.19"
        };

        // 星座数组
        private static final String[] CONSTELLATION = {
                "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座",
                "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座"
        };

        // ==========
        // = 对外公开 =
        // ==========

        // 用于建立十六进制字符的输出的小写字符数组
        public static char[] HEX_DIGITS() {
            return Arrays.copyOf(HEX_DIGITS, HEX_DIGITS.length);
        }

        // 用于建立十六进制字符的输出的大写字符数组
        public static char[] HEX_DIGITS_UPPER() {
            return Arrays.copyOf(HEX_DIGITS_UPPER, HEX_DIGITS_UPPER.length);
        }

        // 0123456789
        public static char[] NUMBERS() {
            return Arrays.copyOf(NUMBERS, NUMBERS.length);
        }

        // abcdefghijklmnopqrstuvwxyz
        public static char[] LOWER_CASE_LETTERS() {
            return Arrays.copyOf(LOWER_CASE_LETTERS, LOWER_CASE_LETTERS.length);
        }

        // ABCDEFGHIJKLMNOPQRSTUVWXYZ
        public static char[] CAPITAL_LETTERS() {
            return Arrays.copyOf(CAPITAL_LETTERS, CAPITAL_LETTERS.length);
        }

        // abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ
        public static char[] LETTERS() {
            return Arrays.copyOf(LETTERS, LETTERS.length);
        }

        // 0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ
        public static char[] NUMBERS_AND_LETTERS() {
            return Arrays.copyOf(NUMBERS_AND_LETTERS, NUMBERS_AND_LETTERS.length);
        }

        // 生肖数组
        public static String[] ZODIAC() {
            return Arrays.copyOf(ZODIAC, ZODIAC.length);
        }

        // 星座对应日期
        public static String[] CONSTELLATION_DATE() {
            return Arrays.copyOf(CONSTELLATION_DATE, CONSTELLATION_DATE.length);
        }

        // 星座数组
        public static String[] CONSTELLATION() {
            return Arrays.copyOf(CONSTELLATION, CONSTELLATION.length);
        }
    }

    /**
     * detail: 校验接受常量
     * @author Ttt
     */
    public static final class ACCEPT {

        public static final String NUMBERS = "0123456789";

        public static final String LOWER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";

        public static final String CAPITAL_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        public static final String LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        public static final String NUMBERS_AND_LETTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    }

    /**
     * detail: 默认值常量
     * @author Ttt
     */
    public static final class DEFAULT {

        public static final int        ERROR_INT         = -1;
        public static final long       ERROR_LONG        = -1L;
        public static final float      ERROR_FLOAT       = -1F;
        public static final double     ERROR_DOUBLE      = -1D;
        public static final boolean    ERROR_BOOLEAN     = false;
        public static final short      ERROR_SHORT       = -1;
        public static final char       ERROR_CHAR        = (char) -1;
        public static final byte       ERROR_BYTE        = (byte) -1;
        public static final BigDecimal ERROR_BIG_DECIMAL = BigDecimal.valueOf(-1L);
        public static final BigInteger ERROR_BIG_INTEGER = BigInteger.valueOf(-1L);
        public static final String     ERROR_STRING      = null;

        public static final int        INT         = 0;
        public static final long       LONG        = 0L;
        public static final float      FLOAT       = 0F;
        public static final double     DOUBLE      = 0D;
        public static final boolean    BOOLEAN     = false;
        public static final short      SHORT       = 0;
        public static final char       CHAR        = (char) 0;
        public static final byte       BYTE        = (byte) 0;
        public static final BigDecimal BIG_DECIMAL = BigDecimal.ZERO;
        public static final BigInteger BIG_INTEGER = BigInteger.ZERO;
        public static final String     STRING      = "";

        public static final Object ENTITY = null;
        public static final Object OBJECT = null;
    }
}