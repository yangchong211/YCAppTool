# 框架公共组件层
#### 目录介绍
- 01.该库具有功能
- 02.崩溃收集库
- 03.网络分析库
- 04.ping库模块
- 05.磁盘file库
- 06.卡顿监测库
- 07.Anr监控库
- 08.网络异常模拟库
- 09.隐私合规库
- 10.时间统计库
- 11.FPS统计库
- 12.内存OOM库
- 13.安全处理库





### 01.该库具有功能
- **最大特点**
    - 入侵性低，你不用改动愿项目代码，几行代码设置即可使用这几个模块功能，已经用于多个实际项目中。
- 功能齐全
    - 包括平常看到的卡顿分析收集&监控；Anr模拟；FPS；监控和日志收集；崩溃拦截处理和日志界面展示；网络接口请求数据的收集和ping；网络异常模拟和弱网模拟；测速。每个均是一个独立的module，以及超全的系列文档。



#### 1.1 崩溃收集模块
- **崩溃模块**：
    - 崩溃重启操作，崩溃记录日志操作，崩溃日志列表支持查询，删除，查看详情，分享，保存文本，以及截图等操作。
- 崩溃分析流程
    - 从崩溃流程分析，到拦截App崩溃，然后收集崩溃相关信息，记录到file文件，以及到UI界面展示。一体化来学习崩溃的系统化处理。


#### 1.2 网络分析模块
- **网络分析库模块**：
    - 网络流程分析，记录每个网络请求->响应数据，方便查看很全面的请求头信息，响应头信息，以及body实体。
- **ping库模块**：
    - 通过ping检测网络问题，帮助诊断，这个在Android中检查域名的诊断信息……
- **查看网络时间和流量**
    - 网络连接，dns解析，TLS连接，请求响应等时间差……都可以记录下来通过界面展示



#### 1.3 卡顿监测模块
- **支持fps查看帧率**
    - 一键便可以开启查看页面的帧率。
- **卡顿监控方案**
    - LooperPrinter实现卡顿监控，卡顿之后的数据收集，
    - WatchDog卡顿监控，作为一种学习案例，理解它监控卡顿的原理。



#### 1.4 ANR监测模块



### 02.崩溃收集模块
#### 2.1 异常崩溃介绍
- 低入侵性接入该lib，不会影响你的其他业务。
    - 暴露崩溃重启，以及支持开发者自己捕获crash数据的接口！能够收集崩溃中的日志写入文件，记录包括设备信息，进程信息，崩溃信息(Java崩溃、Native崩溃 or ANR)，以及崩溃时内存信息到file文件中。支持用户获取崩溃列表，以及跳转崩溃日志详情页面，并且可以将崩溃日志分享，截长图，复制等操作。可以方便测试和产品给开发提出那种偶发性bug的定位日志，免得对于偶发行崩溃，开发总是不承认……开发总是不承认……

#### 2.2 截图如下所示
![image](https://img-blog.csdnimg.cn/20200902194445625.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3NzAwMjc1,size_16,color_FFFFFF,t_70#pic_center)
![image](https://img-blog.csdnimg.cn/20200902194445623.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3NzAwMjc1,size_16,color_FFFFFF,t_70#pic_center)
![image](https://img-blog.csdnimg.cn/20200902194445622.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3NzAwMjc1,size_16,color_FFFFFF,t_70#pic_center)
![image](https://img-blog.csdnimg.cn/20200902194445576.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3NzAwMjc1,size_16,color_FFFFFF,t_70#pic_center)


#### 2.3崩溃后日志记录
![image](https://img-blog.csdnimg.cn/20200904095027529.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3NzAwMjc1,size_16,color_FFFFFF,t_70#pic_center)


#### 2.4 崩溃流程图
![image](https://img-blog.csdnimg.cn/2020090417061146.jpg)



### 03.网络分析模块
#### 3.1 网络分析库模块
- 如何拿来用
    - 既然Android中使用到facebook的stetho库，可以拦截手机请求请求，然后去Chrome浏览器，在浏览器地址栏输入：chrome://inspect 。即可查看请求信息。
    - 那么能不能把这个拿到的请求信息，放到集合中，然后在Android的页面中展示呢？这样方便开发和测试查看网络请求信息，以及请求流程中的消耗时间（比如dns解析时间，请求时间，响应时间，共耗时等等）
- OkHttp如何进行各个请求环节的耗时统计呢？
    - OkHttp 版本提供了EventListener接口，可以让调用者接收一系列网络请求过程中的事件，例如DNS解析、TSL/SSL连接、Response接收等。
    - 通过继承此接口，调用者可以监视整个应用中网络请求次数、流量大小、耗时(比如dns解析时间，请求时间，响应时间等等)情况。因此统计耗时，需要致敬EventListener实现，照搬代码即可。
- 如何消耗记录时间
    - 在OkHttp库中有一个EventListener类。该类是网络事件的侦听器。扩展这个类以监视应用程序的HTTP调用的数量、大小和持续时间。
    - 所有启动/连接/获取事件最终将接收到匹配的结束/释放事件，要么成功(非空参数)，要么失败(非空可抛出)。
    - 比如，可以在开始链接记录时间；dns开始，结束等方法解析记录时间，可以计算dns的解析时间。


#### 3.2 网络拦截库功能
- 网络请求拦截
    - 记录每个页面网络请求的数据，保存在map中，方便查看网络请求的请求头，响应头，响应body
    - 针对获取的网络数据，记录网络状态，请求耗时，请求方式，完善了网络日志包括的绝大部分字段信息展示
- 流量统计
    - 目前只是针对get，post网络请求以及上传流量统计，并且针对get和post比例展示，以及抓包数量统计
    - 后期完善比如glide加载图片消耗的流量统计
- 网络消耗时间
    - 网络请求到完成耗时，dns消耗时间，连接消耗时间，TLS连接开始结束消耗时间，request请求消耗时间，response响应消耗时间等等
- 设备信息
    - 获取手机设备信息，包括硬件厂商，版本，root，包名等信息
    - 获取本机wifi信息，主要是wifi的名称，mac地址，ip地址，dns信息，子网掩码等
    - 获取服务端信息，根据网络请求host，获取服务端ip，mac，是ipv4还是ipv6等，这个还在完善中
- ping网络诊断
    - 致敬网易ping方案，应用于Android项目中，获取Android项目域名，拿到ping信息
- 全局悬浮按钮
    - 为了方便查看每个activity页面的网络请求数据，因此在每个activity页面显示全局悬浮按钮，点击即可跳入查看数据页面
    - 这个一行代码设置即可，无任何耦合作用，低入侵


#### 3.3 网络请求接口信息
- 请求接口如下所示
    - https://www.wanandroid.com/friend/json
- 看截图如下
    - ![image](https://img-blog.csdnimg.cn/20200910095754628.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3NzAwMjc1,size_16,color_FFFFFF,t_70#pic_center)


#### 3.4 案例截图如下
![image](https://img-blog.csdnimg.cn/2020090921524345.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3NzAwMjc1,size_16,color_FFFFFF,t_70#pic_center)
![image](https://img-blog.csdnimg.cn/2020090921524393.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3NzAwMjc1,size_16,color_FFFFFF,t_70#pic_center)
![image](https://img-blog.csdnimg.cn/20200910095645227.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3NzAwMjc1,size_16,color_FFFFFF,t_70#pic_center)
![image](https://img-blog.csdnimg.cn/20200914194859602.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3NzAwMjc1,size_16,color_FFFFFF,t_70#pic_center)
![image](https://img-blog.csdnimg.cn/20200923103832877.jpeg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3NzAwMjc1,size_16,color_FFFFFF,t_70#pic_center)



### 04.ping库模块
#### 4.1 ping在Android的应用
- 为了检查网络，在android上也可以通过ping来查看是否网络通。
- 实现方案有哪些
    - 通过后台线程执行ping命令的方式模拟traceroute的过程，缺点就是模拟过程较慢，timeout的出现比较频繁
    - 通过编译开源网络检测库iputilsC代码的方式对traceroute进行了套接字发送ICMP报文模拟，可以明显提高检测速度

#### 4.2 ping使用截图
![image](https://img-blog.csdnimg.cn/20200910145919655.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3NzAwMjc1,size_16,color_FFFFFF,t_70#pic_center)


### 05.磁盘file模块
#### 5.1 项目背景说明
- app展示在数据量多且刷新频繁的情况下，为提升用户体验，通常会对上次已有数据做内存缓存或磁盘缓存，以达到快速展示数据的目的。缓存的数据变化是否正确、缓存是否起到对应作用是QA需要重点测试的对象。
- android缓存路径查看方法有哪些呢？将手机打开开发者模式并连接电脑，在pc控制台输入cd /data/data/目录，使用adb主要是方便测试(删除，查看，导出都比较麻烦)。
- 如何简单快速，傻瓜式的查看缓存文件，操作缓存文件，那么该项目小工具就非常有必要呢！采用可视化界面读取缓存数据，方便操作，直观也简单。


#### 5.2 沙盒作用
- 可以通过该工具查看缓存文件
    - 快速查看`data/data/包名`目录下的缓存文件。
    - 快速查看`/sdcard/Android/data/包名`下存储文件。
- 对缓存文件处理
    - 支持查看file文件列表数据，打开缓存文件查看数据详情。还可以删除缓存对应的文件或者文件夹，并且友好支持分享到外部。
    - 能够查看缓存文件修改的信息，修改的时间，缓存文件的大小，获取文件的路径等等。都是在可视化界面上处理。


#### 5.3 设计目标
- 可视化界面展示
![在这里插入图片描述](https://img-blog.csdnimg.cn/cccea61a47ed43ef88ec3f335f84ab0b.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5p2o5YWF,size_14,color_FFFFFF,t_70,g_se,x_16)
![在这里插入图片描述](https://img-blog.csdnimg.cn/81e1797d2220499cae25b90c932d1092.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5p2o5YWF,size_15,color_FFFFFF,t_70,g_se,x_16)
![在这里插入图片描述](https://img-blog.csdnimg.cn/2752b6b0e866402f8e76c8c544028de0.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5p2o5YWF,size_15,color_FFFFFF,t_70,g_se,x_16)
![在这里插入图片描述](https://img-blog.csdnimg.cn/221754c0dd7b4c028d5f0a122bdecd18.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5p2o5YWF,size_15,color_FFFFFF,t_70,g_se,x_16)
- 多种处理文件操作
    - 针对file文件夹，或者file文件，长按可以出现弹窗，让测试选择是否删除文件。
    - 点击file文件夹，则拿到对应的文件列表，然后展示。点击file直到是具体文件(文本，图片，db，json等非file文件夹)跳转详情。
- 一键接入该工具
    - FileExplorerActivity.startActivity(MainActivity.this);




### 07.如何使用该库
#### 7.1 崩溃库
- 如何引入该库
    ``` java
    //磁盘文件查看工具
    implementation 'com.github.yangchong211.YCAndroidTool:MonitorFileLib:1.2.8'
    //卡顿工具
    implementation 'com.github.yangchong211.YCAndroidTool:MonitorCatonLib:1.2.8'
    //网络工具
    implementation 'com.github.yangchong211.YCAndroidTool:MonitorNetLib:1.2.8'
    //崩溃工具
    implementation 'com.github.yangchong211.YCAndroidTool:MonitorCrashLib:1.2.8'
    //anr工具
    implementation 'com.github.yangchong211.YCAndroidTool:MonitorAnrLib:1.2.8'
    ```
- 初始化代码如下所示。建议在Application中初始化……
    ``` java
    CrashHandler.getInstance().init(this, new CrashListener() {
        /**
         * 重启app
         */
        @Override
        public void againStartApp() {
            CrashToolUtils.reStartApp1(App.this,1000);
            //CrashToolUtils.reStartApp2(App.this,1000, MainActivity.class);
            //CrashToolUtils.reStartApp3(AppManager.getAppManager().currentActivity());
        }

        /**
         * 自定义上传crash，支持开发者上传自己捕获的crash数据
         * @param ex                        ex
         */
        @Override
        public void recordException(Throwable ex) {
            //自定义上传crash，支持开发者上传自己捕获的crash数据
            //StatService.recordException(getApplication(), ex);
        }
    });
    ```
- 关于重启App的操作有三种方式api
    ``` java
    //开启一个新的服务KillSelfService，用来重启本APP【使用handler延迟】
    CrashToolUtils.reStartApp1(App.this,1000);
    //用来重启本APP[使用闹钟，整体重启，临时数据清空（推荐）]
    CrashToolUtils.reStartApp2(App.this,1000, MainActivity.class);
    //检索获取项目中LauncherActivity，然后设置该activity的flag和component启动app【推荐】
    CrashToolUtils.reStartApp3(AppManager.getAppManager().currentActivity());
    ```
- 关于获取崩溃目录api
    ``` java
    //崩溃文件存储路径：/storage/emulated/0/Android/data/你的包名/cache/crashLogs
    //崩溃页面截图存储路径：/storage/emulated/0/Android/data/你的包名/cache/crashPics
    String crashLogPath = ToolFileUtils.getCrashLogPath(this);
    String crashPicPath = ToolFileUtils.getCrashPicPath(this);
    ```
- 关于崩溃日志记录
    - 日志记录路径：/storage/emulated/0/Android/data/你的包名/cache/crashLogs
    - 日志文件命名：V1.0_2020-09-02_09:05:01_java.lang.NullPointerException.txt【版本+日期+异常】
- 关于跳转错误日志list列表页面
    - 跳转日志列表页面如下所示，这里调用一行代码即可。点击该页面list条目即可进入详情
    ``` java
    CrashToolUtils.startCrashListActivity(this);
    ```
- 那么如何获取所有崩溃日志的list呢。建议放到子线程中处理！！
    ``` java
    List<File> fileList = ToolFileUtils.getCrashFileList(this);

    //如果是要自己拿到这些文件，建议根据时间来排个序
    //排序
    Collections.sort(fileList, new Comparator<File>() {
        @Override
        public int compare(File file01, File file02) {
            try {
                //根据修改时间排序
                long lastModified01 = file01.lastModified();
                long lastModified02 = file02.lastModified();
                if (lastModified01 > lastModified02) {
                    return -1;
                } else {
                    return 1;
                }
            } catch (Exception e) {
                return 1;
            }
        }
    });
    ```
- 如何删除单个文件操作
    ``` java
    //返回true表示删除成功
    boolean isDelete = ToolFileUtils.deleteFile(file.getPath());
    ```
- 如何删除所有的文件。建议放到子线程中处理！！
    ``` java
    File fileCrash = new File(ToolFileUtils.getCrashLogPath(CrashListActivity.this));
    ToolFileUtils.deleteAllFiles(fileCrash);
    ```
- 如何获取崩溃文件中的内容
    ``` java
    //获取内容
    String crashContent = ToolFileUtils.readFile2String(filePath);
    ```
- 还有一些关于其他的api，如下。这个主要是方便测试同学或者产品，避免开发不承认那种偶发性崩溃bug……
    ```
    //拷贝文件，两个参数分别是源文件，还有目标文件
    boolean copy = ToolFileUtils.copyFile(srcFile, destFile);
    //分享文件。这个是调用原生的分享
    CrashLibUtils.shareFile(CrashDetailsActivity.this, destFile);
    //截图崩溃然后保存到相册。截图---> 创建截图存储文件路径---> 保存图片【图片质量，缩放比还有采样率压缩】
    final Bitmap bitmap = ScreenShotsUtils.measureSize(this,view);
    String crashPicPath = ToolFileUtils.getCrashPicPath(CrashDetailsActivity.this) + "/crash_pic_" + System.currentTimeMillis() + ".jpg";
    boolean saveBitmap = CrashLibUtils.saveBitmap(CrashDetailsActivity.this, bitmap, crashPicPath);
    ```
- 异常恢复原理
    - 第一种方式，开启一个新的服务KillSelfService，用来重启本APP。
        ``` java
        CrashToolUtils.reStartApp1(App.this,1000);
        ```
    - 第二种方式，使用闹钟延时，然后重启app
        ``` java
        CrashToolUtils.reStartApp2(App.this,1000, MainActivity.class);
        ```
    - 第三种方式，检索获取项目中LauncherActivity，然后设置该activity的flag和component启动app
        ``` java
        CrashToolUtils.reStartApp3(AppManager.getAppManager().currentActivity());
        ```
    - 关于app启动方式详细介绍
        - [App启动介绍](https://github.com/yangchong211/YCAndroidTool/blob/master/read/06.App%E9%87%8D%E5%90%AF%E5%87%A0%E7%A7%8D%E6%96%B9%E5%BC%8F.md)


#### 7.2 如何网络拦截
- 如下所示
    ``` java
    new OkHttpClient.Builder()
        //配置工厂监听器。主要是计算网络过程消耗时间
        .eventListenerFactory(NetworkListener.get())
        //主要是处理拦截请求，响应等信息
        .addNetworkInterceptor(new NetworkInterceptor())
        .build()
    ```
- 如何开启悬浮按钮
    ```
    //建议只在debug环境下显示，点击去网络拦截列表页面查看网络请求数据
    NetworkTool.getInstance().setFloat(getApplication());
    ```
- 该库目的
    - 做成悬浮全局按钮，点击按钮可以查看该activity页面请求接口，可以查看请求几个接口，以及接口请求到响应消耗流量
    - 方便查看网络请求流程，比如dns解析时间，请求时间，响应时间
    - 方便测试查看请求数据，方便抓包。可以复制request，respond，body等内容。也可以截图


#### 7.3 如何使用ping
- 直接创建一个ping，需要传递一个网址url
    ``` java
    _netDiagnoService = new NetDiagnoService(getContext(), getContext().getPackageName()
            , versionName, userId, deviceId, host, this);
    _netDiagnoService.execute();
    ```
- 如何取消ping
    ``` java
    if (_netDiagnoService!=null){
        _netDiagnoService.cancel(true);
        _netDiagnoService = null;
    }
    ```
- 或者直接停止ping。停止线程允许，并把对象设置成null
    ``` java
    _netDiagnoService.stopNetDialogsis();
    ```
- 关于监听
    ``` java
    /**
     * 诊断结束，输出全部日志记录
     * @param log                       log日志输出
     */
    @Override
    public void OnNetDiagnoFinished(String log) {
        setText(log);
    }

    /**
     * 监控网络诊断过程中的日志输出
     * @param log                       log日志输出
     */
    @Override
    public void OnNetDiagnoUpdated(String log) {
        showInfo += log;
        setText(showInfo);
    }
    ```




