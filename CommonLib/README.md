# 框架公共组件层
#### 目录介绍
- 01.框架公共组件层
- 02.组件化建设
- 03.公共组件库依赖
- 04.Activity栈管理库
- 05.通用存储库
- 06.Log日志打印库
- 07.App重新启动库
- 08.通用工具类库
- 09.通用基类封装库
- 10.反射工具类库
- 11.Intent封装库
- 12.基础接口库
- 13.异常上报接口库


### 01.框架公共组件层
- 组件化开发中基础公共库，activity栈管理；Log日志；通用缓存库(支持sp，mmkv，lru，disk，fastsp等多种存储方式切换)；App重启；通用全面的工具类Utils；通用基类fragment，adpater，activity等简单封装；intent内容打印到控制台库


### 02.组件化建设


### 03.公共组件库依赖
- 关于依赖库如下所示，可以根据需求选择性使用：
    ``` java
    //base基类
    implementation 'com.github.yangchong211.YCCommonLib:BaseClassLib:1.3.0'
    //工具类utils
    implementation 'com.github.yangchong211.YCCommonLib:ToolUtilsLib:1.3.0'
    //activity栈管理
    implementation 'com.github.yangchong211.YCCommonLib:ActivityManager:1.3.0'
    //通用缓存存储库，支持sp，fastsp，mmkv，lruCache，DiskLruCache等
    implementation 'com.github.yangchong211.YCCommonLib:AppBaseStore:1.3.0'
    //通用日志输出库
    implementation 'com.github.yangchong211.YCCommonLib:AppLogLib:1.3.0'
    //app重启库
    implementation 'com.github.yangchong211.YCCommonLib:AppRestartLib:1.3.0'
    //intent内容输出到控制台
    implementation 'com.github.yangchong211.YCCommonLib:SafeIntentLib:1.3.0'
    ```


### 04.Activity栈管理库
- 非常好用的activity任务栈管理库，自动化注册。完全解耦合的activity栈管理，拿来即可用，或者栈顶Activity，移除添加，推出某个页面，获取应用注册Activity列表等，可以注册监听某个页面的生命周期，小巧好用。
    ``` java
    //退出应用程序
    ActivityManager.getInstance().appExist();
    //查找指定的Activity
    Activity commonActivity = ActivityManager.getInstance().get(CommonActivity.class);
    //判断界面Activity是否存在
    boolean exist = ActivityManager.getInstance().isExist(CommonActivity.class);
    //移除栈顶的activity
    ActivityManager.getInstance().pop();
    //结束所有Activity
    ActivityManager.getInstance().finishAll();
    //结束指定的Activity
    ActivityManager.getInstance().finish(CommonActivity.this);
    //判断activity任务栈是否为空
    ActivityManager.getInstance().isEmpty();
    //获取栈顶的Activity
    Activity activity = ActivityManager.getInstance().peek();
    //判断activity是否处于栈顶
    ActivityManager.getInstance().isActivityTop(this,"MainActivity");
    //添加 activity 入栈
    ActivityManager.getInstance().add(CommonActivity.this);
    //移除 activity 出栈
    ActivityManager.getInstance().remove(CommonActivity.this);
    //监听某个activity的生命周期，完全解耦合
    ActivityManager.getInstance().registerActivityLifecycleListener(CommonActivity.class, new ActivityLifecycleListener() {
        @Override
        public void onActivityCreated(@Nullable Activity activity, Bundle savedInstanceState) {
            super.onActivityCreated(activity, savedInstanceState);
        }
    
        @Override
        public void onActivityStarted(@Nullable Activity activity) {
            super.onActivityStarted(activity);
        }
    });
    //移除某个activity的生命周期，完全解耦合
    //ActivityManager.getInstance().registerActivityLifecycleListener(CommonActivity.this,listener);
    ```




### 05.通用存储库
- 通用存储库，支持二级缓存，LRU缓存，磁盘缓存(可以使用sp，mmkv，或者DiskLruCache)。不管你使用那种方式的存储，都是一套通用的api，使用几乎是零成本。
- 第一步：通用存储库初始化
    ``` java
    CacheConfig.Builder builder = CacheConfig.Companion.newBuilder();
    //设置是否是debug模式
    CacheConfig cacheConfig = builder.debuggable(BuildConfig.DEBUG)
            //设置外部存储根目录
            .extraLogDir(null)
            //设置lru缓存最大值
            .maxCacheSize(100)
            //内部存储根目录
            .logDir(null)
            //创建
            .build();
    CacheInitHelper.INSTANCE.init(MainApplication.getInstance(),cacheConfig);
    //最简单的初始化
    //CacheInitHelper.INSTANCE.init(CacheConfig.Companion.newBuilder().build());
    ```
- 第二步：存储数据和获取数据
    ```
    //存储数据
    dataCache.saveBoolean("cacheKey1",true);
    dataCache.saveFloat("cacheKey2",2.0f);
    dataCache.saveInt("cacheKey3",3);
    dataCache.saveLong("cacheKey4",4);
    dataCache.saveString("cacheKey5","doubi5");
    dataCache.saveDouble("cacheKey6",5.20);
    
    //获取数据
    boolean data1 = dataCache.readBoolean("cacheKey1", false);
    float data2 = dataCache.readFloat("cacheKey2", 0);
    int data3 = dataCache.readInt("cacheKey3", 0);
    long data4 = dataCache.readLong("cacheKey4", 0);
    String data5 = dataCache.readString("cacheKey5", "");
    double data6 = dataCache.readDouble("cacheKey5", 0.0);
    ```
- 第三步：一些存储说明
    - 关于设置磁盘缓存的路径，需要注意一些问题。建议使用该库默认的路径
    ``` java
    /**
     * log路径，通常这个缓存比较私有的内容
     * 比如sp，mmkv，存储的用户数据
     * 内部存储根目录，举个例子：
     * file:data/user/0/包名/files
     * cache:/data/user/0/包名/cache
     */
    val logDir: String?
    
    /**
     * 额外的log路径，通常缓存一些不私密的内存
     * 比如缓存图片，缓存视频，缓存下载文件，缓存日志等
     *
     * 外部存储根目录，举个例子
     * files:/storage/emulated/0/Android/data/包名/files
     * cache:/storage/emulated/0/Android/data/包名/cache
     */
    val extraLogDir: File?
    ```


### 06.Log日志打印库
- 通用日志库框架，专用LogCat工具，主要功能全局配置log输出, 个性化设置Tag，可以设置日志打印级别，支持打印复杂对象，可以实现自定义日志接口，支持简化版本将日志写入到文件中。小巧好用！
- 第一步：初始化操作
    ``` java
    String ycLogPath = AppFileUtils.getCacheFilePath(this, "ycLog");
    AppLogConfig config = new AppLogConfig.Builder()
            //设置日志tag总的标签
            .setLogTag("yc")
            //是否将log日志写到文件
            .isWriteFile(true)
            //是否是debug
            .enableDbgLog(true)
            //设置日志最小级别
            .minLogLevel(Log.VERBOSE)
            //设置输出日志到file文件的路径。前提是将log日志写入到文件设置成true
            .setFilePath(ycLogPath)
            .build();
    //配置
    AppLogFactory.init(config);
    ```
- 第二步：使用Log日志，十分简单，如下所示
    ``` java
    //自己带有tag标签
    AppLogHelper.d("MainActivity: ","debug log");
    //使用全局tag标签
    AppLogHelper.d("MainActivity log info no tag");
    //当然，如果不满足你的要求，开发者可以自己实现日志输出的形式。
    AppLogFactory.addPrinter(new AbsPrinter() {
        @NonNull
        @Override
        public String name() {
            return "yc";
        }
    
        @Override
        public void println(int level, String tag, String message, Throwable tr) {
            //todo 这块全局回调日志，你可以任意实现自定义操作
        }
    });
    ```



### 07.App重新启动库
- 使用场景说明
    - 比如App切换了debug或者release环境，需要进行app重新启动，可以使用该库，小巧好用。一行代码搞定，傻瓜式使用！
- 第一种方式，开启一个新的服务KillSelfService，用来重启本APP。
    ``` java
    RestartAppHelper.restartApp(this,RestartFactory.SERVICE);
    ```
- 第二种方式，使用闹钟延时，使用PendingIntent延迟意图，然后重启app
    ``` java
    RestartAppHelper.restartApp(this,RestartFactory.ALARM);
    ```
- 第三种方式，检索获取项目中LauncherActivity，然后设置该activity的flag和component启动app
    ``` java
    RestartAppHelper.restartApp(this,RestartFactory.MAINIFEST);
    ```


### 08.通用工具类库
- ToolUtils 是一个 Android 工具库。便于开发人员，快捷高效开发需求。
- 常用的基本上都有，还包括图片压缩，日历事件，异常上报工具，网络，手机方向监听，防爆力点击等工具类。
    ``` java
    FastClickUtils              防爆力点击，可以自定义设置间距时间
    PerfectClickListener        避免在1秒内出发多次点击
    AppFileUtils                file文件工具类
    FileShareUtils              file文件分享工具类
    ScreenShotUtils             监听屏幕被截图事件
    SensorManagerUtils          手机系统方向监听器工具类
    AppNetworkUtils             网络工具类
    AppProcessUtils             进程工具类，高效获取进程名称
    CompressUtils               图片压缩工具类
    ExceptionReporter           异常上报工具类
    OnceInvokeUtils             避免多次执行工具类
    StatusBarUtils              状态栏工具类
    
    //还有很多其他常用工具类，这里就不一一罗列呢
    ……
    ```


### 09.通用基类封装库
- 通用PagerAdapter封装
    - 方便快速tabLayout+ViewPager。针对页面较少的Fragment选择使用BaseFragmentPagerAdapter，针对页面较多的Fragment选择使用BasePagerStateAdapter。
- BaseLazyFragment懒加载fragment
    - 就是等到该页面的UI展示给用户时，再加载该页面的数据(从网络、数据库等)。
- BaseVisibilityFragment
    - fragment是否可见封装类，比如之前需求在页面可见时埋点就需要这个。弥补了setUserVisibleHint遇到的bug……
- ViewPager2封装库
    - DiffFragmentStateAdapter，可以用作diff操作的适配器；ViewPagerDiffCallback用来做diff计算的工具类


### 10.反射工具类库


### 11.Intent封装库
- 通用打印Intent对象内容到log日志栏中，支持普通intent和延迟PendingIntent。超级方便检查，可以打印action，category，data，flags，extras等等
    ``` java
    //打印intent所有的数据
    IntentLogger.print("intent test : ", intent)
    //打印intent中component
    IntentLogger.printComponentName("intent component : " , intent)
    //打印intent中extras参数
    IntentLogger.printExtras("intent test : ", intent)
    //打印intent中flags参数
    IntentLogger.printFlags("intent test : ", intent)
    
    //PendingIntent
    //打印intent所有的数据
    PendingIntentLogger.print("intent test : ", intent)
    //打印intent中content
    PendingIntentLogger.printContentIntent("intent content : " , intent)
    //打印intent的tag
    PendingIntentLogger.printTag("intent tag : " , intent)
    ```


### 12.基础接口库
- 背景说明：由于组件化开发中有很多基础组件，由于某些需求，需要统计一些事件，异常上报到平台上，获取控制降级，自定义日志打印等，因此采用接口回调方式实现
- IEventTrack，event事件接口，一般用于特殊事件上报作用
- IExceptionTrack，异常事件接口，一般可以用在组件库中catch捕获的时候，上报日志到服务平台操作
- ILogger，log自定义日志接口，一般用于组件库日志打印，暴露给外部开发者
- IMonitorToggle，AB测试开关接口，也可以叫降级开关，一般用于组件库某功能降级操作，暴露给开发者设置



### 13.异常上报接口库
- 基础库作为一个功能比较独立的lib，总不可能依赖APM组件吧。因此，就采用抽象类隔离！App壳工程继承抽象类，lib库直接调用抽象类。
- api调用如下所示，直接拿来用即可。下面这些调用可以用在lib库中，特轻量级上报
    ``` java
    //上报日志
    LoggerReporter.report("DiskLruCacheHelper" , "lru disk file path : " + directory.getPath());
    LoggerReporter.report("DiskLruCacheHelper clear cache");
    //上报异常
    ExceptionReporter.report("Unable to get from disk cache", e);
    ExceptionReporter.report(ioe);
    //上报event事件，通常用来埋点
    EventReporter.report("initCacheSuccess")
    EventReporter.report("initCacheSuccess",map)
    ```
- 需要在壳工程代码中，添加具体实现类。代码如下所示：
    ``` java
    //LoggerReporter，ExceptionReporter，EventReporter都是类似的
    public class LoggerReporterImpl extends LoggerReporter {
        @Override
        protected void reportLog(String eventName) {
            
        }
    
        @Override
        protected void reportLog(String eventName, String message) {
    
        }
    }
    ```



