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


### 01.框架公共组件层


### 02.组件化建设


### 03.公共组件库依赖


### 04.Activity栈管理库
- 非常好用的activity任务栈管理库，自动化注册。拿来即可用，或者栈顶Activity，移除添加，推出某个页面，获取应用注册Activity列表等，可以注册监听某个页面的生命周期，小巧好用。
    ``` java
    //添加 activity
    ActivityManager.getInstance().add(this);
    //移除 activity
    ActivityManager.getInstance().remove(this);
    //结束指定的Activity
    ActivityManager.getInstance().finish(this);
    //结束所有Activity
    ActivityManager.getInstance().finishAll();
    //退出应用程序。先回退到桌面，然后在杀死进程
    ActivityManager.getInstance().appExist();
    //这个是监听目标Activity的生命周期变化
    ActivityManager.getInstance().registerActivityLifecycleListener(
            CommonActivity.class,new ActivityLifecycleListener(){
                @Override
                public void onActivityCreated(@Nullable Activity activity, Bundle savedInstanceState) {
                    super.onActivityCreated(activity, savedInstanceState);
                }
            });
    //移除栈顶的activity
    ActivityManager.getInstance().pop();
    //获取栈顶的Activity
    Activity activity = ActivityManager.getInstance().peek();
    //判断activity是否处于栈顶
    ActivityManager.getInstance().isActivityTop(this,"MainActivity");
    //返回AndroidManifest.xml中注册的所有Activity的class
    ActivityManager.getInstance().getActivitiesClass(
            this, AppUtils.getAppPackageName(),null);
    ```



### 05.通用存储库


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


### 08.通用工具类库


### 09.通用基类封装库


### 10.反射工具类库


### 11.Intent封装库





