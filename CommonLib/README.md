# 框架公共组件层
#### 目录介绍
- 01.框架公共组件层
- 02.组件化建设
- 03.公共组件库依赖
- 04.Activity栈管理库[ActivityManager]()
- 05.通用存储库[AppBaseStore]()
- 06.Log日志打印库[AppLogLib]()
- 07.App重新启动库[AppRestartLib]()
- 08.通用工具类库[ToolUtilsLib]()
- 09.通用基类封装库[BaseClassLib]()
- 10.反射工具类库[ReflectionLib]()
- 11.Intent封装库[SafeIntentLib]()
- 12.基础接口库[AppCommonInter]()
- 13.异常上报接口库[EventUploadLib]()
- 14.File流读写库[FileIoHelper]()
- 15.加密和解密库[AppEncryptLib]()
- 16.Lru内存缓存库[AppLruCache]()
- 17.Lru磁盘缓存库[AppLruDisk]()
- 18.磁盘分区库[AppMediaStore]()
- 19.来电和去电监听[ActivityManager]()
- 20.广播监听库[AppStatusLib]()
- 21.权限申请封装库[AppPermission]()
- 22.Fragment周期监听[FragmentManager]()
- 23.App启动优化库[ParallelTaskLib]()
- 25.屏幕截屏库[AppScreenLib]()
- 26.内存获取库[ToolMemoryLib]()
- 27.Wi-Fi工具库[AppWiFiLib]()
- 28.Vp相关适配器库[BaseVpAdapter]()
- 30.网络判断库[NetWorkLib]()
- 31.手机方向监听库[PhoneSensor]()
- 32.File文件库[ToolFileLib]()
- 33.Zip压缩库[ZipFileLib]()
- 34.多次点击判断库[AppClickHelper]()





### 01.框架公共组件层
- 组件化开发中基础公共库
    - activity栈管理；Log日志；通用缓存库(支持sp，mmkv，lru，disk，fastsp等多种存储方式切换)；App重启；
    - 通用全面的工具类Utils；通用基类fragment，adpater，activity等简单封装；intent内容打印到控制台库


### 02.组件化建设
- 按照不同层级架构图如下所示
    - ![image](https://github.com/yangchong211/YCAppTool/blob/master/Image/02.App%E7%BB%84%E4%BB%B6%E5%8C%96%E6%9E%B6%E6%9E%84%E5%9B%BE.jpg)


### 03.公共组件库依赖
- 关于依赖库如下所示，可以根据需求选择性使用：
    ```
    //base基类
    implementation 'com.github.yangchong211.YCCommonLib:BaseClassLib:1.4.9'
    implementation 'com.github.yangchong211.YCCommonLib:ComponentLib:1.4.9'
    //工具类utils
    implementation 'com.github.yangchong211.YCCommonLib:ToolUtilsLib:1.4.9'
    //activity栈管理
    implementation 'com.github.yangchong211.YCCommonLib:ActivityManager:1.4.9'
    //通用缓存存储库，支持sp，fastsp，mmkv，lruCache，DiskLruCache等
    implementation 'com.github.yangchong211.YCCommonLib:AppBaseStore:1.4.9'
    //通用日志输出库
    implementation 'com.github.yangchong211.YCCommonLib:AppLogLib:1.4.9'
    //app重启库
    implementation 'com.github.yangchong211.YCCommonLib:AppRestartLib:1.4.9'
    //intent内容输出到控制台
    implementation 'com.github.yangchong211.YCCommonLib:SafeIntentLib:1.4.9'
    //通用组件接口库
    implementation 'com.github.yangchong211.YCCommonLib:AppCommonInter:1.4.9'
    //各种广播监听哭
    implementation 'com.github.yangchong211.YCCommonLib:AppStatusLib:1.4.9'
    //基建库
    implementation 'com.github.yangchong211.YCCommonLib:ArchitectureLib:1.4.9'
    //同上上报库
    implementation 'com.github.yangchong211.YCCommonLib:EventUploadLib:1.4.9'
    //权限库
    implementation 'com.github.yangchong211.YCCommonLib:AppPermission:1.4.9'
    //Lru磁盘缓存库
    implementation 'com.github.yangchong211.YCCommonLib:AppLruDisk:1.4.9'
    //Lru内存缓存库
    implementation 'com.github.yangchong211.YCCommonLib:AppLruCache:1.4.9'
    //fragment生命周期监听库
    implementation 'com.github.yangchong211.YCCommonLib:FragmentManager:1.4.9'
    //反射工具库
    implementation 'com.github.yangchong211.YCCommonLib:ReflectionLib:1.4.9'
    //App启动优化库
    implementation 'com.github.yangchong211.YCCommonLib:ParallelTaskLib:1.4.9'
    //Context上下文库
    implementation 'com.github.yangchong211.YCCommonLib:AppContextLib:1.4.9'
    //加解密库
    implementation 'com.github.yangchong211.YCCommonLib:AppEncryptLib:1.4.9'
    //handler包装库
    implementation 'com.github.yangchong211.YCCommonLib:AppHandlerLib:1.4.9'
    //Application库
    implementation 'com.github.yangchong211.YCCommonLib:ApplicationLib:1.4.9'
    //store磁盘分区库
    implementation 'com.github.yangchong211.YCCommonLib:AppMediaStore:1.4.9'
    //内存
    implementation 'com.github.yangchong211.YCCommonLib:ToolMemoryLib:1.4.9'
    //屏幕截屏库
    implementation 'com.github.yangchong211.YCCommonLib:AppScreenLib:1.4.9'
    //Wi-Fi库
    implementation 'com.github.yangchong211.YCCommonLib:AppWiFiLib:1.4.9'
    //Vp相关适配器库
    implementation 'com.github.yangchong211.YCCommonLib:BaseVpAdapter:1.4.9'
    //io流读写库
    implementation 'com.github.yangchong211.YCCommonLib:FileIoHelper:1.4.9'
    //图片工具库
    implementation 'com.github.yangchong211.YCCommonLib:ImageToolLib:1.4.9'
    //网络判断库
    implementation 'com.github.yangchong211.YCCommonLib:NetWorkLib:1.4.9'
    //手机
    implementation 'com.github.yangchong211.YCCommonLib:PhoneSensor:1.4.9'
    //File文件库
    implementation 'com.github.yangchong211.YCCommonLib:ToolFileLib:1.4.9'
    //Zip压缩库
    implementation 'com.github.yangchong211.YCCommonLib:ZipFileLib:1.4.9'
    //图片压缩
    implementation 'com.github.yangchong211.YCCommonLib:AppCompress:1.4.9'
    ```


### 04.Activity栈管理库
- 非常好用的activity任务栈管理库，自动化注册。
    - 完全解耦合的activity栈管理，拿来即可用，或者栈顶Activity，移除添加，推出某个页面，获取应用注册Activity列表等，可以注册监听某个页面的生命周期，小巧好用。
    ```
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
- 如何依赖该库
    ```
    
    ```
- 相关文档连接
    - [ReadMe]()



### 05.通用存储库
- 业务背景：
    - 项目中有用到sp，mmkv，file，内存缓存，缓存的使用场景有很多，那么能否可以打造一套通用api的缓存方案。
- 通用缓存方案：
    - 不管是sp，mmkv，lru，disk，file，内存缓存，都使用同一套api。大大简化了磁盘缓存应用流程……
- 无缝切换：
    - 常见一套api+不同接口实现+代理类+工厂模型
- 方案替代稳定性：
    - 该库异常上报，容错性，已经暴露外部config配置，打造通用的轮子
- 内部开源该库：
    - 作为技术沉淀，当作专项来推动进展。高复用低耦合，便于拓展，可快速移植，解决各个项目使用内存缓存，sp，mmkv，sql，lru，DataStore的凌乱。抽象一套统一的API接口。





### 06.Log日志打印库
- 通用日志库框架，专用LogCat工具，主要功能全局配置log输出, 个性化设置Tag，可以设置日志打印级别，支持打印复杂对象，可以实现自定义日志接口，支持简化版本将日志写入到文件中。小巧好用！
- 第一步：初始化操作
    ```
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
    ```
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
    - 使用到了简单工厂模式，通过工厂类，只要提供一个参数传给工厂，就可以直接得到一个想要的产品对象，并且可以按照接口规范来调用产品对象的所有功能（方法）。
    - 比如App切换了debug或者release环境，需要进行app重新启动，可以使用该库，小巧好用。一行代码搞定，傻瓜式使用！
- 第一种方式，开启一个新的服务KillSelfService，用来重启本APP。
    ```
    RestartAppHelper.restartApp(this,RestartFactory.SERVICE);
    ```
- 第二种方式，使用闹钟延时，使用PendingIntent延迟意图，然后重启app
    ```
    RestartAppHelper.restartApp(this,RestartFactory.ALARM);
    ```
- 第三种方式，检索获取项目中LauncherActivity，然后设置该activity的flag和component启动app
    ```
    RestartAppHelper.restartApp(this,RestartFactory.MAINIFEST);
    ```


### 08.通用工具类库
- ToolUtils 是一个 Android 工具库。便于开发人员，快捷高效开发需求。
- 常用的基本上都有，还包括图片压缩，日历事件，异常上报工具，网络，手机方向监听，防爆力点击等工具类。
    ```
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
    ```
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
    ```
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
    ```
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


### 14.File流读写库
#### 14.1 从文件中读数据
- 从文件中读数据，使用普通字节流或者字符流方式，如下所示
    ```
    //字节流读取file文件，转化成字符串
    String file2String = FileIoUtils.readFile2String1(fileName);
    //字符流读取file文件，转化成字符串
    String file2String = FileIoUtils.readFile2String2(fileName);
    ```
- 从文件中读数据，使用高效流方式，如下所示
    ```
    //高效字节流读取file文件，转化成字符串
    String file2String = BufferIoUtils.readFile2String1(fileName);
    //高效字符流读取file文件，转化成字符串
    String file2String = BufferIoUtils.readFile2String2(fileName);
    ```


#### 14.2 将内容写入文件
- 从文件中读数据，使用普通字节流或者字符流方式，如下所示
    ```
    //使用字节流，写入字符串内容到文件中
    FileIoUtils.writeString2File1(content,fileName);
    //使用字符流，写入字符串内容到文件中
    FileIoUtils.writeString2File2(content,fileName);
    ```
- 从文件中读数据，使用高效流方式，如下所示
    ```
    //高效字节流写入字符串内容到文件中
    BufferIoUtils.writeString2File1(content,fileName);
    //高效字符流写入字符串内容到文件中
    BufferIoUtils.writeString2File2(content,fileName);
    ```


#### 14.3 文件复制操作
- 使用字节流&字符流复制
    ```
    //使用字节流复制文件，根据文件路径拷贝文件。
    FileIoUtils.copyFile1(fileName,newFileName);
    //使用字符流复制文件，根据文件路径拷贝文件。
    FileIoUtils.copyFile2(fileName,newFileName);
    ```
- 使用高效流复制
    ```
    //使用高效字符缓冲流，根据文件路径拷贝文件。
    BufferIoUtils.copyFile1(fileName,newFileName);
    //使用高效字节缓冲流，根据文件路径拷贝文件
    BufferIoUtils.copyFile2(fileName,newFileName);
    ```


#### 14.4 将流对象写入文件
- 将InputStream流对象写入到本地文件中
    ```
    //使用字符流读取流数据到新的file文件中
    FileIoUtils.writeFileFromIS1(is,srcFile);
    //使用字节流将流数据写到文件中
    FileIoUtils.writeFileFromIS1(is,fileName);
    ```



### 15.加密和解密库
- 关于MD5加密Api如下所示
    ```
    //对字符串md5加密
    String md2 = Md5EncryptUtils.encryptMD5ToString(string);
    //对字符串md5加密，加盐处理
    String md3 = Md5EncryptUtils.encryptMD5ToString(string,"doubi");
    //对字节数据md5加密
    String md4 = Md5EncryptUtils.encryptMD5ToString(bytes);
    //对字节数据md5加密，加盐处理
    String md5 = Md5EncryptUtils.encryptMD5ToString(bytes,"doubi".getBytes());
    //对文件进行md5加密
    String md6 = Md5EncryptUtils.encryptMD5File1(txt);
    //对文件进行md5加密
    String md7 = Md5EncryptUtils.encryptMD5File2(new File(txt));
    ```
- 关于base64加密和解密的Api如下所示
    ```
    //字符Base64加密
    String strBase64_2 = Base64Utils.encodeToString(str);
    //字符Base64解密
    String strBase64_3 = Base64Utils.decodeToString(strBase64_2);
    ```
- 关于DES加密和解密的Api如下所示
    ```
    //DES加密字符串
    String encrypt1 = DesEncryptUtils.encrypt(string,password);
    //DES解密字符串
    String decrypt1 = DesEncryptUtils.decrypt(encrypt1 , password);
    //DES加密文件
    String encryptFile1 = DesEncryptUtils.encryptFile(password, fileName, destFile);
    //DES解密文件
    String decryptFile1 = DesEncryptUtils.decryptFile(password, destFile, destFile3);
    //DES 加密后转为 Base64 编码
    String encrypt2 = DesEncryptUtils.encrypt(string.getBytes(), password.getBytes());
    //DES解密字符串 Base64 解码
    String decrypt2 = DesEncryptUtils.decrypt(encrypt2.getBytes(), password.getBytes());
    ```
- 关于AES加密和解密的Api如下所示
    ```
    //AES加密字符串
    String encrypt1 = AesEncryptUtils.encrypt(string,password);
    //AES解密字符串
    String decrypt1 = AesEncryptUtils.decrypt(encrypt1 , password);
    ```
- 关于RC4加密和解密的Api如下所示
    ```
    //RC4加密
    String encrypt1 = Rc4EncryptUtils.encryptString(string, secretKey);
    //RC4解密
    String decrypt1 = Rc4EncryptUtils.decryptString(encrypt1, secretKey);
    //RC4加密base64编码数据
    String encrypt2 = Rc4EncryptUtils.encryptToBase64(bytes1, secretKey);
    //RC4解密base64解码数据
    byte[] bytes = Rc4EncryptUtils.decryptFromBase64(encrypt2, secretKey);
    ```


### 16.Lru内存缓存库



### 17.Lru磁盘缓存库



### 19.来电和去电监听
- 业务场景说明
    - 在App进行音视频聊天的时，这个时候来电了，电话接通后，需要关闭音视频聊天。这个时候就需要监听电话来电和去电状态。
- 如何依赖该库
    ```
    
    ```
- 相关文档连接
    - [ReadMe]()


